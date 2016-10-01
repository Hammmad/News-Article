package com.example.shekhchilli.newsarticle;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "DebuGGing";

    public TextView message;
    private static String apiurl = "https://ajax.googleapis.com/ajax/services/feed/find?v=1.0&q=dance";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchClickListener();
        itemClickListener();
    }

    public void SearchClickListener() {
        Button btn = (Button) findViewById(R.id.Search);
        message = (TextView) findViewById(R.id.message);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    message.setText("Connected");
                    new API().execute(apiurl);

                } else {
                    message.setText("Internet is not Connected");
                }
            }
        });

    }

    public void itemClickListener(){
        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container,new BlankFragment()).commit();
                Log.e(DEBUG_TAG,"Item clicked MMMMAAAAN");
            }
        });
    }

    private class API extends AsyncTask<String, Void, String> {

        String result;

        @Override
        protected String doInBackground(String... params) {
            //String Url = "https://www.googleapis.com/books/v1/volumes?q=";
            InputStream stream = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(apiurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                int response = conn.getResponseCode();
                stream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream, "utf-8"), 8);
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                stream.close();
                result = stringBuilder.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

//            String s = new String(FromStream);

            Log.e(DEBUG_TAG, s);
            ArrayList<Spanned> arrayList = new ArrayList<>();
            try {
                JSONObject rootObject = new JSONObject(s);
                Log.e(DEBUG_TAG, "root object created");
                if (rootObject != null) {

                    JSONObject feed = rootObject.getJSONObject("responseData");
                    JSONArray entries = feed.getJSONArray("entries");

                    for (int i = 0; i < entries.length(); i++) {

                        JSONObject item = entries.getJSONObject(i);

                        String title = item.getString("title");

                        arrayList.add(Html.fromHtml(title));
                    }
                    Log.e(DEBUG_TAG, String.valueOf(entries.length()));
                }


                ArrayAdapter<Spanned> adapter = new ArrayAdapter<Spanned>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
                listView = (ListView) findViewById(R.id.list_view);
                listView.setAdapter(adapter);
//                message.setText("code completed");

            } catch (JSONException e) {
                Log.e(DEBUG_TAG, "JSON EXCEPTION ::" + e.getMessage());
                e.getLocalizedMessage();
            }


        }
    }

//    private class Task extends AsyncTask<String, Void, String> {
//
//        private static final String DEBUG_TAG = "DebuGGing";
//        String result;
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            InputStream inputStream;
//            BufferedReader bReader;
//
//            try {
//                URL url = new URL(apiurl);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(1000);
//                conn.setConnectTimeout(15000);
//                conn.setRequestMethod("GET");
//                Log.e(DEBUG_TAG,"setRequestMethod");
//
//                conn.connect();
//                int response = conn.getResponseCode();
//                Log.e(DEBUG_TAG,"response code is "+response);
//
//                inputStream = conn.getInputStream();
//                bReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"),8);
//
//                StringBuilder stringBuilder = new StringBuilder();
//                String line = null;
//
//
//                while((line = bReader.readLine()) != null){
//                    stringBuilder.append(line +"\n");
//                }
//
//                inputStream.close();
//                result = stringBuilder.toString();
//
//
//            } catch (MalformedURLException e) {
//                Log.e(DEBUG_TAG,"URLException" + e.getMessage());
//            } catch (IOException e) {
//                Log.e(DEBUG_TAG,"IO Exception" + e.getMessage());
//            }
//
//
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String json) {
//
//            Log.e(DEBUG_TAG,"OnPostExecute::::::::" + json);
//
//            ArrayList<String> list = new ArrayList<String>();
//
//            try {
//                JSONObject rootObject = new JSONObject(json);
//                JSONArray jArray = rootObject.getJSONArray("entries");
//
//                for(int i = 0; i<jArray.length() ; i++){
//                    JSONObject entries = jArray.getJSONObject(i);
//
//                    String title = entries.getString("title");
//                    Log.e(DEBUG_TAG,"Title =======" + title);
//                    list.add(title);
//                }
//
//
//                ListView listView = (ListView) findViewById(R.id.list_view);
//                ArrayAdapter<String> stringArrayAdapter =
//                        new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,list);
//                listView.setAdapter(stringArrayAdapter);
//
//
//
//            } catch (JSONException e) {
//                Log.e(DEBUG_TAG,"JSON EXCEPTION" + e.getMessage());
//            }
//
//
//        }
//
//    }
}
