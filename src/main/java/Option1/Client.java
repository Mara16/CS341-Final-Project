/*
 * The Client class for the Final project.
 *
 * - This class is an MQTT Client as well
 * - This class has:
 *   - A constructor,
 *   - Methods to:
 *        launch GUI,
 *        listen to messages from PeerMachines,
 *        ... (what else?)
 * - ?
 *
 *
 * CS342: Parallel Computing & Distributed Systems - Final Project
 * Team: Obsmara Ulloa + Sebin Puthenthara Suresh
 * Professor Ahmed Khaled
 * Spring 2021
 */

package Option1;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    // MQTT Client ID for the class
    final static String CLIENT_ID = "Team2_ClientMachine";
    private Gson gson;


    private MqttClient mqttClient;

    public Client() {

        try {
            createAndInitializeMQTT();

        } catch (MqttException e) {
            App.handleMQTTException(e);
        }

    }

    // Method to create and initialize the MQTT Client that represents
    // this client machine.
    public void createAndInitializeMQTT() throws MqttException {

        // Construct the MQTT Client
        this.mqttClient = new MqttClient(
                App.MQTT_BROKER, CLIENT_ID, new MemoryPersistence()
        );

        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);

        // Connecting to the broker
        this.mqttClient.connect(connOpts);
        System.out.println("Connected to broker: " + App.MQTT_BROKER);

        // Subscribing to topic - QoS 1
        mqttClient.subscribe("/CS341FinalProj/Team2/FromPeerMachine/#", 1);
        System.out.println("Subscribed");

        // Set the callback for the client
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                System.out.println("Message has arrived on Client Machine via MQTT." + "\n" + topic +
                        "\n\tMessage: " + new String(mqttMessage.getPayload()));

                // TODO - wait for 2 peer machine replies before new search

                // The user, through the terminal/GUI, can type one of the commands
                // prompt user for another command?
                System.out.println("Enter a first name: ");
                String statement = "";
                Scanner in = new Scanner(System.in);
                statement = in.next();

                // TODO - edit row
                String[] row = {statement, "", "", "", ""};
                Message msg = new Message(App.type.CL_2_PM, null, null, null);
                gson = new Gson();
                String toSend = gson.toJson(msg);

                // publish to both peer machines
                mqttMessage = new MqttMessage(toSend.getBytes());
                mqttMessage.setQos(1);
                mqttClient.publish("/CS341FinalProj/Team2/FromClient", mqttMessage);
            }

            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("Client Machine lost/disconnected from MQTT connection.");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("Client machine delivered message to Broker.");
            }
        });
    }
}
