import java.io.*;
import java.security.SecureRandom;

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

    static String getRandomName(){
        final SecureRandom random = new SecureRandom();

        long n = random.nextLong();
        if (n == Long.MIN_VALUE) {
            n = 0;      // corner case
        } else {
            n = Math.abs(n);
        }

        return Long.toString(n);

    }

    static String getFileExtension(String name) {
        return name.substring(name.lastIndexOf("."));
    }

    static String getResourcePath(String path){
        String absoluteName = Utils.class.getResource(path).getPath();
        return absoluteName.replaceFirst("/", "");
    }

}
