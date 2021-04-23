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

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Client {

    // MQTT Client ID for the class
    final static String CLIENT_ID = "Team2_ClientMachine";

    private MqttClient mqttClient;

    public Client(){

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
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println("Message has arrived on Client Machine via MQTT.");
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
