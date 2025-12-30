/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.managers;

import com.mycompany.managers.*;
import com.mycompany.auxiliary.*;
import com.mycompany.models.*;
import com.mycompany.gui.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author USER
 */
public class TransactionManager 
{
    private final Map<String, String> deptFiles = new LinkedHashMap<>();
    
    //load all transactions
    public List<Transaction> loadAllTransactions() throws IOException
    {
        List<Transaction> transactions = new ArrayList<>();

        transactions.addAll(loadRequestTransactions());
        transactions.addAll(loadDeliveryTransactions());

        return transactions;
    }

    //load request transactions
    private List<Transaction> loadRequestTransactions() throws FileNotFoundException, IOException 
    {
        List<Transaction> list = new ArrayList<>();

        //for each entry with dept name and its file path
        for(Map.Entry<String, String> entry : getDeptFiles().entrySet())
        {
            String dept = entry.getKey();
            String filePath = entry.getValue();

            File file = new File(filePath);
            if(!file.exists()) continue;

            //read the line
            try(BufferedReader br = new BufferedReader(new FileReader(file)))
            {
                String line;

                //read line, and get each part of the line
                while((line = br.readLine()) != null)
                {
                    String[] parts = line.split(",", -1);
                    if(parts.length < 7) continue;

                    String transactionId = parts[0];
                    String dateRequested = parts[1];
                    String userName = parts[2];
                    String itemCode = parts[3];
                    String itemName = parts[4];
                    int quantity = Integer.parseInt(parts[5]);
                    String unit = parts[6];
                    String status = parts[7];
                    LocalDate dateApproved = null;

                    if (parts.length >= 9 && !parts[8].isEmpty()) {
                        dateApproved = tryParseDate(parts[8]);
                    }
                    String releasedBy = null;

                    LocalDate dateReq = tryParseDate(dateRequested);
                    LocalDate dateCompleted = null;
                    if (parts.length >= 11 && !parts[9].isEmpty()) {
                        dateCompleted = tryParseDate(parts[9]);
                        releasedBy = parts[10];
                    }
                    int quantityToBeReleased = 0;
                    if (parts.length >= 12 && !parts[11].isEmpty()) {
                        quantityToBeReleased = Integer.parseInt(parts[11]);
                    }

                    //instantiate Transaction class
                    Transaction transaction = new Transaction(transactionId, TransactionType.REQUEST, userName, itemCode, itemName,
                        quantity, quantityToBeReleased,unit, dept, releasedBy, status, dateReq, dateApproved, dateCompleted);

                    list.add(transaction);
                }   
            }
        }
        return list;
    }

    //load delivery tranactions
    private List<Transaction> loadDeliveryTransactions() throws IOException {

        List<Transaction> list = new ArrayList<>();

        //read each line through the file, and store in the String array
        try (BufferedReader br = new BufferedReader(new FileReader("data/deliveries.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");

                if (p.length < 8) continue;

                Transaction t = new Transaction(
                    p[0],                        // deliveryId (DEL...)
                    TransactionType.DELIVERY,   // type
                    p[1],
                    p[2],                        // itemCode
                    p[3],                        // itemName
                    Integer.parseInt(p[4]),      // quantity
                    0,
                    p[5],                        // unit
                    p[8],                        // supplier (source)
                    p[9],
                    "",                          // status (not applicable)
                    tryParseDate(p[6]),          // dateDelivered
                    null,
                    tryParseDate(p[7])           // dateCompleted
                );

                // VERY IMPORTANT: store who created/received it
                t.setUserName(p[1]);             // admin username

                list.add(t);
            }
        }

        return list;
    }

    //store the key value pair of the each department with the file paths of their txt file
    public Map<String, String> getDeptFiles()
    {
        deptFiles.put("Engineering", "data/engineering_dept_requests.txt");
        deptFiles.put("Financial", "data/financial_dept_requests.txt");
        deptFiles.put("HR", "data/human_resource_dept_requests.txt");
        deptFiles.put("IT", "data/it_dept_requests.txt");
        deptFiles.put("Logistics", "data/logistics_dept_requests.txt");
        deptFiles.put("LCC Ayala", "data/lcc_ayala_branch_requests.txt");
        deptFiles.put("LCC Daraga", "data/lcc_daraga_branch_requests.txt");
        return deptFiles;
    }

    //try parse the String to the DateTimeFormatter BASIC_ISO_DATE
    private LocalDate tryParseDate(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        s = s.trim();
        // try ISO_LOCAL_DATE first (yyyy-MM-dd)
        try {
            return LocalDate.parse(s);
        } catch (Exception e) {
            // try BASIC_ISO_DATE (yyyyMMdd)
            try {
                DateTimeFormatter f = DateTimeFormatter.BASIC_ISO_DATE;
                return LocalDate.parse(s, f);
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
