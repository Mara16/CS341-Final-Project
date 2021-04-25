/*
 * The Worker Machine class for the Final project.
 *
 * 1. Initializes the worker - finding the right file to read, setting up CSV reader, etc.
 * 2. Listens for messages from Boss over Actor system.
 * 3. Processes message and sends response to Boss over Actor system.
 *
 * CS341: Parallel Computing & Distributed Systems - Final Project
 * Team: Obsmara Ulloa + Sebin Puthenthara Suresh
 * Professor Ahmed Khaled
 * Spring 2021
 */
package Option1;

import akka.actor.UntypedActor;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class Worker extends UntypedActor {

    private String name;
    private Gson gson;
    private String pathToCSV;
    private List<String[]> results;

    @Override
    public void preStart() {
        name = getSelf().path().name();
        gson = new Gson();

        System.out.println("Starting Worker: " + name);
    }

    @Override
    public void onReceive(Object o) {

        if (o instanceof String){
            String msgStr = (String) o;
            Message msg = gson.fromJson(msgStr, Message.class);

            // The message that worker recieved was the initial
            // message with CSV file path in it.
            if(msg.type == App.type.PM2_2_W_CSV){
                this.pathToCSV = msg.msg;
            }

            // TODO: The message that the worker receievd was a query
            if(msg.type == App.type.PM_2_W_Q){
                prepResults(msg);
            }
        }
        else {
            unhandled(o);     //received undefined msg
        }
    }

    // Given a Message object containing a query, search for
    // entries with that query and fill in the List results.
    private void prepResults(Message msg) {

        // Reset/initialize the list
        results = new ArrayList<String[]>();

        // This array will keep track of which fields the user
        // is searching for. All values false by default.
        // Eg: If queryHasTerms[2] and [3] is true, the user
        // has searched with Address and Salary fields set.
        boolean[] queryHasTerms = new boolean[App.NUM_COLUMNS];

        // Keeps track of the first column that user has
        // entered for search.
        int firstIndex = -1;

        // Set values of queryHasTerms and firstIndex
        for (int i = 0; i < App.NUM_COLUMNS; i++) {
            if(msg.row[i] != null) {
                queryHasTerms[i] = true;
                firstIndex = firstIndex == -1 ? i : firstIndex;
            }
        }

        // TODO: CSVReader stuff

    }

    @Override
    public void postStop() {
        System.out.println("Terminating Worker " + name);
    }
}