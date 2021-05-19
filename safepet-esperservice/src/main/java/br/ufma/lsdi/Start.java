package br.ufma.lsdi;

import br.ufma.lsdi.esper.CepEngine;
import br.ufma.lsdi.esper.event.LocationUpdate;
import br.ufma.lsdi.esper.monitor.MonitorInPet;
import br.ufma.lsdi.esper.monitor.MonitorOutPet;
import br.ufma.lsdi.mqtt.LocalBrokerMqtt;
import br.ufma.lsdi.util.Parse;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.varia.NullAppender;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * tópico para gerar uma epl :  create/epl/id_pet;distance;location_home
 * tópico para enviar um evento complexo para o android: event/status/type_event;id_pet;distance;coordenada_atual
 *                                                       event/status/out;72;200.00;-5.55445,-45.8454
 *
 *  coordenada exemplo:
 *  latitude = -5.512638352723324
 *  longitude = -43.21173949860797
 */
public class Start {

    static MqttClient client = null;

    public static void main(String[] args) {

        //Remover um alerta que aparece no terminal
        BasicConfigurator.configure(new NullAppender());

        client = LocalBrokerMqtt.connect("127.0.0.1", "clientEsperCep");
        CepEngine.configure(client);
        try {
            client.subscribe("+/+/sensor/gps", (topic, message) ->
                    CepEngine.sendEvent(
                        Parse.parseLocationUpdate(topic, message),
                        LocationUpdate.getNameEvent()
                    )
            );
        } catch (MqttException e) {
            e.printStackTrace();
        }

        
        System.out.println("Esper CEP Service started!");
    }
}