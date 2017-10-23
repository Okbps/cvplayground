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
    private UploadService service;
    private String lastSource;
    private String[]fileNames;

    @Override
    public void init() throws ServletException {
        service = new UploadService();
        lastSource = "";
        fileNames = new String[2];

        fileNames[0] = "";
        fileNames[1] = "";
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream is = req.getPart("sampleFile").getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Utils.copy(is, buffer);
        buffer.flush();
        buffer.close();

        String submittedSource = req.getPart("sampleFile").getSubmittedFileName();

        if(!submittedSource.equals(lastSource)) {
            fileNames[0] = "";
            fileNames[1] = "";
            lastSource = submittedSource;
        }

        fileNames[1] = service.generateAvatarByConfig(fileNames[1]);

        OutputStream out = resp.getOutputStream();

        Utils.writeJson(out, fileNames);

        out.flush();
        out.close();

        resp.setContentType("application/json");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String imgName = req.getParameter("img");
        String jsonName = req.getParameter("json");

        if(imgName!=null){
            doGetImage(resp, imgName);
        }

        if(jsonName!=null){
            doGetJson(resp, imgName);
        }
    }

    private void doGetImage(HttpServletResponse resp, String imgName) throws IOException {
        String fileName = Utils.getResourcePath("")+imgName;
        OutputStream out = resp.getOutputStream();

        resp.setContentType(getServletContext().getMimeType(fileName));
        long fileSize = Utils.copy(new FileInputStream(fileName), out);
        resp.setContentLength((int) fileSize);

        out.flush();
        out.close();
    }

    private void doGetJson(HttpServletResponse resp, String imgName) throws IOException {
        FeatureLayer[]layers = Utils.getFeatureLayers();
        FeatureLayerDTO[]dtos = new FeatureLayerDTO[layers.length];

        for (int i = 0; i < layers.length; i++) {
            dtos[i] = new FeatureLayerDTO(layers[i]);
        }

        OutputStream out = resp.getOutputStream();

        Utils.writeJson(out, dtos);

        out.flush();
        out.close();

        resp.setContentType("application/json");
    }
}
