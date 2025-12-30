/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.managers;

import com.mycompany.managers.*;
import com.mycompany.auxiliary.*;
import com.mycompany.models.*;
import com.mycompany.gui.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class RequestManager {

    //check if the request is successfully saved
    public boolean saveRequests(JTable table, String department, String role, String filePath, User loginUser)
    {
        boolean savedRequest = false;
        if(table.isEditing())
        {
            table.getCellEditor().stopCellEditing();
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        //get the date and requestId
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String requestId = generateRequestID(department, filePath);

        //write the filepath with the requests
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true)))
        {
            for(int i = 0; i < model.getRowCount(); i++)
            {
                Object quantityObj = model.getValueAt(i, 4);
                int quantity = 0;

                if(quantityObj instanceof Number)
                {
                    quantity = ((Number) quantityObj).intValue();
                }

                //if quantity > 0 save the request in the file
                if(quantity > 0)
                {
                    savedRequest = true;
                    String itemCode = model.getValueAt(i, 0).toString();
                    String itemName = model.getValueAt(i, 1).toString();
                    String unit = model.getValueAt(i, 3).toString();

                    String status = role.equalsIgnoreCase("staff")
                        ? "Pending" : "Approved";

                    if(role.equalsIgnoreCase("head"))
                    {
                        String dateApproved = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

                        String line = String.join(",", requestId, date, loginUser.getUsername(), itemCode,
                            itemName, String.valueOf(quantity), unit, status, dateApproved);
                        
                        bw.write(line);
                        bw.newLine();
                    }
                    else
                    {
                        String line = String.join(",", requestId, date, loginUser.getUsername(), itemCode,
                            itemName, String.valueOf(quantity), unit, status);
                        
                        bw.write(line);
                        bw.newLine();
                    }
                }
            }

            bw.flush();

            return savedRequest;
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

//    public String generateRequestID(String department, String filePath)
//    {
//        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
//
//        int counter = 1;
//
//        File file = new File(filePath);
//        if(file.exists())
//        {
//            try (BufferedReader br = new BufferedReader(new FileReader(file)))
//            {
//                String line;
//                while((line = br.readLine()) != null)
//                {
//                    String[] parts = line.split(",");
//                    if(parts.length > 0 && parts[0].startsWith("REQ" + date))
//                    {
//                        counter++;
//                    }
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//            }
//        }
//
//        String counterPart = String.format("%03d", counter);
//
//        return "REQ" + date + "-" + counterPart;
//    }

    //generate id to have unique id, using the date
    public String generateRequestID(String department, String filePath) {
        //get the date today for the requestId
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        int counter = 1;

        File file = new File(filePath);
        if (file.exists()) {
            Set<String> existingRequestIds = new HashSet<>();

            //read the file go get the next number of the requestId
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        String requestId = parts[0];
                        // Only count request IDs starting with today's date
                        if (requestId.startsWith("REQ" + date)) {
                            existingRequestIds.add(requestId);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // next request number
            counter = existingRequestIds.size() + 1; 
        }

        //for the String requestId REQyyyyMMdd-counter
        String counterPart = String.format("%03d", counter);
        return "REQ" + date + "-" + counterPart;
    }

    //load all requests
    public Map<String, List<Request>> loadAllRequests(String filePath) throws IOException
    {
        //set the requests map with String requestId, and list of request
        Map<String, List<Request>> requestsMap = new LinkedHashMap<>();

        //read the file of request of the department 
        try(BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            String line;

            //read each line
            while((line = br.readLine()) != null)
            {
                String[] parts = line.split(",");
                //accept both 7-field (no completion date) and 8-field (with completion date)
                if(parts.length < 8) continue;

                //declare each part of the file as follows
                String requestId = parts[0];
                LocalDate date = LocalDate.parse(parts[1]);
                String staffName = parts[2];
                String itemCode = parts[3];
                String itemName = parts[4];
                int quantity = Integer.parseInt(parts[5]);
                String unit = parts[6];
                String status = parts[7];
                LocalDate dateApproved = null;

                if(parts.length >= 9 && parts[8] != null && !parts[8].trim().isEmpty()) {
                    try {
                        dateApproved = LocalDate.parse(parts[8]);
                    } catch (Exception ex) {
                        // ignore parse errors and keep dateCompleted null
                    }
                }
                LocalDate dateCompleted = null;

                if(parts.length >= 10 && parts[9] != null && !parts[9].trim().isEmpty()) {
                    try {
                        dateCompleted = LocalDate.parse(parts[9]);
                    } catch (Exception ex) {
                        // ignore parse errors and keep dateCompleted null
                    }
                }
                int quantityToBeReleased = 0;
                if (parts.length >= 12 && !parts[11].isEmpty()) {
                    quantityToBeReleased = Integer.parseInt(parts[11]);
                }

                //call the Request Constructor with the following arguements
                Request request = new Request(requestId, staffName, itemCode, itemName, date, dateApproved, dateCompleted, quantity, quantityToBeReleased, unit, status);

                //add the Request request in the requestsMap to be returned
                requestsMap.putIfAbsent(requestId, new ArrayList<>());
                requestsMap.get(requestId).add(request);
            }
        }

        return requestsMap;
    }

    //to update if the requested item is either approved or rejected
    public void updateRequestStatus(String filePath, Request updatedRequest, User loginUser) throws IOException
    {
        List<String> lines = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            String line;

            while((line = br.readLine()) != null)
            {
                String[] parts = line.split(",");
                if(parts.length == 0) continue;

                String requestId = parts[0];
                String itemCode = parts[3];

                //if matches change status
                if(requestId.equals(updatedRequest.getRequestId()) && itemCode.equals(updatedRequest.getItemCode()))
                {
                    if(parts.length <= 9) parts = Arrays.copyOf(parts,12);
                    
                    parts[5] = String.valueOf(updatedRequest.getQuantity());
                    parts[7] = updatedRequest.getStatus();
                    parts[8] = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

                    //date completed
                    parts[9] = updatedRequest.getDateCompleted() != null 
                        ? updatedRequest.getDateCompleted().toString() : "";

                    parts[10] = updatedRequest.getDateCompleted() != null 
                        ? loginUser.getUsername().toString() : "";
                    
                    parts[11] = updatedRequest.getQuantityToBeReleased() != 0 
                        ? String.valueOf(updatedRequest.getQuantityToBeReleased()) : String.valueOf(0);
                }

                lines.add(String.join(",", parts));
            }
        }

        //rewrite the file
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath)))
        {
            for(String l : lines)
            {
                bw.write(l);
                bw.newLine();
            }
        }
    }

}
