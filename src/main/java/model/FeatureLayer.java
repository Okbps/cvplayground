package model;

public class FeatureLayer{
    private String path;
    private String alias;
    private int availability;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "FeatureLayer{" +
                "path='" + path + '\'' +
                ", alias='" + alias + '\'' +
                ", availability=" + availability +
                '}';
    }
}
