package br.ufma.lsdi.safepetmobile.mqtt;


import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import static android.content.ContentValues.TAG;


public class LocalBrokerMqtt {

    public static MqttAndroidClient connect(Context context, String ip, String id) {
        System.out.println("Connecting broker...");
        MqttAndroidClient client = null;

        try {
            client = new MqttAndroidClient(context, String.format("tcp://%s", ip), id);
            IMqttToken iMqttToken = client.connect();
            iMqttToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Sucess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Failure");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return client;
    }

    public static void subscribe(MqttAndroidClient client, String topic) {
        try {
            IMqttToken iMqttToken = client.subscribe(topic, 0);
            iMqttToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Sucess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
