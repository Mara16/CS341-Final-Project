package Option1;

import java.util.List;

public class Message {

    // Type of the message - see App.java for possible values.
    App.type type;

    // A single string message.
    String msg;

    // A single row of information - sent as query for matching
    // from Client to PeerMachine, and PeerMachine to Worker.
    String[] row;

    // A list of rows of information - sent back as result of a
    // search from Worker to PeerMachine and PeerMachine to Client.
    List<String[]> results;
}
