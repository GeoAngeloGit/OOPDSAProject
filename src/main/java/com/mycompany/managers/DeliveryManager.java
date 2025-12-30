/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.managers;

import com.mycompany.managers.*;
import com.mycompany.auxiliary.*;
import com.mycompany.models.*;
import com.mycompany.gui.*;

/**
 *
 * @author USER
 */
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DeliveryManager {
    private List<Delivery> deliveryItems;
    private List<DeliveryRecord> deliveryHistory;

    public DeliveryManager() {
        deliveryItems = new ArrayList<>();
        deliveryHistory = new ArrayList<>();
    }

    // Load delivery inventory from file
    public void loadDeliveryInventory() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/Inventory.txt"))) {
            String line;
            // use same inventory path as other managers
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // expect: itemCode,itemName,lastRestockDate,quantity,unit
                if(parts.length >= 5) {
                    String code = parts[0];
                    String name = parts[1];
                    LocalDate date = LocalDate.parse(parts[2]);
                    int qty = Integer.parseInt(parts[3]);
                    String unit = parts[4];
                    deliveryItems.add(new Delivery(code, name, qty, unit, date));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Delivery> getDeliveryItems() {
        return deliveryItems;
    }

    // Process a delivery for an item
    public boolean processDelivery(String itemCode, int deliverQty) throws IOException {
        for (Delivery item : deliveryItems) {
            if (item.getItemCode().equals(itemCode)) {
                // deliverQty should be added to inventory whenever positive
                if (deliverQty > 0) {
                    item.setQuantity(item.getQuantity() + deliverQty);
                    item.setDeliverQuantity(deliverQty);
                    saveDeliveryRecords(); // Save changes to the file
                    updateInventoryFile(item);
                    return true;
                }
                return false; // invalid delivered qty
            }
        }
        return false; // Item not found
    }

    public boolean saveDeliveries(JTable table, String supplierName, String supplierStaffName, String filePath, User loginUser)
    {
        boolean savedDelivery = false;
        if(table.isEditing())
        {
            table.getCellEditor().stopCellEditing();
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String dateCompleted = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        // generate one delivery ID for this batch (all items in this save call share the same ID)
        String deliveryId = generateDeliveryID(filePath);

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true)))
        {
            for (int i = 0; i < model.getRowCount(); i++) {
                Object qtyObj = model.getValueAt(i, 4);
                int deliveredQty = (qtyObj instanceof Number) ? ((Number) qtyObj).intValue() : 0;

                if (deliveredQty > 0) {
                    savedDelivery = true;
                    String itemCode = model.getValueAt(i, 0).toString();
                    String itemName = model.getValueAt(i, 1).toString();
                    String unit = model.getValueAt(i, 3).toString();

                    processDelivery(itemCode, deliveredQty);

                    String line = String.join(",", deliveryId, loginUser.getUsername(), itemCode, itemName,
                                            String.valueOf(deliveredQty), unit, date, dateCompleted, supplierName, supplierStaffName);

                    bw.write(line);
                    bw.newLine();
                }
            }

            bw.flush();

            return savedDelivery;
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    // Save delivery history to file
    private void saveDeliveryRecords() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/deliveries.txt", true))) {
            for (DeliveryRecord record : deliveryHistory) {
                bw.write(record.toCSV());
                bw.newLine();
            }
            deliveryHistory.clear(); // optional: clear after saving
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateInventoryFile(Delivery deliveredItem) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data/Inventory.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 5) continue; // itemCode,itemName,lastRestockDate,quantity,unit

                if (parts[0].equals(deliveredItem.getItemCode())) {
                    int currentQty = Integer.parseInt(parts[3]);
                    int newQty = currentQty + deliveredItem.getDeliverQuantity();
                    parts[2] = LocalDate.now().toString(); // update last delivery/restock date
                    parts[3] = String.valueOf(newQty);
                }

                lines.add(String.join(",", parts));
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/Inventory.txt"))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        }
    }



    public String generateDeliveryID(String filePath)
    {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        int counter = 1;

        File file = new File(filePath);
        if (file.exists()) {
            Set<String> existingRequestIds = new HashSet<>();

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        String requestId = parts[0];
                        // Only count request IDs starting with today's date
                        if (requestId.startsWith("DEL" + date)) {
                            existingRequestIds.add(requestId);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            counter = existingRequestIds.size() + 1; // next request number
        }

        String counterPart = String.format("%03d", counter);
        return "DEL" + date + "-" + counterPart;
    }

    /**
     * Returns the next counter number for today's deliveries based on existing file entries.
     * This does not write to the file; caller should use the returned value and increment locally
     * when creating multiple delivery IDs in the same operation.
     */
    /*public int getNextDeliveryCounter(String filePath) {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        int counter = 1;

        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0 && parts[0].startsWith("DEL" + date)) {
                        counter++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return counter;
    }*/

    public void addNewItem(String itemCode, String itemName, int quantity, String unit, String filePath, JPanel panel) {
        List<Delivery> allItems = new ArrayList<>();

        boolean itemExists = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length >= 5) {
                    String code = parts[0];
                    String name = parts[1];
                    LocalDate lastRestockDate = LocalDate.parse(parts[2]);
                    int qty = Integer.parseInt(parts[3]);
                    String u = parts[4];


                    if(code.equalsIgnoreCase(itemCode)) {
                        itemExists = true;
                    }

                    allItems.add(new Delivery(code, name, qty, u, lastRestockDate));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(itemExists) {
            JOptionPane.showMessageDialog(null, "Item Code already exists in the inventory!", 
                                        "Duplicate Item", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2️⃣ Add the new item
        Delivery newItem = new Delivery(itemCode, itemName, quantity, unit, LocalDate.now());
        allItems.add(newItem);

        // 3️⃣ Sort alphabetically by itemName
        allItems.sort(Comparator.comparing(Delivery::getItemName, String.CASE_INSENSITIVE_ORDER));

        // 4️⃣ Save back to inventory file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Delivery item : allItems) {
                String line = String.join(",",
                        item.getItemCode(),
                        item.getItemName(),
                        item.getLastRestockDate().toString(),
                        String.valueOf(item.getQuantity()),
                        item.getUnit()
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(panel, "New item added successfully!", 
                                    "Item Added", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Adds a new item to inventory and records an initial delivery transaction
     * with the provided supplier name. Appends a delivery record to the deliveries file.
     */
    public boolean addNewItemWithDelivery(String itemCode, String itemName, int quantity, String unit, String filePath, JPanel panel, String supplierName, String deliveriesFilePath) {
        // reuse addNewItem logic but also write a delivery record
        addNewItem(itemCode, itemName, quantity, unit, filePath, panel);

        // create delivery record
        String deliveryId = generateDeliveryID(deliveriesFilePath);
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String dateCompleted = date;

        String line = String.join(",",
                deliveryId,
                itemCode,
                itemName,
                String.valueOf(quantity),
                unit,
                date,
                dateCompleted,
                supplierName == null ? "" : supplierName
        );

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(deliveriesFilePath, true))) {
            bw.write(line);
            bw.newLine();
            bw.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}

