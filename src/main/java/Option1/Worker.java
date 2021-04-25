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
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.util.List;


public class Worker extends UntypedActor {

    private String name;
    private Gson gson;

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

            if(msg.type == App.type.PM2_2_W_CSV){
                
            }
        }
        else {
            unhandled(o);     //received undefined msg
        }
    }

    @Override
    public void postStop() {
        System.out.println("Terminating Worker " + name);
    }
}