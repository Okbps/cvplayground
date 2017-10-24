package to;

import model.FeatureLayer;
import util.Util;

import java.io.File;
import java.util.Arrays;

public class FeatureLayerTo {
    private String path;
    private String alias;
    private String[]fileNames;

    public FeatureLayerTo(FeatureLayer layer) {
        this.path = layer.getPath();
        this.alias = layer.getAlias();

        File[] files = Util.getFiles(Util.getResourcePath("images/person/"+layer.getPath()));

        fileNames = Arrays.stream(files)
                .map(File::getName)
                .toArray(String[]::new);
    }

    public String getPath() {
        return path;
    }

    public String getAlias() {
        return alias;
    }

    public String[] getFileNames() {
        return fileNames;
    }
}
