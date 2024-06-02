package Service;

import Model.Contributor;
import Model.Repository;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static Separator.GetLinkByParamAndValue.getLinkFromLinkHeaderByParamAndValue;

public class ApiService {

    private static final String GITHUB_API_URL = "https://api.github.com/";

    public static List<Repository> getMostForkedRepositories(HttpClient httpClient, String orgName, int numRepos) throws IOException {
        String url = GITHUB_API_URL + "orgs/" + orgName + "/repos?type=public&per_page=100";
        List<Repository> repositories = new ArrayList<>();

        while (url != null) {
            System.out.println("getMostForkedRepositories: " + url);
            HttpGet request = new HttpGet(url);
            request.setHeader("Accept", "application/vnd.github.v3+json");
            HttpResponse httpResponse = httpClient.execute(request);
            int respCode = httpResponse.getStatusLine().getStatusCode();
            if (respCode != 200) {
                String responseString = EntityUtils.toString(httpResponse.getEntity());
                System.out.println("Http Error:" + respCode + " " + responseString);
                break;
            }
            Header[] links = httpResponse.getHeaders("link");
            if (links != null && links.length > 0) {
                url = getLinkFromLinkHeaderByParamAndValue(links[0].getValue(), "rel", "\"next\"");
            } else {
                url = null;
            }

            String responseJson = EntityUtils.toString(httpResponse.getEntity());
            JSONArray reposArray = new JSONArray(responseJson);
            for (int i = 0; i < reposArray.length(); i++) {
                JSONObject repoJson = reposArray.getJSONObject(i);
                String name = repoJson.getString("name");
                int forks = repoJson.getInt("forks_count");
                String html_url = repoJson.getString("html_url");
                repositories.add(new Repository(name, forks, html_url));
            }
        }

        repositories.sort(new Comparator<Repository>() {
            @Override
            public int compare(Repository s1, Repository s2) {
                return Integer.compare(s2.getForks(), s1.getForks());
            }
        });

        return repositories.size() > numRepos ? repositories.subList(0, numRepos) : repositories;
    }

    public static List<Contributor> getTopContributors(HttpClient httpClient, String orgName, String repoName, int numContributors) throws IOException {
        String url = GITHUB_API_URL + "repos/" + orgName + "/" + repoName + "/contributors?per_page=100";
        List<Contributor> contributors = new ArrayList<>();

        while (url != null) {
            System.out.println("getTopContributors: " + url);
            HttpGet request = new HttpGet(url);
            request.setHeader("Accept", "application/vnd.github.v3+json");
            HttpResponse httpResponse = httpClient.execute(request);
            int respCode = httpResponse.getStatusLine().getStatusCode();
            if (respCode != 200) {
                System.out.println("Http Error:" + respCode);
                break;
            }
            Header[] links = httpResponse.getHeaders("link");
            if (links != null && links.length > 0) {
                url = getLinkFromLinkHeaderByParamAndValue(links[0].getValue(), "rel", "\"next\"");
            } else {
                url = null;
            }

            String responseJson = EntityUtils.toString(httpResponse.getEntity());

            JSONArray contributorsArray = new JSONArray(responseJson);

            for (int i = 0; i < contributorsArray.length(); i++) {
                JSONObject contributorJson = contributorsArray.getJSONObject(i);
                String username = contributorJson.getString("login");
                int contributions = contributorJson.getInt("contributions");
                contributors.add(new Contributor(username, contributions));
            }
        }

        contributors.sort(new Comparator<Contributor>() {
            @Override
            public int compare(Contributor s1, Contributor s2) {
                return Integer.compare(s2.getContributions(), s1.getContributions()); // desc
            }
        });

        List<Contributor> topContributors = contributors.size() > numContributors ? contributors.subList(0, numContributors) : contributors;

        for (Contributor contrib : topContributors) {
            contrib.setFollowers(getUserFollowers(httpClient, contrib.getUsername()));
        }

        return topContributors;
    }

    public static int getUserFollowers(HttpClient httpClient, String username) throws IOException {
        String url = GITHUB_API_URL + "users/" + username;
        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "application/vnd.github.v3+json");

        System.out.println("getUserFollowers: " + url);
        HttpResponse httpResponse = httpClient.execute(request);
        int respCode = httpResponse.getStatusLine().getStatusCode();
        if (respCode != 200) {
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            System.out.println("Http Error:" + respCode + " " + responseString);
            return -1;
        }

        String responseJson = EntityUtils.toString(httpResponse.getEntity());

        JSONObject userJson = new JSONObject(responseJson);
        return userJson.getInt("followers");
    }
}
