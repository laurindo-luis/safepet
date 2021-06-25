package br.ufma.lsdi.esper;

import br.ufma.lsdi.esper.event.LocationUpdate;
import br.ufma.lsdi.esper.monitor.Monitor;
import br.ufma.lsdi.mqtt.LocalBrokerMqtt;
import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.compiler.client.EPCompiler;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.espertech.esper.runtime.client.*;
import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CepEngine {
    private static CompilerArguments compilerArguments;
    private static EPCompiler epCompiler;
    private static EPRuntime epRuntime;
    private static UpdateListener updateListener;
    private static ExecutorService executorService;

    public static void configure () {
        Configuration configuration = new Configuration();
        configuration.getCommon().addEventType(LocationUpdate.class);
        compilerArguments = new CompilerArguments(configuration);
        epCompiler = EPCompilerProvider.getCompiler();
        epRuntime = EPRuntimeProvider.getDefaultRuntime(configuration);
        executorService = Executors.newSingleThreadExecutor();
        createUpdateListener();
    }

    public static void compiledAndDeploy(Monitor monitor) {
        boolean isDeploy = epRuntime.getDeploymentService()
                .isDeployed(monitor.getRuleId());
        if(!isDeploy) {
            System.out.println("\n*Creating EPL");
            EPCompiled compiledRule = null;
            try {
                compiledRule = epCompiler.compile(String.format("@name('%s') %s",
                        monitor.getRuleName(), monitor.getRuleEpl()), compilerArguments);
            } catch (EPCompileException e) {
                e.printStackTrace();
            }

            EPDeployment epDeployment;
            try {
                epDeployment = epRuntime.getDeploymentService().deploy(
                        compiledRule, new DeploymentOptions().setDeploymentId(monitor.getRuleId())
                );

                EPStatement statement = epRuntime.getDeploymentService()
                        .getStatement(epDeployment.getDeploymentId(), monitor.getRuleName());
                statement.addListener(updateListener);

                //Name da EPL no ambiente de execução
                System.out.println("EPL Name: "+statement.getName());
                //Id da EPL no ambiente de execução
                System.out.println("EPL Id: "+epDeployment.getDeploymentId());
                //EPL
                System.out.println("EPL: "+monitor.getRuleEpl());
                //EPL Criada
                System.out.println("*EPL Created");
            } catch (EPDeployException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendEvent(Object object, String nameEventSensor) {
        epRuntime.getEventService().sendEventBean(object, nameEventSensor);
    }
    public static void createUpdateListener() {
        MqttClient mqttClientPubishEventComplex =
                LocalBrokerMqtt.connect("127.0.0.1", "clientEsperCepClientPubishEventComplex");

        updateListener = (newData, oldData, epStatement, epRuntime) -> {
            //Aqui acontece as ações quando um evento complexo é gerado
            for (EventBean eventBean : newData) {
                String typeEvent = "";
                StringBuilder payload = new StringBuilder();

                if(epStatement.getName().equals("epl-location-in")) {
                    typeEvent = "in";
                } else if(epStatement.getName().equals("epl-location-out")) {
                    typeEvent = "out";
                }

                String[] propertyNames = eventBean.getEventType().getPropertyNames();
                for (String propertyName : propertyNames) {
                    payload.append(";").append(eventBean.get(propertyName));
                }

                //event/status/out;72;-5.55445,-45.8454;distance
                String topic = String.format("event/status/%s", eventBean.get("idPet"));
                payload.insert(0, typeEvent);
                executorService.execute(() ->
                        LocalBrokerMqtt.publish(mqttClientPubishEventComplex, topic, payload.toString())
                );
            }
        };
    }
}
