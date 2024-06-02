import Model.Contributor;
import Model.Repository;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static FileWriter.WriteToCSV.writeContributorsToCSV;
import static FileWriter.WriteToCSV.writeRepositoriesToCSV;
import static Service.ApiService.getMostForkedRepositories;
import static Service.ApiService.getTopContributors;

public class MyRestApiApp {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java MyRestApiApp <organization_name> <num_repos> <num_contributors>");
            return;
        }

        String orgName = args[0];
        int numRepos = Integer.parseInt(args[1]);
        int numContributors = Integer.parseInt(args[2]);

        HttpClient httpClient = HttpClients.createDefault();

        try {
            List<Repository> repositories = getMostForkedRepositories(httpClient, orgName, numRepos);
            writeRepositoriesToCSV(orgName, repositories);

            for (Repository repo : repositories) {
                List<Contributor> contributors = getTopContributors(httpClient, orgName, repo.getName(), numContributors);
                writeContributorsToCSV(orgName, repo.getName(), contributors);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
