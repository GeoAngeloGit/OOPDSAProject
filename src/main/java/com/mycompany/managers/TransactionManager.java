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
    
    public List<Transaction> loadAllTransactions() throws IOException
    {
        List<Transaction> transactions = new ArrayList<>();

        transactions.addAll(loadRequestTransactions());
        transactions.addAll(loadDeliveryTransactions());

        return transactions;
    }

    private List<Transaction> loadRequestTransactions() throws FileNotFoundException, IOException 
    {
        // TODO Auto-generated method stub
        List<Transaction> list = new ArrayList<>();

        for(Map.Entry<String, String> entry : getDeptFiles().entrySet())
        {
            String dept = entry.getKey();
            String filePath = entry.getValue();

            File file = new File(filePath);
            if(!file.exists()) continue;

            try(BufferedReader br = new BufferedReader(new FileReader(file)))
            {
                String line;
                while((line = br.readLine()) != null)
                {
                    String[] parts = line.split(",", -1);
                    if(parts.length < 7) continue;

                    String transactionId = parts[0];
                    String dateRequested = parts[1];
                    String itemCode = parts[2];
                    String itemName = parts[3];
                    int quantity = Integer.parseInt(parts[4]);
                    String unit = parts[5];
                    String status = parts[6];

                    LocalDate dateReq = tryParseDate(dateRequested);
                    LocalDate dateCompleted = null;
                    if (parts.length >= 8 && !parts[7].isEmpty()) {
                        dateCompleted = tryParseDate(parts[7]);
                    }

                    Transaction transaction = new Transaction(transactionId, TransactionType.REQUEST, itemCode, itemName,
                        quantity, unit, dept, status, dateReq, dateCompleted);

                    list.add(transaction);
                }   
            }
        }
        return list;
    }

    private List<Transaction> loadDeliveryTransactions() throws IOException {

        List<Transaction> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data/deliveries.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");

                if (p.length < 8) continue;

                Transaction t = new Transaction(
                    p[0], // deliveryId
                    TransactionType.DELIVERY,
                    p[1], // itemCode
                    p[2], // itemName
                    Integer.parseInt(p[3]),
                    p[4], // unit
                    p[7], // supplier
                "", // status (deliveries have no status)
                tryParseDate(p[5]), // dateDelivered
                tryParseDate(p[6])  // dateCompleted
                );

                list.add(t);
            }
        }

        return list;
    }

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
