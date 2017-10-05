import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@MultipartConfig(fileSizeThreshold = 1024*1024*2,
maxFileSize = 1024*1024*10,
maxRequestSize = 1024*1024*50)
public class UploadServlet extends HttpServlet{
    private CascadeClassifier faceDetector;

    private void loadCascade(){
        String cascadePath = getResourcePath("cascades/lbpcascade_frontalface_improved.xml");
        faceDetector = new org.opencv.objdetect.CascadeClassifier(cascadePath);
    }

    private String getResourcePath(String path){
        String absoluteName = getClass().getResource(path).getPath();
        return absoluteName.replaceFirst("/", "");
    }

    private Mat receiveImage(HttpServletRequest req) throws IOException, ServletException {
        byte[] imageBytes = receiveImageBytes(req);
        return convertBytesToMatrix(imageBytes);
    }

    private Mat convertBytesToMatrix(byte[]bytes){
        Mat encodedMat = new Mat(bytes.length, 1, CvType.CV_8U);
        encodedMat.put(0, 0, bytes);
        return Imgcodecs.imdecode(encodedMat, Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
    }

    private byte[] receiveImageBytes(HttpServletRequest req) throws IOException, ServletException {
        InputStream is = req.getPart("fileatr").getInputStream();

        BufferedInputStream bin = new BufferedInputStream(is);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int ch = 0;
        while((ch=bin.read())!=-1){
            buffer.write(ch);
        }
        buffer.flush();
        bin.close();
        return buffer.toByteArray();
    }

    private Mat loadOverlayImage(){
        String overlayFileName = getResourcePath("images/fedora.png");
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
        faceDetector.detectMultiScale(image, faceDetections, 1.1, 7, 0, new Size(),  new Size());

        for(Rect rect: faceDetections.toArray()){
            double hatGrowthFactor = 2.3;//1.8;

            int hatWidth = (int) (rect.width *hatGrowthFactor);
            int hatHeight = (int) (hatWidth * overlay.height() / overlay.width());

             //region of interest
            int roiX =  rect.x - (hatWidth-rect.width)/2;
            int roiY =  (int) (rect.y  - 0.6*hatHeight);
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

    private void writeResponse(HttpServletResponse resp, Mat image) throws IOException {
        MatOfByte outBuffer = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, outBuffer);

        resp.setContentType("image/jpeg");
        ServletOutputStream out = resp.getOutputStream();

        out.write(outBuffer.toArray());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.load(getResourcePath("/bin/" + Core.NATIVE_LIBRARY_NAME + ".dll"));
//        System.load("D:/Soft/Java/Projects/cvplayground/src/main/resources/bin/opencv_java330.dll");
        loadCascade();

        Mat image = receiveImage(req);
        Mat overlay = loadOverlayImage();

        detectFaceAndDrawHat(image, overlay);
        writeResponse(resp, image);
    }

}
