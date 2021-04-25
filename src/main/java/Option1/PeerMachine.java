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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeerMachine extends UntypedActor {

    private String name;
    private int machineNumber;
    private ActorRef[] workers = new ActorRef[NUM_WORKERS];
    private final static int NUM_WORKERS = 4;
    private int numResponses = 0;
    private List<String[]> searchResults;

    private Gson gson;

    @Override
    public void preStart() throws IOException {

        name = getSelf().path().name();
        machineNumber = Integer.parseInt(name.replaceAll("[^0-9]", ""));
        System.out.println("PeerMachine " + name + " created.");

        gson = new Gson();

        String csvPathWithHashTag = getPathToCSVs();
        Message msg = new Message();

        // Set the type of message sent to worker as
        // "message with CSV path".
        msg.type = App.type.PM2_2_W_CSV;

        // Create workers
        // Note: They go from 1 to 4, not 0 to 3.
        for (int i = 1; i <= NUM_WORKERS; i++) {

            workers[i - 1] = getContext().actorOf(Props.create(Worker.class),
                    name + "_W" + i);

            // Update the path to reflect the CSV file for that worker
            msg.msg = csvPathWithHashTag.replace("File#", "File" + i);

            // Convert the Message object to JSON
            String toSend = gson.toJson(msg);

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

        // TODO: MQTT part
        /*// Message that PM_n receives from Client Program
        if(msg.type == App.type.CL_2_PM){

        }*/


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