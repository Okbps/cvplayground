package model;

import java.util.LinkedHashMap;

public class Person {
    private LinkedHashMap<String, String> selectedFeatures = new LinkedHashMap<>();
    private String fileName;

    public LinkedHashMap<String, String> getSelectedFeatures() {
        return selectedFeatures;
    }

    public void setSelectedFeatures(LinkedHashMap<String, String> selectedFeatures) {
        this.selectedFeatures = selectedFeatures;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
