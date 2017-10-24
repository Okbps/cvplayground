package to;

import java.util.HashMap;
import java.util.Map;

public class PersonTo {
    private Map<String, String> selectedFeatures = new HashMap<>();
    private String fileName;

    public Map<String, String> getSelectedFeatures() {
        return selectedFeatures;
    }

    public void setSelectedFeatures(Map<String, String> selectedFeatures) {
        this.selectedFeatures = selectedFeatures;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
