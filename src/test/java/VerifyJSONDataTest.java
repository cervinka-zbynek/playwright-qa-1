import org.testng.annotations.Test;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertTrue;

public class VerifyJSONDataTest extends VerifyJSONDataParent {
    @Test
    public void verifyCountOfTeamsTest() {
        int expectedTeamsCount = 32;
        int returnedTeamsCount = teamsOnlyJSON.size();

        assertTrue(returnedTeamsCount == expectedTeamsCount, "The response did not returned correct count of teams (expected " + expectedTeamsCount + ", returned " + returnedTeamsCount + ")");
    }

    @Test
    public void verifyOldestTeamTest() {
        String expectedOldestTeam = "Montreal Canadiens";
        assertTrue(!teamsOnlyJSON.isEmpty(), "The JSON array contain no teams.");
        String oldestTeamFromRequest = findOldestTeam(teamsOnlyJSON);

        assertTrue(oldestTeamFromRequest.equals(expectedOldestTeam), "The oldest team is not " + expectedOldestTeam + " as expected, the oldest team is " + oldestTeamFromRequest +".");
    }

    @Test
    public void verifyCityWithMoreThanOneTeamTest() {
        List<String> cities = getCities(teamsOnlyJSON);
        List<String> citiesWithMoreThanOneTeam = getCitiesWithMoreThanOneTeam(cities);
        assertTrue(!citiesWithMoreThanOneTeam.isEmpty(), "There is no city with more than one team.");

        List<String> teamNames = new ArrayList<>();
        teamNames.add("New York Islanders");
        teamNames.add("New York Rangers");

        boolean teamNamesVerified = verifyTeamsInOneCity(teamsOnlyJSON, citiesWithMoreThanOneTeam.get(0), teamNames);
        assertTrue(teamNamesVerified, "Names of the teams in the " + citiesWithMoreThanOneTeam.get(0) + " city does not match the expected values.");
    }

    @Test
    public void verifyTeamsInMetropolitanDivisionTest() {
        String divisionName = "Metropolitan";
        List<String> teamsInMetropolitanDivisionNames = new ArrayList<>();
        teamsInMetropolitanDivisionNames.add("Carolina Hurricanes");
        teamsInMetropolitanDivisionNames.add("Columbus Blue Jackets");
        teamsInMetropolitanDivisionNames.add("New Jersey Devils");
        teamsInMetropolitanDivisionNames.add("New York Islanders");
        teamsInMetropolitanDivisionNames.add("New York Rangers");
        teamsInMetropolitanDivisionNames.add("Philadelphia Flyers");
        teamsInMetropolitanDivisionNames.add("Pittsburgh Penguins");
        teamsInMetropolitanDivisionNames.add("Washington Capitals");

        boolean teamsInMetropolitanVerified = verifyTeamsInOneDivision(teamsOnlyJSON, divisionName, teamsInMetropolitanDivisionNames);
        assertTrue(teamsInMetropolitanVerified, "Teams in the " + divisionName + " division does not match the expected values.");
    }

    private static String getCity(JSONArray teamsOnlyJSON, int indexOfTeam) {
        return (String) getTeamAttribute(teamsOnlyJSON, indexOfTeam, 5);
    }

    private static String getDivisionName(JSONArray teamsOnlyJSON, int indexOfTeam) {
        String teamString = teamsOnlyJSON.get(indexOfTeam).toString();

        try {
            JSONObject team = (JSONObject) parser.parse(teamString);
            Collection values = team.values();
            Object[] valuesArray = values.toArray();

            JSONObject divisionJSONObject = (JSONObject) parser.parse(valuesArray[0].toString());
            return (String) divisionJSONObject.get("name");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> getCities(JSONArray teamsOnlyJSON) {
        List<String> citiesList = new ArrayList<>();

        for (int i = 0; i < teamsOnlyJSON.size(); i++) {
            citiesList.add(getCity(teamsOnlyJSON, i));
        }

        return  citiesList;
    }

    private static List<String> getCitiesWithMoreThanOneTeam(List<String> citiesList) {
        Set<String> uniqueCities = new HashSet<>();
        List<String> duplicateCities = new ArrayList<>();

        for (String city : citiesList) {
            if (!uniqueCities.add(city)) {
                duplicateCities.add(city);
            }
        }
        return duplicateCities;
    }

    private static boolean verifyTeamsInOneCity(JSONArray teamsOnlyJSON, String targetCityName, List<String> targetTeamNames) {
        for (int i = 0; i < teamsOnlyJSON.size(); i++) {
            String cityName = getCity(teamsOnlyJSON, i);
            if (cityName.equals(targetCityName)) {
                String teamName = getTeamName(teamsOnlyJSON, i);
                Iterator<String> iterator = targetTeamNames.iterator();
                boolean teamVerified = false;

                while (iterator.hasNext()) {
                    String targetTeamName = iterator.next();
                    if (teamName.equals(targetTeamName)) {
                        iterator.remove();
                        teamVerified = true;
                        break;
                    }
                }

                if (!teamVerified) {
                    return false;
                }
            }
        }

        return targetTeamNames.isEmpty();
    }

    private static boolean verifyTeamsInOneDivision(JSONArray teamsOnlyJSON, String targetDivisionName, List<String> targetTeamNames) {
        for (int i = 0; i < teamsOnlyJSON.size(); i++) {
            String divisionName = getDivisionName(teamsOnlyJSON, i);
            if (divisionName.equals(targetDivisionName)) {
                String teamName = getTeamName(teamsOnlyJSON, i);
                Iterator<String> iterator = targetTeamNames.iterator();
                boolean teamVerified = false;

                while (iterator.hasNext()) {
                    String targetTeamName = iterator.next();
                    if (teamName.equals(targetTeamName)) {
                        iterator.remove();
                        teamVerified = true;
                        break;
                    }
                }

                if (!teamVerified) {
                    return false;
                }
            }
        }

        return targetTeamNames.isEmpty();
    }
}
