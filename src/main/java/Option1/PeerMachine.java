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
 * CS342: Parallel Systems & Distributed Systems - Final Project
 * Team: Obsmara Ulloa + Sebin Puthenthara Suresh
 * Professor Ahmed Khaled
 * Spring 2021
 */

package Option1;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PeerMachine extends UntypedActor {

    //to print debugging messages
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void preStart() throws IOException {          //what to do when this actor is created and started

        log.info("Starting Boss Actor");

        ActorRef worker =  getContext().actorOf(Props.create(Worker.class), "Worker");   //create one worker

        //Scanner scanner = new Scanner(System.in);
        log.info("Enter a first name, last Name, address, salary, or age");
        //String input = scanner.next();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();


        log.info("You entered " + input);

        log.info("Boss is creating 4 workers to search a file each for \"" + input +"\"");

    }

    @Override
    public void onReceive(Object msg) {    //what to do when a message is received
        if (msg instanceof String) {

            log.info("Boss received message: " + msg.toString());  //print the identity of the sender

            getSender().tell(("terminate"), getSelf());  //respond to the sender worker with another message

        } else {

            unhandled(msg);
            //getContext().stop(getSelf());                               //stop the sender and with it the application
        }
    }


    @Override
    public void postStop() {                        //what to do when terminated

        log.info("terminating the Boss actor");

    }
}