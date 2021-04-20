package Option1;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.*;


public class Boss1 extends UntypedActor {

    //to print debugging messages
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void preStart() throws IOException {          //what to do when this actor is created and started

        log.info("Starting Boss Actor");

        ActorRef worker =  getContext().actorOf(Props.create(Worker1.class), "Worker");   //create one worker

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

            getSender().tell(new String("terminate"), getSelf());  //respond to the sender worker with another message

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