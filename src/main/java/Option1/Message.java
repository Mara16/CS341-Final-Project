/*
 * The Message class contains messages being sent between virtual devices.
 *
 *
 * CS342: Parallel Computing & Distributed Systems - Final Project
 * Team: Obsmara Ulloa + Sebin Puthenthara Suresh
 * Professor Ahmed Khaled
 * Spring 2021
 */

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

    public Message() {}

    public Message(App.type type, String msg, String[] row, List<String[]> results) {
        this.type = type;
        this.msg = msg;
        this.row = row;
        this.results = results;
    }
}
