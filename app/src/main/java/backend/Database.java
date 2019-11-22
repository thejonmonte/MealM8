package backend;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.sql.*;

public class Database {
    private static final String app_id = "cf5ba5f1";
    private static final String app_key = "228bb270794c3dcc69d293f7f0c1ea33";
    private static String jdbc_url = "jdbc:mysql://google/mealm8?cloudSqlInstance=mealm8:us-central1:mealm8"
            + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user=root" +
            "&password=grujic";

    public static void addToMealbox(int profileId, String recipeURI) {

        // connect to database
        Connection conn = null;
        PreparedStatement ps = null;

        // Check if user is in base
        String insertString = "INSERT INTO Mealbox(profileId, recipeId) " +
                "VALUES ?, " +
                "(SELECT recipeId FROM Recipe WHERE recipeURI = ?);";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            //conn = DriverManager.getConnection(url);

            ps = conn.prepareStatement(insertString);

            ps.setInt(1, profileId);
            ps.setString(2, recipeURI);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successful insert!");
            } else {
                System.out.println("Error inserting");
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (ps!= null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                System.out.println("ERROR @ LOGIN FUNCTION");
                System.out.println(sqle.getMessage());
            }
        }
    }

    public static void removeFromoMealbox(int profileId, String recipeURI) {

        // connect to database
        Connection conn = null;
        PreparedStatement ps = null;

        // Check if user is in base
        String deleteString = "DELETE FROM Mealbox " +
                "WHERE profileId = ? AND recipeId = " +
                "(SELECT recipeId FROM Recipe WHERE recipeURI = ?);";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(jdbc_url);

            ps = conn.prepareStatement(deleteString);

            ps.setInt(1, profileId);
            ps.setString(2, recipeURI);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successful delete!");
            } else {
                System.out.println("Error deleting");
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (ps!= null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                System.out.println("ERROR @ LOGIN FUNCTION");
                System.out.println(sqle.getMessage());
            }
        }
    }

    public static ArrayList<JSONObject> getMostFavorited() {
        // connect to database
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;


        // Check if user is in base
        String favoriteString = "SELECT * FROM Recipe ORDER BY timesSaved DESC";
        ArrayList<JSONObject> favorites = new ArrayList<JSONObject>();
        //String url2 = System.getProperty("ae-cloudsql.cloudsql-database-url");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(jdbc_url);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(favoriteString);
            System.out.println("Hello!");
            while (rs.next()) {
                System.out.println(rs.getString("recipeURI"));
                // Make GET request to the Edamam Recipe API to get JSON String
                HttpURLConnection con = null;
                String recipesString = null;
                try {
                    URL url = new URL("https://api.edamam.com/search?r=" +
                            URLEncoder.encode(rs.getString("recipeURI"), "UTF-8") +
                            "&app_id=" + app_id + "&app_key=" + app_key);
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
                            //System.out.println(recipesString);
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
                JSONArray recipesDataArray = null;
                try {
                    recipesDataArray = (JSONArray) parser.parse(recipesString);
                    JSONObject recipes = (JSONObject) recipesDataArray.get(0);
                    recipes.put("TimesSaved", rs.getInt("timesSaved"));
                    favorites.add(recipes);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs!= null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                System.out.println("ERROR @ LOGIN FUNCTION");
                System.out.println(sqle.getMessage());
            }
        }
        return favorites;
    }
}
