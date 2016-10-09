package com.example.shekhchilli.newsarticle;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment{

    private static final String DEBUG_TAG = "DebuGGing";
    MianFragment.CallBackMainFragment MyContext;
    View rootview;
    private static String loadurl = "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://dancemagazine.com/feed";
    String title;
    String Key = "Value requires";




    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview =  inflater.inflate(R.layout.fragment_news, container, false);



        new LoadApi().execute(loadurl);
//        Log.e("DebuGGing", mainFragment.getTitle());



        return rootview;

    }

//    public void updateText(String message){
//        TextView textView = (TextView) rootview.findViewById(R.id.news_textview);
//        textView.setText(message);
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MyContext = (MianFragment.CallBackMainFragment) context;
    }


    @Override
    public void onStart() {
        super.onStart();


    }

    public void updateText() {
        Log.e(DEBUG_TAG,"Loading....");
    }

    public class LoadApi extends AsyncTask<String,Void,String>{

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
            return MyContext.downloadUrl(loadurl);
        }

        @Override
        protected void onPostExecute(String json) {
//            TextView newstextView = (TextView) rootview.findViewById(R.id.news_textview);
            dialogue.dismiss();
            String url = null;
            WebView webView = (WebView) rootview.findViewById(R.id.news_webView);
            WebSettings webSettings = webView.getSettings();
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setJavaScriptEnabled(true);

            Log.e(DEBUG_TAG,json);

            try {
                JSONObject rootObject = new JSONObject(json);

                if(rootObject != null){

                    JSONObject responseData = rootObject.getJSONObject("responseData");

                    JSONObject feed = responseData.getJSONObject("feed");

                    JSONArray entries = feed.getJSONArray("entries");

                    JSONObject item;
                    for(int i =0; i<entries.length(); i++){

                        if(i == 0) {

                             item = entries.getJSONObject(i);
                             url = item.getString("content");

                        }
                        if(i != 0){

                            url += "\n\n";
                            item = entries.getJSONObject(i);

                            url += item.getString("content");
                        }

                    }

                    webView.loadDataWithBaseURL(null, url , "text/html" , "UTF-8",null);

                }
            } catch (JSONException e) {
                Log.e(DEBUG_TAG,"JSON ExcePtion" + e.getMessage());
            }

        }
    }



}
