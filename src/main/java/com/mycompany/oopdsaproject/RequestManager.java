/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oopdsaproject;

import java.util.ArrayList;
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
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class RequestManager {

    public boolean saveRequests(JTable table, String department, String role, String filePath, User loginUser)
    {
        boolean savedRequest = false;
        if(table.isEditing())
        {
            table.getCellEditor().stopCellEditing();
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String requestId = generateRequestID(department, filePath);

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

                if(quantity > 0)
                {
                    savedRequest = true;
                    String itemCode = model.getValueAt(i, 0).toString();
                    String itemName = model.getValueAt(i, 1).toString();
                    String unit = model.getValueAt(i, 3).toString();

                    String status = role.equalsIgnoreCase("staff")
                        ? "Pending" : "Approved";

                    String line = String.join(",", requestId, date, itemCode,
                            itemName, String.valueOf(quantity), unit, status);

                    bw.write(line);
                    bw.newLine();
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

    public String generateRequestID(String department, String filePath)
    {
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        int counter = 1;

        File file = new File(filePath);
        if(file.exists())
        {
            try (BufferedReader br = new BufferedReader(new FileReader(file)))
            {
                String line;
                while((line = br.readLine()) != null)
                {
                    String[] parts = line.split(",");
                    if(parts.length > 0 && parts[0].startsWith("REQ" + date))
                    {
                        counter++;
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        String counterPart = String.format("%03d", counter);

        return "REQ" + date + "-" + counterPart;
    }


    public Map<String, List<Request>> loadAllRequests(String filePath) throws IOException
    {
        Map<String, List<Request>> requestsMap = new LinkedHashMap<>();

        try(BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while((line = br.readLine()) != null)
            {
                String[] parts = line.split(",");

                if(parts.length != 7) continue;

                String requestId = parts[0];
                String date = parts[1];
                String itemCode = parts[2];
                String itemName = parts[3];
                int quantity = Integer.parseInt(parts[4]);
                String unit = parts[5];
                String status = parts[6];

                Request request = new Request(requestId, itemCode, itemName, date, quantity, unit, status);

                requestsMap.putIfAbsent(requestId, new ArrayList<>());
                requestsMap.get(requestId).add(request);
            }
        }

        return requestsMap;
    }

    //to update if the requested item is either approved or rejected
    public void updateRequestStatus(String filePath, Request updatedRequest) throws IOException
    {
        List<String> lines = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            String line;

            while((line = br.readLine()) != null)
            {
                String[] parts = line.split(",");
                if(parts.length != 7) continue;

                String requestId = parts[0];
                String itemCode = parts[2];

                //if matches change status
                if(requestId.equals(updatedRequest.getRequestId()) && itemCode.equals(updatedRequest.getItemCode()))
                {
                    parts[1] = updatedRequest.getDateCreated();
                    parts[4] = String.valueOf(updatedRequest.getQuantity());
                    parts[6] = updatedRequest.getStatus();
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
