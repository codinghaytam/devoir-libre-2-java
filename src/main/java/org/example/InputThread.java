package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InputThread extends Thread {
    @Override
    public void run() {

        BufferedWriter outputfile=null;
        BufferedWriter errorfile=null;
        BufferedReader inputfile=null;


        try {
            outputfile =new BufferedWriter(new FileWriter("src/main/resources/output.json",true));
            errorfile =new BufferedWriter(new FileWriter("src/main/resources/error.json",true));
            inputfile =new BufferedReader(new FileReader("src/main/resources/input.json"));
            List<Order> orderlist = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();

        }

        while (true) {
            try {
                String iterator;
                // Open the output and error files for appending
                outputfile = new BufferedWriter(new FileWriter("src/main/resources/output.json", true));
                errorfile = new BufferedWriter(new FileWriter("src/main/resources/error.json", true));

                // Read the entire input file into memory
                inputfile = new BufferedReader(new FileReader("src/main/resources/input.json"));
                List<String> lines = new ArrayList<>();
                while ((iterator = inputfile.readLine()) != null) {
                    lines.add(iterator);
                }
                inputfile.close(); // Close the reader after reading

                // Process each line
                for (String line : lines) {
                    try {
                        Order order = new JsonParser().JsonToOrder(line);
                        OrderDaoImpl orderDao = new OrderDaoImpl();
                        CustomerDaoImpl customerDao = new CustomerDaoImpl();
                        if (customerDao.getCustomer(order.getCustomer().getId()) != null) {
                            orderDao.AddOrder(order);
                            outputfile.append(new Timestamp(System.currentTimeMillis()) + " : " + line);
                            outputfile.append("\n");

                        } else {
                            errorfile.append(new Timestamp(System.currentTimeMillis()) + " : " + line);
                            errorfile.append("\n");

                        }
                    } catch (Exception e) {
                        errorfile.append(new Timestamp(System.currentTimeMillis()) + " : " + line);
                        errorfile.append("\n");
                    }
                }

                // Clear the input file content (if needed)
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/main/resources/input.json"));
                writer.write(""); // Clear the content of the input file
                writer.flush();
                writer.close();
                // Flush and close the output and error files
                outputfile.flush();
                errorfile.flush();
                outputfile.close();
                errorfile.close();

                // Sleep for 1 second before processing again
                sleep(1000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();

                try {
                    if (outputfile != null) outputfile.close();
                    if (errorfile != null) errorfile.close();
                    if (inputfile != null) inputfile.close();
                } catch (IOException closeException) {
                    closeException.printStackTrace();
                }
            }
        }
    }
}
