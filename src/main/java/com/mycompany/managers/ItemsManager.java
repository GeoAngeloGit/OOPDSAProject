package com.mycompany.managers;

import com.mycompany.models.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class ItemsManager {
    //decalre the attributes
    private List<Items> itemList = new ArrayList<>();
    private String status;
    private static final String INVENTORY_PATH = "data/Inventory.txt";
    
    //to load the inventory 
    public void loadInventory()
    {
        //read the file
        try (BufferedReader br = new BufferedReader(new FileReader(INVENTORY_PATH))) {
            String line;

            //read the line, split and store each part
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String itemCode = parts[0];
                String itemName = parts[1];
                String dateOfLastRestock = parts[2];
                int quantity = Integer.parseInt(parts[3]);
                String unit = parts[4];

                //set the status depending on the quantity
                if(quantity == 0)
                {
                    status = "No Stock";
                }
                else if(quantity < 5)
                {
                    status = "Low Stock";
                }
                else
                {
                    status = "In Stock";
                }

                //add the details of the Items using the constructor
                itemList.add(new Items(itemCode, itemName, dateOfLastRestock, quantity, unit, status));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //to get the items
    public List<Items> getItems()
    {
        return itemList;
    }

    //to deduct the quantity of request items when the request is completed
    public int deductInventory(Request completedRequest) throws IOException
    {
        List<String> items = new ArrayList<>();
        int releasedQty = 0;

        //read the inventory file
        try(BufferedReader br = new BufferedReader(new FileReader(INVENTORY_PATH)))
        {
            String line;

            //read the line, and seperate per comma
            while((line = br.readLine()) != null)
            {
                String[] parts = line.split(",");

                String itemCode = parts[0];
                int availableQty = Integer.parseInt(parts[3]);

                //to check if the available quantity is sufficient for the reqeusted quantity
                if(itemCode.equals(completedRequest.getItemCode()))
                {
                    int requestedQty = completedRequest.getQuantity();

                    if(availableQty <= 0)
                    {
                        releasedQty = 0;
                    }
                    else if(requestedQty > availableQty)
                    {
                        releasedQty = availableQty;
                        availableQty = 0;
                    }
                    else
                    {
                        releasedQty = requestedQty;
                        availableQty -= requestedQty;
                    }

                    parts[3] = String.valueOf(availableQty);

                    completedRequest.setQuantity(releasedQty);
                }

                items.add(String.join(",", parts));
            }
        }

        //write the items
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(INVENTORY_PATH)))
        {
            for(String l : items)
            {
                bw.write(l);
                bw.newLine();
            }
        }

        return releasedQty;
    }

    //Update an item's code and/or name in the inventory file.
    //Matches by originalCode and replaces the code and name with provided values.
    public boolean updateItemCodeAndName(String originalCode, String newCode, String newName) {
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        //read the line and update the itemCode and itemName
        try (BufferedReader br = new BufferedReader(new FileReader(INVENTORY_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(originalCode)) {
                    parts[0] = newCode;
                    parts[1] = newName;
                    updated = true;
                }
                lines.add(String.join(",", parts));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!updated) return false;

        //write the new itemName and code
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(INVENTORY_PATH))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * check whether the provided code or name would duplicate another entry in the inventory.
     * the item identified by originalCode is ignored (so editing an item to keep its own values
     * is allowed).
     */
    public boolean isDuplicateCodeOrName(String codeToCheck, String nameToCheck, String originalCode) {
        try (BufferedReader br = new BufferedReader(new FileReader(INVENTORY_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;
                String code = parts[0].trim();
                String name = parts[1].trim();

                // skip the original record being edited
                if (code.equals(originalCode)) continue;

                if (!codeToCheck.equalsIgnoreCase("") && code.equalsIgnoreCase(codeToCheck)) return true;
                if (!nameToCheck.equalsIgnoreCase("") && name.equalsIgnoreCase(nameToCheck)) return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // on error, be conservative and report no duplicate so UI can attempt save and handle failures
            return false;
        }

        return false;
    }

    /**
     * Delete an item from the inventory file by its item code.
     */
    public boolean deleteItemByCode(String codeToDelete) {
        List<String> lines = new ArrayList<>();
        boolean removed = false;

        try (BufferedReader br = new BufferedReader(new FileReader(INVENTORY_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].trim().equals(codeToDelete)) {
                    removed = true;
                    // skip this line (delete)
                    continue; 
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!removed) return false;

        //write with the deleted item
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(INVENTORY_PATH))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
