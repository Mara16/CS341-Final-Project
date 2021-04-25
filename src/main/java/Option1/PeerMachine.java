/*
 * The Peer Machine class for the Final project.
 * This class is equivalent to a Boss machine in Actor system, since each Peer machine should be a boss actor.
 *
 * 1. Creates 4 workers in the system.
 * 2. Listens for messages over MQTT from the Client.
 * 3. Sends messages to workers over the actor system for processing the work.
 * 4. Listens for messages over the actor system from workers.
 * 5. Sends response to Client over MQTT.
 *
 * CS341: Parallel Computing & Distributed Systems - Final Project
 * Team: Obsmara Ulloa + Sebin Puthenthara Suresh
 * Professor Ahmed Khaled
 * Spring 2021
 */

package Option1;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PeerMachine extends UntypedActor {

    private String name;
    private int machineNumber;
    private ActorRef[] workers = new ActorRef[NUM_WORKERS];
    private final static int NUM_WORKERS = 4;
    private int numResponses = 0;
    private List<String[]> searchResults;

    private MqttClient mqttClient;
    private String CLIENT_ID = "Team2_";

    private Gson gson;

    @Override
    public void preStart() throws MqttException {

        name = getSelf().path().name();
        machineNumber = Integer.parseInt(name.replaceAll("[^0-9]", ""));
        CLIENT_ID += name;  // CLIENT_ID is now something like "Team2_PM1"

        gson = new Gson();

        Message initialMsgToWorkers = new Message();
        // Set the type of message sent to worker as "message with CSV path".
        initialMsgToWorkers.type = App.type.PM2_2_W_CSV;

        String csvPathWithHashTag = getPathToCSVs();

        // Create workers
        // Note: They go from 1 to 4, not 0 to 3.
        for (int i = 1; i <= NUM_WORKERS; i++) {

            workers[i - 1] = getContext().actorOf(Props.create(Worker.class),
                    name + "_W" + i);

            // Update the path to reflect the CSV file for that worker
            initialMsgToWorkers.msg = csvPathWithHashTag.replace("File#", "File" + i);

            // Convert the Message object to JSON
            String toSend = gson.toJson(initialMsgToWorkers);

            // Send JSON to worker
            workers[i - 1].tell(toSend, self());

            // TODO: TEST - Remove this part
            Message testMsg = new Message();
            testMsg.type = App.type.PM_2_W_Q;
            // String[] testQ = {null, null, null, "30000", null};
            //String[] testQ = {"Harry", "Potter", null, null, null};
            String[] testQ = {null, "Potter", null, null, null};
            testMsg.row = testQ;
            workers[i - 1].tell(gson.toJson(testMsg), self());
        }

        initializeMQTTStuff();

        System.out.println("PeerMachine " + name + " created.");
    }

    @Override
    public void onReceive(Object o) {
        if (o instanceof String) {

            String msgStr = (String) o;
            Message msg = gson.fromJson(msgStr, Message.class);


            // Response message from Worker to PeerMachine
            if (msg.type == App.type.W_2_PM) {

                if (searchResults == null)
                    searchResults = new ArrayList<>();
                searchResults.addAll(msg.results);

                this.numResponses++;

                // Received response from all workers.
                if (this.numResponses == NUM_WORKERS) {

                    //System.out.println(this.name + " got response from all workers");

                    // Create response to send to Client
                    Message responseMsg = new Message();
                    responseMsg.type = App.type.PM_2_CL;
                    responseMsg.results = this.searchResults;
                    String responseJson = gson.toJson(responseMsg);

                    // TODO : Send response JSON to Client over MQTT

                    this.numResponses = 0;
                    this.searchResults = null;
                }
            }

        } else {

            unhandled(o);
        }
    }

    @Override
    public void postStop() {                        //what to do when terminated

        System.out.println("Terminating worker");
    }

    // Method to create and initialize the MQTT Client that represents
    // this Peer machine.
    private void initializeMQTTStuff() throws MqttException {
        // Construct the MQTT Client
        this.mqttClient = new MqttClient(
                App.MQTT_BROKER, CLIENT_ID, new MemoryPersistence()
        );

        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);

        // Connecting to the broker
        this.mqttClient.connect(connOpts);
        System.out.println(this.name + " connected to broker: " + App.MQTT_BROKER);

        // Subscribing to topic - QoS 1
        mqttClient.subscribe("/CS341FinalProj/Team2/FromClient", 1);

        // Set the callback for PeerMachine's MQTT client
        mqttClient.setCallback(new MqttCallback() {

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

                System.out.println("Message has arrived on PeerMachne via MQTT." + "\n" + topic +
                        "\n\tMessage: " + new String(mqttMessage.getPayload()));

                String msgStr = new String(mqttMessage.getPayload());
                Message clientMsg = gson.fromJson(msgStr, Message.class);

                // Message that PM_n receives from Client Program
                if (clientMsg.type == App.type.CL_2_PM) {

                    // Construct JSON to send from PM to each worker
                    Message toWorkerMsg = new Message();
                    toWorkerMsg.type = App.type.PM_2_W_Q;
                    toWorkerMsg.row = clientMsg.row;
                    String toWorkerJson = gson.toJson(toWorkerMsg);

                    // Send query request json to each worker
                    for (var worker : workers){
                        worker.tell(toWorkerJson, getSelf());
                    }
                }

            }

            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println(name + " lost/disconnected from MQTT connection.");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                //System.out.println(name + " delivered message to Broker.");
            }
        });
    }

    // Get the absoltute path string for the folder holding
    // the CSV files.
    public String getPathToCSVs() {
        // Name of the CSV file we're looking for
        String filename = this.name + "_File1.csv";

        // This array stores the relative path to the CSV files within the project
        // folder. If the CSV files are placed elsewhere in the Project folder,
        // change it accordingly.
        String[] pathInProject = {"src", "main", "java", "Option1", this.name, filename};

        // Path object that points to the CSV file.
        Path path = Paths.get("", pathInProject);

        // Exit the program if the file cannot be found at specified path.
        if (!Files.exists(path)) {
            System.err.println("File not found in specified project folder : " +
                    path.toAbsolutePath() + "\n" +
                    "Please modify the pathInProject[] array.");

            System.exit(1);     // Exit Program
        }

        // Replace the name "File1" with "File#" so that it can be easily changed.
        return path.toAbsolutePath().toString().replace("File1", "File#");
    }
}