package Option1;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.util.List;


public class Worker1 extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void preStart() {                                            //what to do when created and started
        log.info("Starting " + getSelf().path().name() + " Actor");
    }

    @Override
    public void onReceive(Object msg) {                                 //what to do when message is received

        if (msg instanceof String && msg.equals("terminate")) {
            getContext().stop(getSelf());                               //terminate the current worker

        } else if (msg instanceof String) {

            // worker name
            String workerName = getSelf().path().name();

            // determine file number to search
            String fileNum = String.valueOf(workerName.charAt(workerName.length()-1));

            log.info(workerName + " received the following message from "+ getSender().path().name() + ": " + msg.toString());              //print the identity of the sender
            int count = 0;

            String[] names = msg.toString().split(",");
            String name = names[names.length-1].replaceAll("\\s", "");

            try {
                String fileName = "path..." + fileNum +".csv";

                FileReader filereader = new FileReader(fileName);

                CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
                List<String[]> allData = csvReader.readAll();

                for (String[] row : allData) {
                    for (String cell : row) {
                        if (cell.equalsIgnoreCase(name))
                            count++;
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            String reply = workerName + " reported " + count + " occurrences in file " + fileNum;
            getSender().tell(reply, getSelf());  //respond to the sender worker with another message
        }
        else {
            unhandled(msg);                                             //received undefined msg
        }
    }

    @Override
    public void postStop() {                                            //what to do when terminated
        log.info("terminating " + getSelf().path().name());
    }
}