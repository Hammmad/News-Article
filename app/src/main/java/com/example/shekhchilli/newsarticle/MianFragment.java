package com.example.shekhchilli.newsarticle;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class MianFragment extends Fragment {

    CallBackMainFragment MyContext;


    private String title;

    public String getTitle() {
        return title;
    }

    View rootview;
    private static final String DEBUG_TAG = "DebuGGing";
    public TextView message;
    private static String findurl = "https://ajax.googleapis.com/ajax/services/feed/find?v=1.0&q=";
    private ListView listView;
    String keyword;
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    public MianFragment() {
        // Required empty public constructor
    }

    public interface CallBackMainFragment {
        public void displayNewsFragment();

        public String downloadUrl(String url);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview =  inflater.inflate(R.layout.fragment_mian, container, false);

        SearchClickListener();
        itemClickListener();
        return rootview;
    }

    public void SearchClickListener() {
        Button btn = (Button) rootview.findViewById(R.id.Search);
        final EditText search = (EditText) rootview.findViewById(R.id.search_editText);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                keyword = search.getText().toString();
                keyword = keyword.replaceAll(" ", "%20");
                connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {

                    new FindApi().execute(findurl+keyword);

                } else {
                    Toast.makeText(getActivity(), "Internet is not Connected", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public void itemClickListener(){
        listView = (ListView) rootview.findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                connMgr = (ConnectivityManager) getActivity().getSystemService(getContext().CONNECTIVITY_SERVICE);
                networkInfo = connMgr.getActiveNetworkInfo();
                if(networkInfo!=null && networkInfo.isConnected()){
                    // show news of the selected item



//                    new LoadApi().execute(loadurl);
                    MyContext.displayNewsFragment();
//                    MyContext.message(title);

                    Log.e(DEBUG_TAG,"Clicked");
                }else{
                    Toast.makeText(getActivity(),"Bad Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }





    public class FindApi extends AsyncTask<String, Void, String> {


        ProgressDialog dialogue;

        @Override
        protected void onPreExecute() {
            dialogue = new ProgressDialog(getContext());
            dialogue.setTitle("Loading ...");
            dialogue.setMessage("just a moment");
            dialogue.show();


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return MyContext.downloadUrl(findurl+keyword);
        }

//        public String downloadUrl(String Url) {
//            String result = null;
//            try {
//                URL url = new URL(Url);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(10000);
//                conn.setConnectTimeout(15000);
//                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//
//                conn.connect();
//                int response = conn.getResponseCode();
//                InputStream stream = conn.getInputStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"), 8);
//                StringBuilder stringBuilder = new StringBuilder();
//
//                String line = null;
//                while ((line = reader.readLine()) != null) {
//                    stringBuilder.append(line).append("\n");
//                }
//                stream.close();
//                 result = stringBuilder.toString();
//
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return result;
//        }

        @Override
        protected void onPostExecute(String s) {

//            String s = new String(FromStream);

            dialogue.dismiss();
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


                ArrayAdapter<Spanned> adapter = new ArrayAdapter<Spanned>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
                listView = (ListView) rootview.findViewById(R.id.list_view);
                listView.setAdapter(adapter);
//                message.setText("code completed");

            } catch (JSONException e) {
                Log.e(DEBUG_TAG, "JSON EXCEPTION ::" + e.getMessage());
                e.getLocalizedMessage();
            }


        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MyContext = (CallBackMainFragment) context;
    }
}
