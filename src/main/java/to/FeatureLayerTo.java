package to;

import model.FeatureLayer;
import util.Util;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FeatureLayerTo {
    private String path;
    private String alias;
    private List<String> fileNames;

    public FeatureLayerTo(FeatureLayer layer) {
        this.path = layer.getPath();
        this.alias = layer.getAlias();

        File[] files = Util.getFiles(Util.getResourcePath("images/person/"+layer.getPath()));

        fileNames = Arrays.stream(files)
                .map(File::getName)
                .collect(Collectors.toList());

        if(layer.getAvailability()<100){
            fileNames.add("blank.png");
        }
    }

    public String getPath() {
        return path;
    }

    public String getAlias() {
        return alias;
    }

    public List<String> getFileNames() {
        return fileNames;
    }
}
