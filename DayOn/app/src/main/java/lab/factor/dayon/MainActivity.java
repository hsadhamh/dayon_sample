package lab.factor.dayon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_agenda)
    Button btnAgenda;

    @BindView(R.id.btn_splash)
    Button btnSplash;

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

    @OnClick(R.id.btn_splash)
    public void OnSplashClick(){
        Logger.d("Click splash - start splash activity.");
        //  start next activity
        Intent myIntent = new Intent(MainActivity.this, SplashActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

}
