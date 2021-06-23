package br.ufma.lsdi.safepetmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


import br.ufma.lsdi.safepetmobile.mqtt.LocalBrokerMqtt;

import static android.content.ContentValues.TAG;


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
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String[] payload = new String(message.getPayload()).split(";");

                Double distance = new Double(payload[4]);
                String status = payload[0];

                runOnUiThread(() -> {
                    if(status.equals("in")) {
                        textViewStatus.setText("Dentro do perímetro seguro");
                        textViewStatus.setTextColor(Color.GREEN);
                    } else {
                        textViewStatus.setText("Fora do perímetro seguro");
                        textViewStatus.setTextColor(Color.RED);
                    }

                    textViewDistance.setText(String.format("Distância até a casa: %.2f metros", distance));
                });
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

    public void save(View view) {
        //"create/epl" -m "72;182;-5.512638352723324,-43.21173949860797"
        String idPet = editTextIdPet.getText().toString();
        String radius = editTextRadius.getText().toString();
        String location = editTextLocationOfTheHouse.getText().toString();

        String payload = String.format("%s;%s;%s", idPet, radius, location);

        try {
            client.publish("create/epl", new MqttMessage(payload.getBytes()));

            LocalBrokerMqtt.subscribe(client,"event/status/"+idPet);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
