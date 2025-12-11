/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oopdsaproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class RequestManager {
    public static void saveRequest(String dept, Request request)
    {
        String fileNameDept = toFileSafeName(dept);
        String path = "database/" + fileNameDept + "_requests.txt";

        StringBuilder itemsString = new StringBuilder();
        for(RequestItem item : request.getItems())
        {
            itemsString.append(item.getItemName())
                    .append(":")
                    .append(item.getQuantity())
                    .append(";");
        }

        try(FileWriter fw = new FileWriter(path, true))
        {
            fw.write(request.getRequestId() + "," +
                     request.getDepartment() + "," +
                     request.getStatus() + "," +
                     request.getDateCreated() + "," +
                     itemsString.toString() +
                     "\n");
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static String toFileSafeName(String dept)
    {
        return dept.toLowerCase().replace(" ", "_");
    }

    public void saveRequests(JTable table, String department, String role, String filePath)
    {
        if(table.isEditing())
        {
            table.getCellEditor().stopCellEditing();
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
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
            JOptionPane.showMessageDialog(null, "Requests saved successfully for " + department + ".");
        } catch (IOException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving requests: " + e.getMessage());
        }
    }

    public String generateRequestID(String department, String filePath)
    {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

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
}
