package br.ufma.lsdi.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocalBrokerMqtt {

    public static MqttClient connect(String ip, String id) {
        System.out.println("Connecting broker...");
        MqttClient client = null;
        try {
            client = new MqttClient(String.format("tcp://%s", ip), id);
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setCleanSession(true);
            client.connect(mqttConnectOptions);
            System.out.println("Connected!");
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return client;
    }

    public static void publish(MqttClient client, String topic, String payload) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            MqttMessage message = new MqttMessage(payload.getBytes());
            try {
                client.publish(topic, message);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
    }
}
