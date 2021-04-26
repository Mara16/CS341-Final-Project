package Option1;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Test {

    public static void main(String[] args) throws IOException {

        String[] row = {"arry", "potta", "address address address address address address address address address " +
                "address ", "salary example", "age example"};
        String[] rowWithWorkerName = new String[App.NUM_COLUMNS + 1];
        rowWithWorkerName[App.NUM_COLUMNS] = "PM1_M3";
        System.arraycopy(row, 0,
                rowWithWorkerName, 0, App.NUM_COLUMNS);
        System.out.println(Arrays.toString(rowWithWorkerName));
        System.out.println();
        String[] r = {"First Name", "Last Name", "Address", "Salary", "Age", "Worker"};

        int[] spacesReserved = {15, 20, 50, 6, 3, 6};
        // Total width = 2 + 15 + 3 + 20 + 3 + 50 + 3 + 6 + 3 +  3 + 3 + 6 + 2 = 119
        // stars and stuff at center = 23
        // 119 - 23 -2 = 94
        // 47 chars on each side

        String test_ = "╔═══ -ˋˏ *.·:·.◆.·:·.* ˎˊ- ═══╗\n" +
                "╚═══ -ˋˏ *.·:·.◆.·:·.* ˎˊ- ═══╝";
        String dpipe = "║";

        // printing outer line
        System.out.printf(String.format("╔%047d -ˋˏ *.·:·.◆.·:·.* ˎˊ- %047d╗\n", 0, 0).replace("0","═"));

        //-----------vv Print one row vv-------------
        System.out.print("║");
        for (int i = 0; i < App.NUM_COLUMNS + 1; i++) {
            int maxW = r[i].length();
            if (maxW > spacesReserved[i])
                maxW = spacesReserved[i];
            System.out.printf(" %-" + spacesReserved[i] + "s ║", r[i].substring(0, maxW));
        }
        System.out.println();
        //-----------^^ Print one row ^^-------------

        System.out.printf(String.format("╚%047d -ˋˏ *.·:·.◆.·:·.* ˎˊ- %047d╝\n", 0, 0).replace("0","═"));

        /*
        What it could look like:

        With thingies:
        ╔══════════════════╦══════════════════════╦═════ -ˋˏ *.·:·.◆.·:·.* ˎˊ- ════════════════════════╦════════╦════╦════════╗
        ║ First Name       ║ Last Name            ║ Address                                            ║ Salary ║ Ag ║ Worker ║
        ╠══════════════════╬══════════════════════╬════════════════════════════════════════════════════╬════════╬════╬════════╣
        ║ Sebin            ║ Suresh               ║  1231 America lane, US                             ║ 100000 ║    ║        ║
        ║ Obusmara         ║ Ulloa                ║                                                    ║        ║    ║        ║
        ║ Ahmed            ║ Khaled               ║                                                    ║        ║    ║        ║
        ╚══════════════════╩══════════════════════╩═════ -ˋˏ *.·:·.◆.·:·.* ˎˊ- ════════════════════════╩════════╩════╩════════╝

        Without thingies:
        ╔══════════════════╦══════════════════════╦════════════════════════════════════════════════════╦════════╦════╦════════╗
        ║ First Name       ║ Last Name            ║ Address                                            ║ Salary ║ Ag ║ Worker ║
        ╠══════════════════╬══════════════════════╬════════════════════════════════════════════════════╬════════╬════╬════════╣
        ║ Sebin            ║ Suresh               ║  1231 America lane, US                             ║ 100000 ║    ║        ║
        ║ Obusmara         ║ Ulloa                ║                                                    ║        ║    ║        ║
        ║ Ahmed            ║ Khaled               ║                                                    ║        ║    ║        ║
        ╚══════════════════╩══════════════════════╩════════════════════════════════════════════════════╩════════╩════╩════════╝


        */

        // System.out.printf(String.format("+%46s\n", "+").replace(" ","-"));
        // System.out.printf("| %-20s | %-20s |\n", row[0], row[1]);


        /*String t = "abcdef";
        System.out.println((t).substring(t.length() - 1));*/
        /*// Test query
        String fname_q = "Harry";
        // String lname_q = "Potter";
        String lname_q = null;
        String address_q = null;
        String salary_q = null;
        String age_q = "30";
        String[] queryValues = {fname_q, lname_q, address_q, salary_q, age_q};

        boolean[] vals = new boolean[5];    // false by default

        for (int i = 0; i < queryValues.length; i++) {
            if (queryValues[i] != null)
                vals[i] = true;
        }

        int firstIndex = -1;
        List<String[]> personList = new ArrayList();
        for (int i = 0; i < vals.length; i++) {
            if (vals[i]) {
                firstIndex = i;
            }
        }

        // go through csv and find all with index firstIndex value matching that value
        String fileName = "./src/main/Option1/PM1/PM1_File1" + ".csv";
        FileReader filereader = new FileReader(fileName);
        CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
        List<String[]> allData = csvReader.readAll();

        for (String[] row : allData) {
            if (row[firstIndex].toLowerCase().contains(queryValues[firstIndex].toLowerCase())) {
                personList.add(row);
            }
        }

        for (int i = firstIndex + 1; i < 5; i++) {
            for (String[] row : personList) {
                if (vals[i] == true && !row[i].toLowerCase().contains(queryValues[i].toLowerCase())) {
                    personList.remove(row);
                }
            }
        }*/

        // return personList.size();
    }
}
