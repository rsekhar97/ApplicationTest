package com.example.applicationtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.applicationtest.Adaptor.ArticlesAdaptor;
import com.example.applicationtest.model.Articles;

import org.json.JSONArray;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ArticlesListView extends AppCompatActivity implements ArticlesAdaptor.ArticlesAdaptorListener {

    ImageView iv_back;
    TextView tv_title;
    ProgressDialog progressBar;
    RecyclerView rv_articles_view;
    RequestQueue requestQueue;
    ArticlesAdaptor articlesAdaptor;
    List<Articles> articlesList = new ArrayList<>();
    Intent intent;
    String selectedView="",selectedDay="",search="",BaseURL="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_list_view);
        requestQueue = Volley.newRequestQueue(this);

        iv_back= findViewById(R.id.iv_back);
        tv_title= findViewById(R.id.tv_title);
        rv_articles_view = findViewById(R.id.rv_articles_view);

        intent = getIntent();
        selectedView = intent.getStringExtra("ArticlesSearch");
        selectedDay = intent.getStringExtra("SelectedDay");
        search = intent.getStringExtra("Search");

        progressBar = new ProgressDialog(this);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setTitle("Please Wait..");
        progressBar.setMessage("Loading........");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ArticlesListView.this, MainActivity.class));
                finish();
            }
        });
        if(selectedView.equals("MostViewed")){

            tv_title.setText("Most Viewed"+" "+selectedDay+"days");
            BaseURL ="https://api.nytimes.com/svc/mostpopular/v2/viewed/"+selectedDay+".json?api-key=EEyipAJmtTNP17RRGUznUa6ZmHsjNBdw";
            get_Viewed();

        }else if(selectedView.equals("MostShared")){

            tv_title.setText("Most Shared"+" "+selectedDay+"days");
            BaseURL ="https://api.nytimes.com/svc/mostpopular/v2/shared/"+selectedDay+"/facebook.json?api-key=EEyipAJmtTNP17RRGUznUa6ZmHsjNBdw";
            get_Viewed();

        }else if(selectedView.equals("MostEmailed")){

            tv_title.setText("Most Emailed"+" "+selectedDay+"days");
            BaseURL ="https://api.nytimes.com/svc/mostpopular/v2/emailed/"+selectedDay+".json?api-key=EEyipAJmtTNP17RRGUznUa6ZmHsjNBdw";
            get_Viewed();

        }else if(selectedView.equals("SearchArticles")){

            tv_title.setText("Search Articles");
            BaseURL ="https://api.nytimes.com/svc/search/v2/articlesearch.json?q="+search+"&api-key=EEyipAJmtTNP17RRGUznUa6ZmHsjNBdw";
            get_search();

        }

        articlesAdaptor = new ArticlesAdaptor(ArticlesListView.this,articlesList,this);
        articlesAdaptor.notifyDataSetChanged();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_articles_view.setLayoutManager(mLayoutManager);
        rv_articles_view.setItemAnimator(new DefaultItemAnimator());
        rv_articles_view.addItemDecoration(new MyDividerItemDecoration(ArticlesListView.this, DividerItemDecoration.VERTICAL, 3));
        rv_articles_view.setAdapter(articlesAdaptor);




    }

    public void  get_Viewed(){

        progressBar.show();
        StringRequest stringRequest =new StringRequest(Request.Method.GET, BaseURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {


                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    if (status.equals("OK")) {
                        articlesList.clear();
                        JSONArray array_sc = obj.getJSONArray("results");
                        if (array_sc.length() > 0) {
                            for (int i = 0; i < array_sc.length(); i++) {

                                JSONObject sc_Object = array_sc.getJSONObject(i);
                                Articles articles = new Articles();

                                articles.setTitle(sc_Object.getString("title"));
                                articles.setPublished_date(sc_Object.getString("published_date"));
                                articles.setUrl(sc_Object.getString("url"));

                                JSONArray array_sc2 = sc_Object.getJSONArray("media");
                                for (int j = 0; j < array_sc2.length(); j++) {
                                    JSONObject sc_Object2 = array_sc2.getJSONObject(j);
                                    JSONArray array_sc3 = sc_Object2.getJSONArray("media-metadata");
                                    for (int k = 0; k < array_sc3.length(); k++) {
                                        JSONObject sc_Object3 = array_sc3.getJSONObject(k);
                                        String name = sc_Object3.getString("format");
                                        if(name.equals("mediumThreeByTwo440")){
                                            articles.setImage_url(sc_Object3.getString("url"));
                                        }
                                    }
                                }

                                articlesList.add(articles);

                            }
                        }
                    }


                    articlesAdaptor.notifyDataSetChanged();
                    progressBar.dismiss();

                }catch (Exception e){
                    e.printStackTrace();
                    progressBar.dismiss();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                progressBar.dismiss();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public void  get_search(){

        progressBar.show();
        StringRequest stringRequest =new StringRequest(Request.Method.GET, BaseURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {


                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    if (status.equals("OK")) {

                        articlesList.clear();

                        JSONObject obj2 = obj.getJSONObject("response");
                        if (obj2.length() > 0) {
                            for (int i = 0; i < obj2.length(); i++) {
                                JSONArray array_sc = obj2.getJSONArray("docs");

                                for (int j = 0; j < array_sc.length(); j++) {
                                    JSONObject array_sc2 = array_sc.getJSONObject(j);
                                    Articles articles = new Articles();

                                    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
                                    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                    String pubdate = array_sc2.getString("pub_date");

                                    Date date = input.parse(pubdate);
                                    String OrgDate = output.format(date);


                                    articles.setPublished_date(OrgDate);
                                    articles.setUrl(array_sc2.getString("web_url"));

                                    JSONObject obj3 = array_sc2.getJSONObject("headline");
                                   /* String h = obj3.getString("main");
                                    Log.d("",h);*/
                                    articles.setTitle(obj3.getString("main"));
                                    articles.setImage_url("");
                                    articlesList.add(articles);

                                }


                            }

                        }
                    }


                    articlesAdaptor.notifyDataSetChanged();
                    progressBar.dismiss();

                }catch (Exception e){
                    e.printStackTrace();
                    progressBar.dismiss();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                progressBar.dismiss();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


    @Override
    public void onSeleted(Articles articles) {

        String URL = articles.getUrl();

        AlertDialog.Builder builder = new AlertDialog.Builder(ArticlesListView.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.web_view, null);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        WebView webview = dialogView.findViewById(R.id.webview);
        ImageView iv_close = dialogView.findViewById(R.id.iv_close);
        webview.setWebViewClient(new MyBrowser());

        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.loadUrl(URL);


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.show();





    }


    private class MyBrowser extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
           // progressBar.show();
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
          //  progressBar.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ArticlesListView.this, MainActivity.class));
        finish();
    }
}