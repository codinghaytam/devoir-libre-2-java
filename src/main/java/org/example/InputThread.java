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
        File inputFolder = new File("src/main/resources/input");
        File outputFolder = new File("src/main/resources/output");
        File errorFolder = new File("src/main/resources/error");
        List<Order> orderlist = new ArrayList<>();


        if (!inputFolder.exists() || !inputFolder.isDirectory()) {
            System.out.println("Input folder does not exist or is not a folder");
            return;
        }

        if(!outputFolder.exists() || !outputFolder.isDirectory()) {
            System.out.println("Output folder does not exist or is not a folder");
            return;
        }
        if(!errorFolder.exists() || !errorFolder.isDirectory()) {
            System.out.println("Error folder does not exist or is not a folder");
            return;
        }

        while (true) {
            try {
                String iterator;
                File[] inputFiles = inputFolder.listFiles();
                boolean error = false;
                System.out.println(inputFiles.length);
                if (inputFiles != null) {
                    for (File file : inputFiles) {
                        File oFile = new File("src/main/resources/output/"+System.currentTimeMillis()+".json");
                        File eFile = new File("src/main/resources/error/"+System.currentTimeMillis()+".json");
                        outputfile = new BufferedWriter(new FileWriter(oFile, true));
                        errorfile = new BufferedWriter(new FileWriter(eFile, true));
                        inputfile = new BufferedReader(new FileReader(file));
                        System.out.println("Reading file: "+file.getName());
                        // Open the output and error files for appending
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
                                    outputfile.append(line);
                                    outputfile.append("\n");

                                } else {
                                    errorfile.append(line);
                                    errorfile.append("\n");
                                    error = true;

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                errorfile.append(line);
                                errorfile.append("\n");
                                error = true;
                            }
                        }
                        // Clear the input file content (if needed)
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                        writer.write(""); // Clear the content of the input file
                        writer.flush();
                        writer.close();
                        // Flush and close the output and error files
                        outputfile.flush();
                        errorfile.flush();
                        outputfile.close();
                        errorfile.close();

                        file.delete();
                        if(error) {
                            oFile.delete();
                        }else{
                            eFile.delete();
                        }
                    }
                }
                // Sleep for 1 second before processing again
                sleep(1000*3600);
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
