import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

public class VerifyJSONDataParent {

    protected static String url = "https://qa-assignment.dev1.whalebone.io/api/teams";
    protected static JSONObject jsonFromRequest;
    protected static JSONArray teamsOnlyJSON;
    protected static JSONParser parser = new JSONParser();

    @BeforeClass
    public void prepareJsonData() {
        try {
            URL url = new URL(VerifyJSONDataParent.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                jsonFromRequest = (JSONObject) parser.parse(response.toString());
                String key = (String) jsonFromRequest.keySet().stream().findFirst().get();

                Object teams = jsonFromRequest.get(key);
                teamsOnlyJSON = (JSONArray) parser.parse(teams.toString());
            } else {
                System.out.println("HTTP error (HTTP response code - " + responseCode + ").");
            }
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String findOldestTeam(JSONArray teamsOnlyJSON) {
        int teamsOnlySize = teamsOnlyJSON.size();
        int oldestTeamIndex = 0;
        long oldestTeamFounded = getYearFounded(teamsOnlyJSON, 0);

        for (int i = 1; i < teamsOnlySize; i++) {
            long yearFounded = getYearFounded(teamsOnlyJSON, i);
            if (oldestTeamFounded > yearFounded) {
                oldestTeamFounded = yearFounded;
                oldestTeamIndex = i;
            }
        }

        String teamString = teamsOnlyJSON.get(oldestTeamIndex).toString();
        JSONObject team = null;
        try {
            team = (JSONObject) parser.parse(teamString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Collection values = team.values();
        Object[] valuesArray = values.toArray();

        return (String) valuesArray[2];
    }

    private static long getYearFounded(JSONArray teamsOnlyJSON, int indexOfTeam) {
        return (long) getTeamAttribute(teamsOnlyJSON, indexOfTeam, 3);
    }

    protected static String getTeamName(JSONArray teamsOnlyJSON, int indexOfTeam) {
        return (String) getTeamAttribute(teamsOnlyJSON, indexOfTeam, 2);
    }

    protected static Object getTeamAttribute(JSONArray teamsOnlyJSON, int indexOfTeam, int indexOfAttribute) {
        String teamString = teamsOnlyJSON.get(indexOfTeam).toString();

        try {
            JSONObject team = (JSONObject) parser.parse(teamString);
            Collection values = team.values();
            Object[] valuesArray = values.toArray();

            return valuesArray[indexOfAttribute];
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
