package com.example.applicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    Button btn_search_articles,btn_most_viewed,btn_most_shared,btn_most_emailed;

    String[] days = { "1 days", "7 days", "30 days"};
    ArrayAdapter aa;

    String selectedDay ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_search_articles = findViewById(R.id.btn_search_articles);

        btn_most_viewed = findViewById(R.id.btn_most_viewed);
        btn_most_shared = findViewById(R.id.btn_most_shared);
        btn_most_emailed = findViewById(R.id.btn_most_emailed);



        btn_search_articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SearchArticles.class));
                finish();

            }
        });

        btn_most_viewed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDaySelectBox("MostViewed");
               /* startActivity(new Intent(MainActivity.this, ArticlesListView.class)
                .putExtra("ArticlesSearch","MostViewed"));
                finish();*/
            }
        });

        btn_most_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDaySelectBox("MostShared");
            }
        });

        btn_most_emailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDaySelectBox("MostEmailed");
            }
        });
    }


public void AlertDaySelectBox(String selectview){

    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    LayoutInflater inflater = getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.row_spinner, null);
    builder.setView(dialogView);

    AlertDialog alertDialog = builder.create();
    Spinner spinner = dialogView.findViewById(R.id.sp_site);
    Button bt_select = dialogView.findViewById(R.id.bt_select);


    aa = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, days);
    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(aa);


    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
           // selectedDay = days[position];
            if(days[position].equals("1 days")){
                selectedDay ="1";
            }else if(days[position].equals("7 days")){
                selectedDay ="7";
            }else if(days[position].equals("30 days")){
                selectedDay ="30";
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub

        }
    });


    bt_select.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(selectview.equals("MostViewed")){


                startActivity(new Intent(MainActivity.this, ArticlesListView.class)
                        .putExtra("ArticlesSearch","MostViewed")
                        .putExtra("SelectedDay",selectedDay));
                finish();

            }else if(selectview.equals("MostShared")){

                startActivity(new Intent(MainActivity.this, ArticlesListView.class)
                        .putExtra("ArticlesSearch","MostShared")
                        .putExtra("SelectedDay",selectedDay));
                finish();


            }else if(selectview.equals("MostEmailed")){

                startActivity(new Intent(MainActivity.this, ArticlesListView.class)
                        .putExtra("ArticlesSearch","MostEmailed")
                        .putExtra("SelectedDay",selectedDay));
                finish();


            }

        }
    });

    alertDialog.show();
}

}