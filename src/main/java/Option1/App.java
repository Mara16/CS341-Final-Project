/*
* The main App class for the Final project.
*
* 1. Creates and Launches GUI.
* 2. Creates and Launches two Peer Machines, each with its own ActorSystem to communicate with their workers.
* 3. Listens for Messages from GUI (how??) and from Peer machines (over MQTT)
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

    public static void main(String[] args) {

        // The ActorSystem extends “ActorRefFactory“ which is a hierarchical group of actors which share common configuration.
        // It is also the entry point for creating or looking up actors.
        ActorSystem system = ActorSystem.create("Hello");

        //ActorRef is an immutable and serializable handle (thread-safe and fully share-able) to an actor.
        //the first input is the class you use to create that actor
        //the second input is the optional name you give to that actor
        ActorRef bossActor = system.actorOf(Props.create(Boss.class), "Boss");

    }
}
