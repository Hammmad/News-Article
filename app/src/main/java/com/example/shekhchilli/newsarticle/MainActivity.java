package com.example.shekhchilli.newsarticle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MianFragment.CallBackMainFragment {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.container,new MianFragment()).commit();

    }

    @Override
    public void displayNewsFragment() {

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, new NewsFragment()).commit();
    }



    @Override
    public String downloadUrl(String Url) {
        String result = null;
        try {
            URL url = new URL(Url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            int response = conn.getResponseCode();
            InputStream stream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"), 8);
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
