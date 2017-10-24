package service;

import model.FeatureLayer;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import to.PersonTo;
import util.Util;

import java.io.File;
import java.util.HashMap;
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

    public PersonTo generateAvatarByConfig(String fileName){
        FeatureLayer[]layers = Util.getFeatureLayers();
        Map<String, String>selectedFeatures = new HashMap<>();

        Mat mResult = new Mat(new Size(1024, 1024), CvType.CV_8UC(3), new Scalar(255, 255, 255));

        for (FeatureLayer layer : layers) {
            if(layer.getAvailability() >= Util.getRandomInt(100)){

                File layerFile = Util.getRandomFile(
                        Util.getResourcePath(
                                "images/person/"+layer.getPath()
                        ));

                Mat mLayer = Imgcodecs.imread(layerFile.getAbsolutePath(), Imgcodecs.IMREAD_UNCHANGED);
                mResult = overlayImage(mResult, mLayer, new Point());

                selectedFeatures.put(layer.getPath(), layerFile.getName());
            }
        }

        Imgcodecs.imwrite(Util.getResourcePath(fileName), mResult);

        PersonTo person = new PersonTo();
        person.setFileName(fileName);
        person.setSelectedFeatures(selectedFeatures);

        return person;
    }
}
