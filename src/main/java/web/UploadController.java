package web;

import model.FeatureLayer;
import model.PersonFactory;
import service.UploadService;
import to.FeatureLayerTo;
import model.Person;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.LinkedHashMap;

@MultipartConfig(fileSizeThreshold = 1024*1024*2,
maxFileSize = 1024*1024*10,
maxRequestSize = 1024*1024*50)
public class UploadController extends HttpServlet{
    private UploadService service;
    private String lastSampleFileName;
    private String personFileName;

    @Override
    public void init() throws ServletException {
        service = new UploadService();
        lastSampleFileName = "";
        personFileName = "";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String imgName = req.getParameter("img");
        String jsonName = req.getParameter("json");

        if(imgName!=null){
            doGetImage(resp, imgName);
        }

        if(jsonName!=null){
            doGetJson(resp, jsonName);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contentType = req.getHeader("Content-type");

        if(contentType.contains("multipart/form-data")){
            Part sampleFile = req.getPart("sampleFile");

            if(sampleFile!=null){
                doPostSampleFile(req, resp, sampleFile);
            }
        }else if(contentType.contains("application/json")) {
            if(req.getParameterMap().containsKey("updateSet")){
                doPostUpdateTrainingSet(req, resp);
            }else if(req.getParameterMap().containsKey("updatePerson"))
                doPostUpdatePerson(req, resp);
        }
    }


    // Request handlers

    private void doPostSampleFile(HttpServletRequest req, HttpServletResponse resp, Part sampleFile) throws IOException {
        String submittedSource = sampleFile.getSubmittedFileName();

        if(!submittedSource.equals(lastSampleFileName)) {
            InputStream is = sampleFile.getInputStream();
            Util.saveFile(is, Util.getResourcePath("images/uploaded/" + submittedSource));
            is.close();

            personFileName = Util.getRandomName("images/person/generated/", "jpg");;
            lastSampleFileName = submittedSource;
        }

        Person person = PersonFactory.createRandom();
        person.setFileName(personFileName);
        service.generatePersonImage(person);

        OutputStream out = resp.getOutputStream();

        Util.writeJson(out, person);

        out.flush();
        out.close();

        resp.setContentType("application/json");
    }

    private void doPostUpdatePerson(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        InputStream in = req.getInputStream();
        Person person = Util.readJson(in, Person.class);
        in.close();

        service.generatePersonImage(person);
    }

    private void doPostUpdateTrainingSet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        InputStream in = req.getInputStream();
        LinkedHashMap selectedFeatures = new LinkedHashMap<>();
        selectedFeatures = Util.readJson(in, selectedFeatures.getClass());
        in.close();

        service.updateTrainingSet(lastSampleFileName, selectedFeatures);
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
        FeatureLayerTo[]layersTo = new FeatureLayerTo[layers.length];

        for (int i = 0; i < layers.length; i++) {
            layersTo[i] = new FeatureLayerTo(layers[i]);
        }

        OutputStream out = resp.getOutputStream();

        Util.writeJson(out, layersTo);

        out.flush();
        out.close();

        resp.setContentType("application/json");
    }
}
