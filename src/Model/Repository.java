package Model;

public class Repository {
    private final String name;
    private final int forks;
    private final String url;

    public Repository(String name, int forks, String url) {
        this.name = name;
        this.forks = forks;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getForks() {
        return forks;
    }

    public String toCSVString() {
        return name + "," + forks + "," + url;
    }

}
