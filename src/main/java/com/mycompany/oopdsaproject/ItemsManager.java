package com.mycompany.oopdsaproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ItemsManager {
    private List<Items> itemList = new ArrayList<>();
    private String status;
    
    public void loadInventory()
    {
        InputStream is = getClass().getResourceAsStream("/database/Inventory.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
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
}
