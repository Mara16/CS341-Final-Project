/*
 * The Client class for the Final project.
 *
 * - This class is an MQTT Client as well
 * - This class has:
 *   - A constructor,
 *   - Methods to:
 *        launch GUI,
 *        listen to messages from PeerMachines,
 *        ... (what else?)
 * - ?
 *
 *
 * CS342: Parallel Computing & Distributed Systems - Final Project
 * Team: Obsmara Ulloa + Sebin Puthenthara Suresh
 * Professor Ahmed Khaled
 * Spring 2021
 */

package Option1;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class Client {
    // MQTT Client ID for the class
    final static String CLIENT_ID = "Team2_ClientMachine";
    private Gson gson;
    String fancyDivider = "✼ •• " +
            "┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈ •• ✼";
    String nonFancyDivider =
            "┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈";

    private int numberOfResponses = 0;
    private MqttClient mqttClient;

    public Client() {

        try {
            createAndInitializeMQTT();
            new GUI();

        } catch (MqttException e) {
            App.handleMQTTException(e);
        }
    }

    // Method to create and initialize the MQTT Client that represents
    // this client machine.
    public void createAndInitializeMQTT() throws MqttException {

        // Construct the MQTT Client
        this.mqttClient = new MqttClient(
                App.MQTT_BROKER, CLIENT_ID, new MemoryPersistence()
        );

        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);

        // Connecting to the broker
        this.mqttClient.connect(connOpts);
        System.out.println("Connected to broker: " + App.MQTT_BROKER);

        // Subscribing to topic - QoS 1
        mqttClient.subscribe("/CS341FinalProj/Team2/FromPeerMachine/#", 1);
        // System.out.println("Subscribed");

        // Set the callback for the client
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

                String msgJson = new String(mqttMessage.getPayload());
                Message msgFromPM = gson.fromJson(msgJson, Message.class);

                // determine which peer machine is replying
                String repliedMachineName = msgFromPM.msg;
                System.out.println("\n" + repliedMachineName + " reporting results: ");

                // output result to user
                List<String[]> result = msgFromPM.results;
                printTable(result);

                numberOfResponses++;
                // The user, through the terminal/GUI, can send another query if both PeerMachines have replied
                if (numberOfResponses == 2) {
                    System.out.println("\n" + fancyDivider);
                    userInput();
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("Client Machine lost/disconnected from MQTT connection.");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                // System.out.println("Client machine delivered message to Broker.");
            }
        });
    }

    void userInput() throws MqttException {

        System.out.println("\nReady for a query! ʕ•́ᴥ•̀ʔっ♡" +
                "\nAny field is optional (hit enter to skip)");

        Scanner in = new Scanner(System.in);
        String firstName = "", lastName = "", address = "", salary = "", age = "";

        System.out.print("\nEnter a first name: ");
        firstName = in.nextLine();

        System.out.print("Enter a last name: ");
        lastName = in.nextLine();

        System.out.print("Enter an address name: ");
        address = in.nextLine();

        System.out.print("Enter a salary: ");
        salary = in.nextLine();

        System.out.print("Enter their age: ");
        age = in.nextLine();

        String[] row = {firstName, lastName, address, salary, age};

        boolean atLeastone = false;
        for (String queryField : row) {
            if (queryField != null & !queryField.trim().equals("")) {
                atLeastone = true;
            }
        }
        if (!atLeastone) {
            System.err.println("Please enter at least one value!!!");
            userInput();
            return;
        }

        sendMessageToPeerMachines(row);
    }

    // Method to send a row (row) to the PeerMachines over MQTT.
    public void sendMessageToPeerMachines(String[] row) throws MqttException {
        MqttMessage mqttMessage;

        Message msg = new Message(App.type.CL_2_PM, null, row, null);
        gson = new Gson();
        String toSend = gson.toJson(msg);

        // publish to both peer machines
        mqttMessage = new MqttMessage(toSend.getBytes());
        mqttMessage.setQos(1);
        mqttClient.publish("/CS341FinalProj/Team2/FromClient", mqttMessage);
        numberOfResponses = 0;

        // Print the divider before the result table.
        System.out.println();
        System.out.print(fancyDivider);
        System.out.println();
    }

    // Method that prints the rows contained within the results List parameter.
    // Note that the rows should have an extra column - the worker machine name.
    public static void printTable(List<String[]> results) {

        String[] headings = {"First Name", "Last Name", "Address", "Salary", "Age", "Worker"};
        int[] spacesReserved = {15, 20, 50, 6, 3, 6};
        String[] lines = new String[headings.length];
        for (int i = 0; i < headings.length; i++) {
            lines[i] = "═".repeat(spacesReserved[i]);
        }

        // Print the top line
        System.out.println(
                getRowFormatted(lines, spacesReserved, "╔", "╦", "╗", "═")
        );

        // Print the headings
        System.out.println(
                getRowFormatted(headings, spacesReserved, "║", "║", "║", " ")
        );

        // Print the line below headings
        String midSymbol = "╬";
        if (results.size() == 0)
            midSymbol = "╩";
        System.out.println(
                getRowFormatted(lines, spacesReserved, "╠", midSymbol, "╣", "═")
        );

        // Print the rows
        for (String[] row : results) {
            System.out.println(
                    getRowFormatted(row, spacesReserved, "║", "║", "║", " ")
            );
        }
        // If there were no rows to print
        if (results.size() == 0) {
            String noResStr = "No results found!";
            // 119 total width - noResStr.length() - 4
            int sides = (119 - noResStr.length() - 2) / 2;
            System.out.println("║" + " ".repeat(sides) + noResStr + " ".repeat(sides) + "║");
        }

        // Print the bottom line
        midSymbol = "╩";
        if (results.size() == 0)
            midSymbol = "═";
        System.out.println(
                getRowFormatted(lines, spacesReserved, "╚", midSymbol, "╝", "═")
        );

    }

    // Returns string that containts what to print for one row of a table.
    // Example of row[]: {"arry", "potta", "address1 address2", "12000", "32"}
    //      widths[]: {15, 20, 50, 6, 3, 6}
    //      startSymbol: "╔"
    //      midSymbol: "╦"
    //      endSymbol: "╗"
    //      padSymbol: " "/"="
    // The widths arrays length will be used to determine how many columns there should be.
    public static String getRowFormatted(String[] row, int[] widths,
                                         String startSymbol, String midSymbol,
                                         String endSymbol, String padSymbol) {
        String toReturn = startSymbol;
        int nCols = widths.length;

        for (int i = 0; i < nCols; i++) {

            // Trim the value at this column to fit the table
            int maxW = widths[i];
            String col = row[i];
            if (maxW < col.length())
                col = col.substring(0, maxW);

            String columnEndSymbol = midSymbol;
            if (i == nCols - 1)
                columnEndSymbol = endSymbol;

            toReturn += String.format(padSymbol + "%-" + widths[i] + "s" + padSymbol + columnEndSymbol, col);
        }

        return toReturn;
    }
}
