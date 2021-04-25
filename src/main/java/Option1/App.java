/*
 * The main App class for the Final project.
 *
 * 1. Creates a Client object and Launches GUI through it.
 * 2. Creates and Launches two Peer Machines, each with its own ActorSystem to communicate with their workers.
 * 3. Listens for Messages from Client (how??)
 * 4. Listens for Messages from Peer machines (over MQTT)
 *
 * CS342: Parallel Computing & Distributed Systems - Final Project
 * Team: Obsmara Ulloa + Sebin Puthenthara Suresh
 * Professor Ahmed Khaled
 * Spring 2021
 */

package Option1;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.eclipse.paho.client.mqttv3.MqttException;

public class App {

    // Enum for the types of messages there can be
    // TODO: Rename them appropriately
    public enum type {
        CL_2_PM,        // Client to Peer Machine - Query
        PM_2_CL,        // Peer machine to Client - Results
        PM2_2_W_CSV,    // Peer machine to worker - telling the csv file location
        PM_2_W_Q,       // Peer machine to worker - sending a query
        W_2_PM,         // Worker to peer machine - sending results
        STOP            // Client to Peer Machine OR Peer Machine to Worker - terminate command
    }

    // int indices for the Strings arrays
    public static final int FNAME = 0, LNAME = 1, ADDRESS = 2, SALARY = 3, AGE = 4;
    // Number of columns in dataset.
    public static final int NUM_COLUMNS = 5;

    public final static int NUM_PEER_MACHINES = 2;
    public final static String MQTT_BROKER = "tcp://broker.hivemq.com:1883";

    static ActorSystem[] systems;  // TODO ??
    static ActorRef[] pMachines;

    public static void main(String[] args) {

        // 1. Creates a Client object and Launches GUI through it.
        Client client = new Client();

        // 2. Creates and Launches two Peer Machines, each with its own ActorSystem to communicate with their workers.
        systems = new ActorSystem[NUM_PEER_MACHINES];
        pMachines = new ActorRef[NUM_PEER_MACHINES];
        for (int i = 1; i <= NUM_PEER_MACHINES; i++) {
            systems[i - 1] = ActorSystem.create("PMSystem" + i);
            pMachines[i - 1] = systems[i - 1].actorOf(Props.create(PeerMachine.class), "PM" + i);
        }

        // 3. Listens for Messages from Client (how??)

        // 4. Listens for Messages from Peer machines (over MQTT)
    }

    // Handles an MQTTExcpetion by printing the Exception details
    public static void handleMQTTException(MqttException mex) {
        System.out.println("reason " + mex.getReasonCode());
        System.out.println("msg " + mex.getMessage());
        System.out.println("loc " + mex.getLocalizedMessage());
        System.out.println("cause " + mex.getCause());
        System.out.println("excep " + mex);
        mex.printStackTrace();
    }
}
