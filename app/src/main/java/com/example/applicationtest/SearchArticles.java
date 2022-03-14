package com.example.applicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class SearchArticles extends AppCompatActivity {

    ImageView iv_back;
    RequestQueue requestQueue;

    private MultiAutoCompleteTextView SearchText;
    private static final String[] Articles = new String[]{
            "Adventure Sports", " Arts & Leisure", " Arts", " Automobiles", " Blogs", " Books", " Booming", " Business Day", " Business", " Cars", " Circuits", " Classifieds", " Connecticut", " Crosswords & Games", " Culture", " DealBook", " Dining", " Editorial", " Education", " Energy", " Entrepreneurs", " Environment", " Escapes", " Fashion & Style", " Fashion", " Favorites", " Financial", " Flight", " Food", " Foreign", " Generations", " Giving", " Global Home", " Health & Fitness", " Health", " Home & Garden", " Home", " Jobs", " Key", " Letters", " Long Island", " Magazine", " Market Place", " Media", " Men's Health", " Metro", " Metropolitan", " Movies", " Museums", " National", " Nesting", " Obits", " Obituaries", " Obituary", " OpEd", " Opinion", " Outlook", " Personal Investing", " Personal Tech", " Play", " Politics", " Regionals", " Retail", " Retirement", " Science", " Small Business", " Society", " Sports", " Style", " Sunday Business", " Sunday Review", " Sunday Styles", " T Magazine", " T Style", " Technology", " Teens", " Television", " The Arts", " The Business of Green", " The City Desk", " The City", " The Marathon", " The Millennium", " The Natural World", " The Upshot", " The Weekend", " The Year in Pictures", " Theater", " Then & Now", " Thursday Styles", " Times Topics", " Travel", " U.S.", " Universal", " Upshot", " UrbanEye", " Vacation", " Washington", " Wealth", " Weather", " Week in Review", " Week", " Weekend", " Westchester", " Wireless Living", " Women's Health", " Working", " Workplace", " World", " Your Money"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_articles);

        iv_back= findViewById(R.id.iv_back);
        SearchText= findViewById(R.id.mactv);


        requestQueue = Volley.newRequestQueue(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Articles);
        SearchText.setAdapter(adapter);
        SearchText.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchArticles.this, MainActivity.class));
                finish();
            }
        });

    }

    public void showInput(View v) {
        String input = SearchText.getText().toString().trim();
        String[] singleInputs = input.split("\\s*,\\s*");

        String toastText = "";

        for (int i = 0; i < singleInputs.length; i++) {
            toastText +=  singleInputs[i] + ",";
        }



      //  Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();

        startActivity(new Intent(SearchArticles.this, ArticlesListView.class)
                .putExtra("ArticlesSearch","SearchArticles")
                .putExtra("Search",toastText));
        finish();
    }
}