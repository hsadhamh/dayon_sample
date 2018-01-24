package io.tact.tech.dayon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by hsadhamh on 1/10/2018.
 */

public class SampleMainActivity1 extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_main);
        Button btn_sample = (Button) findViewById(R.id.btn_month_view);
        btn_sample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SampleMainActivity1.this, "Show month activity", Toast.LENGTH_SHORT).show();
                startMonthActivity();
            }
        });

        Button btn_sample_pager = (Button) findViewById(R.id.btn_month_pager);
        btn_sample_pager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SampleMainActivity1.this, "Show month pager activity", Toast.LENGTH_SHORT).show();
                startMonthPagerActivity();
            }
        });

        Button btn_agenda = (Button) findViewById(R.id.btn_agenda_view);
        btn_agenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SampleMainActivity1.this, "Show list activity", Toast.LENGTH_SHORT).show();
                startEventsActivity();
            }
        });

        Button btn_db = (Button) findViewById(R.id.btn_db_view);
        btn_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SampleMainActivity1.this, "Show DB activity", Toast.LENGTH_SHORT).show();
                startDbActivity();
            }
        });
    }

    public void startMonthActivity(){
        Intent temp = new Intent(this, OneMonthActivity.class);
        startActivity(temp);
    }

    public void startMonthPagerActivity(){
        Intent temp = new Intent(this, SimpleMonthActivity.class);
        startActivity(temp);
    }

    public void startEventsActivity(){
        Intent temp = new Intent(this, SimpleEventsListView.class);
        startActivity(temp);
    }

    public void startDbActivity(){
        Intent temp = new Intent(this, DatabaseActivity.class);
        startActivity(temp);
    }
}
