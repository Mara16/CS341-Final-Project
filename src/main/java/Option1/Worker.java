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
import com.opencsv.CSVReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Worker extends UntypedActor {

    private String name;
    private Gson gson;
    private String pathToCSV;
    private List<String[]> results;
    private CSVReader csvReader;
    private List<String[]> csvData;

    @Override
    public void preStart() {
        name = getSelf().path().name();
        gson = new Gson();

        System.out.println("Starting Worker: " + name);
    }

    @Override
    public void onReceive(Object o) throws IOException {

        if (o instanceof String) {
            String msgStr = (String) o;
            Message msg = gson.fromJson(msgStr, Message.class);

            // The message that worker recieved was the initial
            // message with CSV file path in it.
            if (msg.type == App.type.PM2_2_W_CSV) {

                // Set the CSV path, CSVReader, and list with data
                this.pathToCSV = msg.msg;
                this.csvReader = new CSVReader(
                        Files.newBufferedReader(
                                Paths.get(pathToCSV)
                        )
                );
                this.csvData = csvReader.readAll();
            }

            // TODO: The message that the worker receieved was a query
            if (msg.type == App.type.PM_2_W_Q) {
                prepResults(msg);

                // TODO: TEST - REMOVE THIS PART
                this.results.forEach(row -> {
                    System.out.println(Arrays.toString(row));
                });
                System.out.println("===============");
            }
        } else {
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
            if (msg.row[i] != null) {
                queryHasTerms[i] = true;
                firstIndex = firstIndex == -1 ? i : firstIndex;
            }
        }

        // Find rows from the CSV that matches the first term
        // in search query.
        results = new ArrayList<>();
        for (String[] row : csvData) {

            // If firstIndex is either salary or age
            if (firstIndex >= App.SALARY) {
                if (row[firstIndex].equals(msg.row[firstIndex]))
                    results.add(row);
            } else if (row[firstIndex].toLowerCase().contains(
                    msg.row[firstIndex].toLowerCase())) {
                results.add(row);
            }
        }

        // Remove the rows that don't match the other query terms.
        for (int i = firstIndex + 1; i < App.NUM_COLUMNS; i++) {

            for (String[] tempRow : results) {
                if (queryHasTerms[i]) {
                    if (i >= App.SALARY) {
                        // For salary and age, remove rows that
                        // aren't EXACT matches.
                        if (!tempRow[i].equals(msg.row[i])) {
                            results.remove(tempRow);
                        }

                    } else if (!tempRow[i].toLowerCase().contains(
                            msg.row[i].toLowerCase())) {
                        results.remove(tempRow);
                    }
                }
            }
        }
    }

    @Override
    public void postStop() {
        System.out.println("Terminating Worker " + name);
    }
}