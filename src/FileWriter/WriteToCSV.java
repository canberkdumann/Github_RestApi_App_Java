package FileWriter;

import Model.Contributor;
import Model.Repository;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class WriteToCSV {

    public static void writeRepositoriesToCSV(String orgName, List<Repository> repositories) throws IOException {
        try (FileWriter writer = new FileWriter(orgName + "_repos.csv")) {
            writer.write("repo,forks,url\n");
            for (Repository repo : repositories) {
                writer.write(repo.toCSVString() + "\n");
            }
        }
    }

    public static void writeContributorsToCSV(String orgName, String repoName, List<Contributor> contributors) throws IOException {
        try (FileWriter writer = new FileWriter(orgName + "_" + repoName + "_users.csv")) {
            writer.write("repo,username,contributions,followers\n");
            for (Contributor contributor : contributors) {
                writer.write(orgName + "," + contributor.toCSVString() + "\n");
            }
        }
    }
}
