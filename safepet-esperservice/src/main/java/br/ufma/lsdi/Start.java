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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public static void main(String[] args) {

        //Remover um alerta que aparece no terminal
        BasicConfigurator.configure(new NullAppender());

        CepEngine.configure();
        try {
            MqttClient clientInputEvent = LocalBrokerMqtt.connect("127.0.0.1", "clientEsperCepInputEvent");
            clientInputEvent.subscribe("+/+/sensor/gps", (topic, message) ->
                    CepEngine.sendEvent(
                        Parse.parseLocationUpdate(topic, message),
                        LocationUpdate.getNameEvent()
                    )
            );
        } catch (MqttException e) {
            e.printStackTrace();
        }

        try {
            MqttClient clientInputCreateEpl = LocalBrokerMqtt.connect("127.0.0.1", "clientEsperCepInputCreateEpl");
            clientInputCreateEpl.subscribe("create/epl", (topic, message) -> {
                String[] payload = new String(message.getPayload()).split(";");
                Integer idPet = Integer.valueOf(payload[0]);
                Double radius = Double.valueOf(payload[1]);
                String coordinater = payload[2];

                //Criando uma EPL de saida e entrada para o pet
                CepEngine.compiledAndDeploy(new MonitorInPet(idPet, radius,
                        Parse.parseCoordinater(coordinater)));
                CepEngine.compiledAndDeploy(new MonitorOutPet(idPet, radius,
                        Parse.parseCoordinater(coordinater)));
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        System.out.println("Esper CEP Service started!");
    }
}