package web;

import model.FeatureLayer;
import service.UploadService;
import to.FeatureLayerTo;
import to.PersonTo;
import util.Util;

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
    private String fileName;

    @Override
    public void init() throws ServletException {
        service = new UploadService();
        lastSource = "";
        fileName = "";
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream is = req.getPart("sampleFile").getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Util.copy(is, buffer);
        buffer.flush();
        buffer.close();

        String submittedSource = req.getPart("sampleFile").getSubmittedFileName();

        if(!submittedSource.equals(lastSource)) {
            fileName = Util.getRandomName("images/person/", "jpg");;
            lastSource = submittedSource;
        }

        PersonTo person = service.generateAvatarByConfig((String) fileName);

        OutputStream out = resp.getOutputStream();

        Util.writeJson(out, person);

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
        String fileName = Util.getResourcePath("")+imgName;
        OutputStream out = resp.getOutputStream();

        resp.setContentType(getServletContext().getMimeType(fileName));
        long fileSize = Util.copy(new FileInputStream(fileName), out);
        resp.setContentLength((int) fileSize);

        out.flush();
        out.close();
    }

    private void doGetJson(HttpServletResponse resp, String imgName) throws IOException {
        FeatureLayer[]layers = Util.getFeatureLayers();
        FeatureLayerTo[]dtos = new FeatureLayerTo[layers.length];

        for (int i = 0; i < layers.length; i++) {
            dtos[i] = new FeatureLayerTo(layers[i]);
        }

        OutputStream out = resp.getOutputStream();

        Util.writeJson(out, dtos);

        out.flush();
        out.close();

        resp.setContentType("application/json");
    }
}
