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
import java.util.List;
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
        // System.out.println("Subscribed");

        // Set the callback for the client
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

                String msgJson = new String(mqttMessage.getPayload());
                Message msgFromPM = gson.fromJson(msgJson, Message.class);

                // determine which peer machine is replying
                String repliedMachineName = msgFromPM.msg;
                System.out.println("\n" + repliedMachineName + " reporting results: ");

                // output result to user
                List<String[]> result = msgFromPM.results;

                result.forEach(strings -> {
                    System.out.print("| ");
                    for (int i = 0; i < strings.length; i++) {
                        System.out.print(strings[i] + " | ");

                    }
                    System.out.println();
                });
                if(result.size() == 0)
                    System.out.println("No results found.");
                System.out.println(nonFancyDivider);

                numberOfResponses++;
                // The user, through the terminal/GUI, can send another query if both PeerMachines have replied
                if (numberOfResponses == 2) {
                    System.out.println("\n" + fancyDivider);
                    userInput();
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("Client Machine lost/disconnected from MQTT connection.");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                // System.out.println("Client machine delivered message to Broker.");
            }
        });
    }

    void userInput() throws MqttException {
        MqttMessage mqttMessage;
        System.out.println("\nReady for a query! ʕ•́ᴥ•̀ʔっ♡" +
                "\nAny field is optional (hit enter to skip");

        Scanner in = new Scanner(System.in);
        String firstName = "", lastName = "", address = "", salary = "", age = "";

        // TODO -- how to let it skip entering input for a value
        System.out.println("\nEnter a first name: ");
        firstName = in.nextLine();

        System.out.print("Enter a last name: ");
        lastName = in.nextLine();

        System.out.print("Enter an address name: ");
        address = in.nextLine();

        System.out.print("Enter a salary: ");
        salary = in.nextLine();

        System.out.print("Enter their age: ");
        age = in.nextLine();

        // TODO - edit row
        String[] row = {firstName, lastName, address, salary, age};

        boolean atLeastone = false;
        for(String queryField: row){
            if(queryField != null & !queryField.trim().equals("")){
                atLeastone = true;
            }
        }
        if(!atLeastone){
            System.err.println("Please enter at least one value!!!");
            userInput();
            return;
        }

        Message msg = new Message(App.type.CL_2_PM, null, row, null);
        gson = new Gson();
        String toSend = gson.toJson(msg);

        // publish to both peer machines
        mqttMessage = new MqttMessage(toSend.getBytes());
        mqttMessage.setQos(1);
        mqttClient.publish("/CS341FinalProj/Team2/FromClient", mqttMessage);
        numberOfResponses = 0;

        System.out.println();
        System.out.print(fancyDivider);
        System.out.println();
    }
}
