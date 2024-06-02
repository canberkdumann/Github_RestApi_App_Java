package Model;

public class Contributor {
    private final String username;
    private final int contributions;
    private int followers;

    public Contributor(String username, int contributions) {
        this.username = username;
        this.contributions = contributions;
        this.followers = 0;
    }

    public String getUsername() {
        return username;
    }

    public int getContributions() {
        return contributions;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int value) {
        followers = value;
    }

    public String toCSVString() {
        return username + "," + contributions + "," + followers;
    }
}
