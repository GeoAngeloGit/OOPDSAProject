package com.mycompany.oopdsaproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemsManager {
    private List<Items> itemList = new ArrayList<>();
    private String status;
    private static final String INVENTORY_PATH = "data/Inventory.txt";
    
    public void loadInventory()
    {
        try (BufferedReader br = new BufferedReader(new FileReader(INVENTORY_PATH))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String itemCode = parts[0];
                String itemName = parts[1];
                String dateOfLastRestock = parts[2];
                int quantity = Integer.parseInt(parts[3]);
                String unit = parts[4];

                if(quantity < 5)
                {
                    status = "Low Stock";
                }
                else
                {
                    status = "In Stock";
                }
                itemList.add(new Items(itemCode, itemName, dateOfLastRestock, quantity, unit, status));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Items> getItems()
    {
        return itemList;
    }

    public void deductInventory(Request completedRequest) throws IOException
    {
        List<String> items = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(INVENTORY_PATH)))
        {
            String line;

            while((line = br.readLine()) != null)
            {
                String[] parts = line.split(",");

                String itemCode = parts[0];
                int quantity = Integer.parseInt(parts[3]);

                if(itemCode.equals(completedRequest.getItemCode()))
                {
                    quantity -= completedRequest.getQuantity();
                    if(quantity < 0) quantity = 0;
                    parts[3] = String.valueOf(quantity);
                }

                items.add(String.join(",", parts));
            }
        }

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(INVENTORY_PATH)))
        {
            for(String l : items)
            {
                bw.write(l);
                bw.newLine();
            }
        }
    }
}
