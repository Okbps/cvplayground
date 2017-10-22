public class FeatureLayer implements Comparable{
    private String path;
    private String alias;
    private int order;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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
                ", order=" + order +
                ", availability=" + availability +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return getOrder() - ((FeatureLayer)o).getOrder();
    }
}
