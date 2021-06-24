package br.ufma.lsdi.safepetmobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import br.ufma.lsdi.safepetmobile.R;
import br.ufma.lsdi.safepetmobile.mqtt.BrokerMqtt;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BrokerMqtt.connect(getApplicationContext(), "10.0.2.2", "appSafePet");
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 3000);

    }
}
