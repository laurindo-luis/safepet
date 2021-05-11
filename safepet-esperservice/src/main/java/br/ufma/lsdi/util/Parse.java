package br.ufma.lsdi.util;

import br.ufma.lsdi.esper.event.LocationUpdate;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Parse {
    public static LocationUpdate parseLocationUpdate(String topic, MqttMessage msg) {
        String payload = new String(msg.getPayload());
        String[] splitTopic = topic.split("/");
        Double latitude = Double.valueOf(payload.split(",")[0]);
        Double longitude = Double.valueOf(payload.split(",")[1].trim());

        return new LocationUpdate(
                Integer.valueOf(splitTopic[1]),
                latitude,
                longitude
        );
    }

    public static Coordinate parseCoordinater(String coordinate) {
        String[] coordinater = coordinate.split(",");
        return new Coordinate(
                new Double(coordinater[0]),
                new Double(coordinater[1])
        );
    }
}
