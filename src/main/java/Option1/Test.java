package Option1;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Test {

    public static void main(String[] args) throws IOException {

        // Test query
        String fname_q = "Harry";
        // String lname_q = "Potter";
        String lname_q = null;
        String address_q = null;
        String salary_q = null;
        String age_q = "30";
        String[] queryValues = {fname_q, lname_q, address_q, salary_q, age_q};

        boolean[] vals = new boolean[5];    // false by default

        for (int i = 0; i < queryValues.length; i++) {
            if(queryValues[i] != null)
                vals[i] = true;
        }

        int firstIndex = -1;
        List<String[]> personList = new ArrayList();
        for (int i = 0; i < vals.length; i++) {
            if(vals[i]){
                firstIndex = i;
            }
        }

        // go through csv and find all with index firstIndex value matching that value
        String fileName = "./src/main/Option1/PM1/PM1_File1" +".csv";
        FileReader filereader = new FileReader(fileName);
        CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
        List<String[]> allData = csvReader.readAll();

        for (String[] row : allData) {
            if(row[firstIndex].toLowerCase().contains(queryValues[firstIndex].toLowerCase())){
                personList.add(row);
            }
        }

        for (int i = firstIndex+1; i < 5; i++) {
            for(String[] row: personList){
                if(vals[i] == true && !row[i].toLowerCase().contains(queryValues[i].toLowerCase())){
                    personList.remove(row);
                }
            }
        }

        // return personList.size();
    }
}
