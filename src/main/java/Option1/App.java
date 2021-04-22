/*
 * The main App class for the Final project.
 *
 * 1. Creates a Client object and Launches GUI through it.
 * 2. Creates and Launches two Peer Machines, each with its own ActorSystem to communicate with their workers.
 * 3. Listens for Messages from Client (how??)
 * 4. Listens for Messages from Peer machines (over MQTT)
 *
 * CS342: Parallel Systems & Distributed Systems - Final Project
 * Team: Obsmara Ulloa + Sebin Puthenthara Suresh
 * Professor Ahmed Khaled
 * Spring 2021
 */

package Option1;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class App {

    final static int NUM_PEER_MACHINES = 2;
    static ActorSystem[] systems;
    static ActorRef[] pMachines;


    public static void main(String[] args) {

        // 1. Creates a Client object and Launches GUI through it.

        // 2. Creates and Launches two Peer Machines, each with its own ActorSystem to communicate with their workers.
        systems = new ActorSystem[NUM_PEER_MACHINES];
        for (int i = 1; i <= NUM_PEER_MACHINES; i++) {
            systems[i - 1] = ActorSystem.create("PMSystem" + i);
            pMachines[i - 1] = systems[i-1].actorOf(Props.create(PeerMachine.class), "PeerMachine" + i);
        }

        // 3. Listens for Messages from Client (how??)

        // 4. Listens for Messages from Peer machines (over MQTT)


    }
}
