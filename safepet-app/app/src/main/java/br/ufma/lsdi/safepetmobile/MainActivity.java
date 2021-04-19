package br.ufma.lsdi.safepetmobile;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.widget.TextView;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import br.ufma.lsdi.safepetmobile.mqtt.BrokerManager;
import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;


public class MainActivity extends AppCompatActivity {

    private Mqtt5BlockingClient client;
    private TextView textCoordenatorOne;
    private TextView textCoordenatorTwo;
    private TextView textCoordenatorThree;
    private TextView textCoordenatorFour;
    private TextView textCoordenatorFive;
    private TextView textCoordenatorSix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textCoordenatorOne = findViewById(R.id.textCoordenatorOne);
        textCoordenatorTwo = findViewById(R.id.textCoordenatorTwo);
        textCoordenatorThree = findViewById(R.id.textCoordenatorThree);
        textCoordenatorFour = findViewById(R.id.textCoordenatorFour);
        textCoordenatorFive = findViewById(R.id.textCoordenatorFive);
        textCoordenatorSix = findViewById(R.id.textCoordenatorSix);

        client = BrokerManager.conectar();
        BrokerManager.subscribe(client, CONSTANTES.TOPIC_DOG_100);
        BrokerManager.subscribe(client, CONSTANTES.TOPIC_HORSE_103);
        BrokerManager.subscribe(client, CONSTANTES.TOPIC_DUCK_104);
        BrokerManager.subscribe(client, CONSTANTES.TOPIC_CAT_101);
        BrokerManager.subscribe(client, CONSTANTES.TOPIC_HORSE_108);
        BrokerManager.subscribe(client, CONSTANTES.TOPIC_DUCK_110);

        client.toAsync().publishes(ALL, publish -> {
            String topic = String.valueOf(publish.getTopic());
            String payload = String.valueOf(UTF_8.decode(publish.getPayload().get()));

            workerThread(topic, payload);

        });

    }


    @WorkerThread
    void workerThread(String topic, String payload) {
        ContextCompat.getMainExecutor(this).execute(()  -> {
            if(topic.equals(CONSTANTES.TOPIC_DOG_100)) {
                textCoordenatorOne.setText(String.format("DOG 100 \n %s", payload));
            } else if (topic.equals(CONSTANTES.TOPIC_HORSE_103)) {
                textCoordenatorTwo.setText(String.format("HORSE 103 \n %s", payload));
            } else if (topic.equals(CONSTANTES.TOPIC_DUCK_104)) {
                textCoordenatorThree.setText(String.format("DUCK 104 \n %s", payload));
            } else if (topic.equals(CONSTANTES.TOPIC_CAT_101)) {
                textCoordenatorFour.setText(String.format("CAT 101 \n %s", payload));
            } else if (topic.equals(CONSTANTES.TOPIC_HORSE_108)) {
                textCoordenatorFive.setText(String.format("HORSE 108 \n %s", payload));
            } else if (topic.equals(CONSTANTES.TOPIC_DUCK_110)) {
                textCoordenatorSix.setText(String.format("DUCK 110 \n %s", payload));
            }
        });
    }
}
