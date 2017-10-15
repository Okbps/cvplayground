import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@MultipartConfig(fileSizeThreshold = 1024*1024*2,
maxFileSize = 1024*1024*10,
maxRequestSize = 1024*1024*50)
public class UploadController extends HttpServlet{
    UploadService service;

    @Override
    public void init() throws ServletException {
        service = new UploadService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream is = req.getPart("sampleFile").getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Utils.copy(is, buffer);
        buffer.flush();
        buffer.close();

        service.setHatGrowthFactor(Double.parseDouble(req.getParameter("hatGrowthFactor")));
        service.setHatOffsetY(Double.parseDouble(req.getParameter("hatOffsetY")));

        String fileName = service.getModifiedImage(buffer.toByteArray());

        OutputStream out = resp.getOutputStream();
        out.write(fileName.getBytes());
        out.flush();
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String img = req.getParameter("img");

        String fileName = Utils.getResourcePath("images")+img;
        OutputStream out = resp.getOutputStream();

        resp.setContentType(getServletContext().getMimeType(fileName));
        long fileSize = Utils.copy(new FileInputStream(fileName), out);
        resp.setContentLength((int) fileSize);

        out.flush();
        out.close();
    }
}
