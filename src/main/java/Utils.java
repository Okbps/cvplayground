import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Random;

class Utils {

    static long copy(InputStream source, OutputStream sink) throws IOException
    {
        long nread = 0L;
        byte[] buf = new byte[8192];
        int n;
        while ((n = source.read(buf)) > 0) {
            sink.write(buf, 0, n);
            nread += n;
        }
        return nread;
    }

    static String getRandomName(String ext){
        final SecureRandom random = new SecureRandom();

        long n = random.nextLong();
        if (n == Long.MIN_VALUE) {
            n = 0;      // corner case
        } else {
            n = Math.abs(n);
        }

        return Long.toString(n) + (ext.isEmpty() ? "" : ".") + ext;

    }

    static String getFileExtension(String name) {
        return name.substring(name.lastIndexOf("."));
    }

    static String getResourcePath(String path){
        String absoluteName = Utils.class.getResource("").getPath()+path;
        return absoluteName.replaceFirst("/", "");
    }

    static File[] getFiles(String folderPath){
        File folder = new File(folderPath);
        return folder.listFiles();
    }

    static String getRandomFile(String folderPath){
        File[]files = getFiles(folderPath);

        return files[getRandomInt(files.length)].getAbsolutePath();
    }

    static int getRandomInt(int bound){
        return new Random().nextInt(bound);
    }

    static boolean getRandomBoolean(){
        return new Random().nextBoolean();
    }


    // JSON

    static void writeJson(OutputStream out, Object o){
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(out, o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static FeatureLayer[] getFeatureLayers(){
        FeatureLayer[]layers = null;

        try {
            byte[] jsonData = Files.readAllBytes(
                    Paths.get(getResourcePath("images/person/config.json")
                    ));
            ObjectMapper mapper = new ObjectMapper();
            layers = mapper.readValue(jsonData, FeatureLayer[].class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return layers;
    }
}
