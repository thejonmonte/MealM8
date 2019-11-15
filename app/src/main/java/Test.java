import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        ArrayList<JSONObject> results = Database.getMostFavorited();
        for (int i = 0; i < results.size(); i++) {
            System.out.println("Results: " + results.get(i) + "\n\n\n");
        }
    }
}
