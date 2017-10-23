import java.io.File;
import java.util.Arrays;

public class FeatureLayerDTO {
    private String path;
    private String alias;
    private String[]fileNames;

    public FeatureLayerDTO(FeatureLayer layer) {
        this.path = layer.getPath();
        this.alias = layer.getAlias();

        File[] files = Utils.getFiles(Utils.getResourcePath("images/person/"+layer.getPath()));

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
