/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oopdsaproject;

/**
 *
 * @author USER
 */
import java.time.LocalDate;

public class DeliveryRecord {
    private String deliveryId;
    private String itemCode;
    private String itemName;
    private int deliveredQuantity;
    private String unit;
    private LocalDate date;

    public DeliveryRecord(String deliveryId, String itemCode, String itemName, int deliveredQuantity, String unit, LocalDate date) {
        this.deliveryId = deliveryId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.deliveredQuantity = deliveredQuantity;
        this.unit = unit;
        this.date = date;
    }

    // Getters
    public String getDeliveryId() { return deliveryId; }
    public String getItemCode() { return itemCode; }
    public String getItemName() { return itemName; }
    public int getDeliveredQuantity() { return deliveredQuantity; }
    public String getUnit() { return unit; }
    public LocalDate getDate() { return date; }

    // Optional: convert to CSV line for saving
    public String toCSV() {
        return deliveryId + "," + itemCode + "," + itemName + "," + deliveredQuantity + "," + unit + "," + date;
    }
}

