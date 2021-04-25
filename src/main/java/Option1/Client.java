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

    private int numberOfResponses = 0;

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
                System.out.println("\nA message has arrived on Client Machine via MQTT" + "\nfrom topic: " + topic +
                        "\nMessage: " + new String(mqttMessage.getPayload()));

                // TODO - display message formatted, currently received as
                //  {"type":"PM_2_CL","results":[["Harry","Potter","05180 Sparks Run Port Keith. MO 48921","58000","32","PM1_W3"] ,["Fleamont","Potter","575 Fuller Rapids Suite 993 East Michael. TX 38202","40000","26","PM1_W1"]]}
                String s = new String(mqttMessage.getPayload());
                String splitting[] = s.split(",",2); // comma will be matched 1 times

                // determine which peer machine is replying
                char machine = splitting[0].charAt(12);
                System.out.println("Machine: " + machine + "reporting results: ");

                // output 
                /*for (int i = 0; i < splitting.length; i++) {
                    System.out.println("Splitted: " + splitting[i]);
                }*/

                numberOfResponses++;
                // The user, through the terminal/GUI, can send another query if both PeerMachines have replied
                if (numberOfResponses == 2) {

                    System.out.println("\nReceived responses from both Peer Machines. Ready for another query! :D" +
                            "\n Any field is optional");

                    Scanner in = new Scanner(System.in);
                    String firstName = "", lastName = "", address = "", salary = "", age = "";

                    // TODO -- how to let it skip entering input for a value
                    System.out.println("\nEnter a first name: ");
                    firstName = in.nextLine();

                    System.out.println("\nEnter a last name: ");
                    lastName = in.nextLine();

                    System.out.println("\nEnter an address name: ");
                    address = in.nextLine();

                    System.out.println("\nEnter a salary name: ");
                    salary = in.nextLine();

                    System.out.println("\nEnter their age: ");
                    age = in.nextLine();

                    // TODO - edit row
                    String[] row = {firstName, lastName, address, salary, age};
                    Message msg = new Message(App.type.CL_2_PM, null, row, null);
                    gson = new Gson();
                    String toSend = gson.toJson(msg);

                    // publish to both peer machines
                    mqttMessage = new MqttMessage(toSend.getBytes());
                    mqttMessage.setQos(1);
                    mqttClient.publish("/CS341FinalProj/Team2/FromClient", mqttMessage);
                }
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
