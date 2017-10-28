package service;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import model.Person;
import util.Util;

import java.util.Map;

public class UploadService {

    static {
        //Place opencv_java330.dll in %classpath%\bin
        //Run JVM with -Djava.library.path="%classpath%\bin"
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public Mat overlayImage(Mat background, Mat foreground, Point location){
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

    public void generatePersonImage(Person person){
        Mat mResult = new Mat(new Size(1024, 1024), CvType.CV_8UC(3), new Scalar(255, 255, 255));

        for(Map.Entry<String, String>entry: person.getSelectedFeatures().entrySet()) {
            String layerPath;
            if(entry.getValue().equals("blank.png")){
                layerPath = Util.getResourcePath("images/person/" + entry.getValue());
            }else {
                layerPath = Util.getResourcePath("images/person/" + entry.getKey() + "/" + entry.getValue());
            }

            Mat mLayer = Imgcodecs.imread(layerPath, Imgcodecs.IMREAD_UNCHANGED);
            mResult = overlayImage(mResult, mLayer, new Point());
        }

        Imgcodecs.imwrite(Util.getResourcePath(person.getFileName()), mResult);
    }
}
