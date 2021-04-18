package br.ufma.lsdi.safepetmobile.mqtt;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import static java.nio.charset.StandardCharsets.UTF_8;

public class BrokerManager {

    public static Mqtt5BlockingClient conectar() {

        System.out.println("Conectando....");
        final String host = "0863dadf94e94ed6aea53e56d4ac2b76.s1.eu.hivemq.cloud";
        final String username = "safepet";
        final String password = "Eduardo1996";

        //create an MQTT client
        final Mqtt5BlockingClient client = MqttClient.builder()
                .useMqttVersion5()
                .serverHost(host)
                .serverPort(8883)
                .sslWithDefaultConfig()
                .buildBlocking();


        //connect to HiveMQ Cloud with TLS and username/pw
        client.connectWith()
                .simpleAuth()
                .username(username)
                .password(UTF_8.encode(password))
                .applySimpleAuth()
                .send();

        System.out.println("Conectado");
        
        return client;
    }

    public static void subscribe(Mqtt5BlockingClient client, String topico) {
        client.subscribeWith()
                .topicFilter(topico)
                .send();
    }
}
