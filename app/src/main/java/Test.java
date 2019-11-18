import org.json.simple.JSONObject;

import java.util.ArrayList;

import backend.Database;
//import java.io.*;
//import java.util.*;
//import com.google.api.gax.paging.Page;
//import com.google.auth.appengine.AppEngineCredentials;
//import com.google.auth.oauth2.ComputeEngineCredentials;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.storage.Bucket;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import com.google.common.collect.Lists;

public class Test {

//    static void authAppEngineStandard() throws IOException {
//        // Explicitly request service account credentials from the app engine standard instance.
//        GoogleCredentials credentials = AppEngineCredentials.getApplicationDefault();
//        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
//
//        System.out.println("Buckets:");
//        Page<Bucket> buckets = storage.list();
//        for (Bucket bucket : buckets.iterateAll()) {
//            System.out.println(bucket.toString());
//        }
//    }

    public static void main(String[] args) {
        ArrayList<JSONObject> results = Database.getMostFavorited();
        for (int i = 0; i < results.size(); i++) {
            System.out.println(results.get(i).get("uri"));
        }
    }
}
