import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UploadService {
    private CascadeClassifier faceDetector;
    private double hatGrowthFactor = 2.3;
    private double hatOffsetY = 0.6;

    static {
        //Place opencv_java330.dll in %classpath%\bin
        //Run JVM with -Djava.library.path="%classpath%\bin"
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void setHatGrowthFactor(double hatGrowthFactor) {
        this.hatGrowthFactor = hatGrowthFactor;
    }

    public void setHatOffsetY(double hatOffsetY) {
        this.hatOffsetY = hatOffsetY;
    }

    private void loadCascade(){
        String cascadePath = Utils.getResourcePath("cascades/lbpcascade_frontalface_improved.xml");
        faceDetector = new org.opencv.objdetect.CascadeClassifier(cascadePath);
    }

    private Mat convertBytesToMatrix(byte[]bytes){
        Mat encodedMat = new Mat(bytes.length, 1, CvType.CV_8U);
        encodedMat.put(0, 0, bytes);
        return Imgcodecs.imdecode(encodedMat, Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
    }

    private Mat loadOverlayImage(){
        String overlayFileName = Utils.getResourcePath("images/fedora.png");
        Mat overlay = null;
        try{
            overlay = openFile(overlayFileName);
        }catch (Exception e){
            e.printStackTrace();
        }

        return overlay;
    }

    private Mat openFile(String fileName) throws Exception{
        Mat image = Imgcodecs.imread(fileName, Imgcodecs.IMREAD_UNCHANGED);
        if(image.dataAddr()==0){
            throw new Exception("Couldn't open file "+fileName);
        }
        return image;
    }

    private void detectFaceAndDrawHat(Mat image, Mat overlay){
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections); //, 1.1, 7, 0, new Size(),  new Size()

        for(Rect rect: faceDetections.toArray()){

            int hatWidth = (int) (rect.width *hatGrowthFactor);
            int hatHeight = (int) (hatWidth * overlay.height() / overlay.width());

            //region of interest
            int roiX =  rect.x - (hatWidth-rect.width)/2;
            int roiY =  (int) (rect.y  - hatOffsetY *hatHeight);
            roiX =  roiX<0?0:roiX;
            roiY = roiY<0?0:roiY;

            hatWidth = hatWidth+roiX > image.width() ? image.width() -roiX : hatWidth;
            hatHeight = hatHeight+roiY > image.height() ? image.height() - roiY : hatHeight;

            Mat resized = new Mat();
            Size size = new Size(hatWidth, hatHeight);

            Imgproc.resize(overlay, resized, size);
            Mat result = overlayImage(image, resized, new Point(roiX, roiY));
            result.copyTo(image);
        }
    }

    private void detectAndDrawFace(Mat image) {
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(	image, faceDetections, 1.1, 7,0,new Size(250,40),new Size());
        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }
    }

    private Mat overlayImage(Mat background, Mat foreground, Point location){
        Mat dest = new Mat(background.size(), background.type());
        background.copyTo(dest);

        for(int y = (int) Math.max(location.y, 0); y < background.rows(); ++y){
            int fY = (int) (y - location.y);

            if(fY >= foreground.rows()) break;

            for(int x = (int) Math.max(location.x, 0); x < background.cols(); ++x){
                int fX = (int) (x - location.x);

                if(fX >= foreground.cols()) break;

                double b[] = background.get(y, x);
                double f[] = foreground.get(fY, fX);

                double alpha = f[3]/255.0;
                double d[] = new double[3];

                for (int k = 0; k < 3; k++) {
                    d[k] = f[k]*alpha + b[k]*(1.0 - alpha);
                }

                dest.put(y, x, d);
            }
        }

        return dest;
    }

    public String getModifiedImage (byte[] imageBytes, String fileName) {

        loadCascade();

        Mat image = convertBytesToMatrix(imageBytes);
        Mat overlay = loadOverlayImage();

        detectFaceAndDrawHat(image, overlay);

        if(fileName.isEmpty()) {
            fileName = "images/"+Utils.getRandomName("jpg");
        }

        Imgcodecs.imwrite(Utils.getResourcePath(fileName), image);

        return fileName;
    }

    private void createAlphaFromWhite(String fileName){
        Mat mat1 = Imgcodecs.imread(fileName);
        Mat mask = new Mat(mat1.size(), CvType.CV_8U);

        final double[]white = {255,255,255};

        for (int i = 0; i < mat1.rows(); i++) {
            for (int j = 0; j < mat1.cols(); j++) {
                double[]pixel = mat1.get(i, j);
                if(Arrays.equals(pixel, white)){
                    mask.put(i, j, 0);
                }else{
                    mask.put(i, j, 255);
                }
            }
        }

        List<Mat>layers = new ArrayList<>();
        Core.split(mat1, layers);
        layers.add(mask);

        Mat mat2 = new Mat(mat1.size(), CvType.CV_32S);
        Core.merge(layers, mat2);

        Imgcodecs.imwrite(fileName+".png", mat2);
    }

    public String generateAvatarByConfig(String fileName){
        FeatureLayer[]layers = Utils.getFeatureLayers();

        Mat mResult = new Mat(new Size(1024, 1024), CvType.CV_8UC(3), new Scalar(255, 255, 255));

        for (FeatureLayer layer : layers) {
            if(layer.getAvailability() >= Utils.getRandomInt(100)){
                String layerFile = Utils.getRandomFile(Utils.getResourcePath("images/person/"+layer.getPath()));
                Mat mLayer = Imgcodecs.imread(layerFile, Imgcodecs.IMREAD_UNCHANGED);
                mResult = overlayImage(mResult, mLayer, new Point());
            }
        }

        if(fileName.isEmpty()) {
            fileName = "images/person/"+Utils.getRandomName("jpg");
        }

        Imgcodecs.imwrite(Utils.getResourcePath(fileName), mResult);

        return fileName;
    }
}
