package lab.factor.dayon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_agenda)
    Button btnAgenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_agenda)
    public void OnAgendaClick() {
        Toast.makeText(getApplicationContext(), "Click Done", Toast.LENGTH_SHORT).show();
        //  start next activity
        Intent myIntent = new Intent(MainActivity.this, AgendaView.class);
        MainActivity.this.startActivity(myIntent);
    }

}
