/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.models;

/**
 *
 * @author USER
 */
import java.time.LocalDate;

public class DeliveryRecord {
    private String deliveryId;
    private String adminName;
    private String itemCode;
    private String itemName;
    private int deliveredQuantity;
    private String unit;
    private LocalDate dateDelivered;
    private LocalDate dateCompleted;

    public DeliveryRecord(String deliveryId, String adminName, String itemCode, String itemName, int deliveredQuantity, String unit, LocalDate dateDelivered, LocalDate dateCompleted) {
        this.deliveryId = deliveryId;
        this.adminName = adminName;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.deliveredQuantity = deliveredQuantity;
        this.unit = unit;
        this.dateDelivered = dateDelivered;
        this.dateCompleted = dateCompleted;
    }

    // Getters
    public String getDeliveryId() { return deliveryId; }
    public String getAdminName() { return adminName; }
    public String getItemCode() { return itemCode; }
    public String getItemName() { return itemName; }
    public int getDeliveredQuantity() { return deliveredQuantity; }
    public String getUnit() { return unit; }
    public LocalDate getDateDelivered() { return dateDelivered; }
    public LocalDate getDateCompleted() { return dateCompleted; }


    public void setDateCompleted(LocalDate dateCompleted) { this.dateCompleted = dateCompleted; }

    // Optional: convert to CSV line for saving
    public String toCSV() {
        return deliveryId + "," + itemCode + "," + itemName + "," + deliveredQuantity + "," + unit + "," + dateDelivered + "," + dateCompleted;
    }
}

