/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oopdsaproject;

import java.time.LocalDate;

/**
 *
 * @author USER
 */
public class Delivery {
    private String itemCode;
    private String itemName;
    private int quantity;        // Current inventory quantity
    private String unit;
    private int deliverQuantity; // Quantity to deliver
    private LocalDate lastRestockDate;


    public Delivery(String itemCode, String itemName, int quantity, String unit, LocalDate lastRestockDate) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unit = unit;
        this.deliverQuantity = 0;
        this.lastRestockDate = lastRestockDate;
    }

    // Getters and setters
    public String getItemCode() { return itemCode; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public int getDeliverQuantity() { return deliverQuantity; }
    public void setDeliverQuantity(int deliverQuantity) { this.deliverQuantity = deliverQuantity; }
    public LocalDate getLastRestockDate() { return lastRestockDate; }
}

