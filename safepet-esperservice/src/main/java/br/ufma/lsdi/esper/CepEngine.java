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

public class CepEngine {
    private static CompilerArguments compilerArguments;
    private static EPCompiler epCompiler;
    private static EPRuntime epRuntime;
    private static UpdateListener updateListener;
    private static MqttClient mqttClient = null;

    public static void configure (MqttClient client) {
        mqttClient = client;
        Configuration configuration = new Configuration();
        configuration.getCommon().addEventType(LocationUpdate.class);
        compilerArguments = new CompilerArguments(configuration);
        epCompiler = EPCompilerProvider.getCompiler();
        epRuntime = EPRuntimeProvider.getDefaultRuntime(configuration);

    }

    public static void sendEvent(Object object, String nameEventSensor) {
        epRuntime.getEventService().sendEventBean(object, nameEventSensor);
    }

}
