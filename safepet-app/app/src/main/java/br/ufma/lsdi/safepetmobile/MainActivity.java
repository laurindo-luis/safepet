package br.ufma.lsdi.safepetmobile;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;

import br.ufma.lsdi.safepetmobile.mqtt.LocalBrokerMqtt;


public class MainActivity extends AppCompatActivity {

    private MqttAndroidClient client;

    private EditText editTextIdPet;
    private EditText editTextLocationOfTheHouse;
    private EditText editTextRadius;

    private TextView textViewStatus;
    private TextView textViewDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextIdPet = findViewById(R.id.editTextIdPet);
        editTextLocationOfTheHouse = findViewById(R.id.editTextLocationHouse);
        editTextRadius = findViewById(R.id.editTextRadius);

        textViewStatus = findViewById(R.id.textViewStatus);
        textViewDistance = findViewById(R.id.textViewDistance);

        client = LocalBrokerMqtt.connect(getApplicationContext(), "10.0.2.2", "appSafePet");

    }

    public void save(View view) {

    }
}
