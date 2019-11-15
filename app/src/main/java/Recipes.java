import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Recipes {
    private static final String app_id = "cf5ba5f1";
    private static final String app_key = "228bb270794c3dcc69d293f7f0c1ea33";

    public static JSONObject search(ArrayList<String> ingredients) {
        // Build the ingredients string
        StringBuilder ingredientsString = new StringBuilder();
        for (String ingredient : ingredients) {
            ingredientsString.append(ingredient).append("+");
        }

        // Make GET request to the Edamam Recipe API to get JSON String
        HttpURLConnection con = null;
        String recipesString = null;
        try {
            URL url = new URL("https://api.edamam.com/search?q=" +
                    ingredientsString.toString().substring(0, ingredientsString.length()-1) +
                    "&app_id=$" + app_id + "&app_key=$" + app_key);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-length", "0");
            con.setUseCaches(false);
            con.setAllowUserInteraction(false);
            con.connect();
            int status = con.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader bufferedReader = new BufferedReader(new
                            InputStreamReader(con.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = bufferedReader.readLine();
                    while (line != null) {
                        stringBuilder.append(line + "\n");
                        line = bufferedReader.readLine();
                    }
                    bufferedReader.close();
                    recipesString = stringBuilder.toString();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        // Parse JSON String using Simple JSON
        JSONParser parser = new JSONParser();
        JSONObject recipesData = null;
        try {
            recipesData = (JSONObject) parser.parse(recipesString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (recipesData != null) ? (JSONObject) recipesData.get("recipe"): null;
    }
}
