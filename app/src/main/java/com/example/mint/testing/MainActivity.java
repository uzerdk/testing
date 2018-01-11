package com.example.mint.testing;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            //String url = "http://api.androidhive.info/contacts/";
            String url = "http://104.131.35.222:5000/announcements?date=2018-01-10";
            //String url = "http://104.131.35.222:5000/announcements?";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting Announcement and Event date
                    String date = jsonObj.getString("date");

                    // Getting JSON Array node for announcements
                    JSONArray announcements = jsonObj.getJSONArray("announcements");

                    // looping through All announcements
                    for (int i = 0; i < announcements.length(); i++) {
                        JSONObject c = announcements.getJSONObject(i);
                        String title = c.getString("title");
                        String contact = c.getString("contact");
                        String details = c.getString("details");
                        String email = c.getString("email");
                        String phone = c.getString("phone");
                        // Tags node is JSON Array
                        JSONArray tags = c.getJSONArray("tags");
                        // TODO unpack tags into needed format


                        // temp hash map for single announcement
                        HashMap<String, String> announcementMap = new HashMap<>();

                        // adding each child node to HashMap key => value
                        announcementMap.put("title", title);
                        announcementMap.put("contact", contact);
                        announcementMap.put("details", details);
                        announcementMap.put("email", email);
                        announcementMap.put("phone", phone);
                        // TODO add tags here
                        //announcementMap.put("tags", tags);

                        // adding announcement to announcement list
                        // TODO uncomment announcements
                        //contactList.add(announcementMap);
                    }

                    // Getting JSON Array node for events
                    JSONArray events = jsonObj.getJSONArray("events");

                    // looping through All events
                    for (int i = 0; i < events.length(); i++) {
                        JSONObject c = events.getJSONObject(i);
                        String title = c.getString("title");
                        String contact = c.getString("contact");
                        String details = c.getString("details");
                        String email = c.getString("email");
                        String phone = c.getString("phone");
                        String cost = c.getString("cost");
                        String datetime = c.getString("datetime");
                        String location = c.getString("location");
                        // Tags node is JSON Array
                        JSONArray tags = c.getJSONArray("tags");
                        // TODO unpack tags into needed format


                        // temp hash map for single announcement
                        HashMap<String, String> eventMap = new HashMap<>();

                        // adding each child node to HashMap key => value
                        eventMap.put("title", title);
                        eventMap.put("contact", contact);
                        eventMap.put("details", details);
                        eventMap.put("email", email);
                        eventMap.put("phone", phone);
                        eventMap.put("cost", cost);
                        eventMap.put("datetime", datetime);
                        eventMap.put("location", location);
                        // TODO add tags here
                        //eventMap.put("tags", tags);

                        // adding eventMap to eventMap list
                        // TODO process the events into some list
                        //eventList.add(eventMap);
                        contactList.add(eventMap);

                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList,
                    R.layout.list_item, new String[]{ "title","contact"},
                    new int[]{R.id.title, R.id.contact});
            lv.setAdapter(adapter);
        }
    }
}