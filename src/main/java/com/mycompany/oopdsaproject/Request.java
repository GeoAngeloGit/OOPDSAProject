/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oopdsaproject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author USER
 */
public class Request {
    private String requestId;
    private String itemCode;
    private String itemName;
    private LocalDate dateCreated;
    private LocalDate dateCompleted;
    private int quantity;
    private int originalQuantity;
    private String unit;
    private String status;

    public Request(String requestId, String itemCode, String itemName, 
        LocalDate dateCreated, LocalDate dateCompleted, int quantity, String unit, String status)
        {
            this.requestId = requestId;
            this.itemCode = itemCode;
            this.itemName = itemName;
            this.dateCreated = dateCreated;
            this.dateCompleted = dateCompleted;
            this.quantity = quantity;
            this.originalQuantity = quantity;
            this.unit = unit;
            this.status = status;
        }
    
    public LocalDate getDateCreated() 
    { 
        return dateCreated; 
    }
    public LocalDate getDateCompleted() 
    { 
        return dateCompleted; 
    }
    public String getRequestId() 
    { 
        return requestId; 
    }
    public String getItemName() 
    { 
        return itemName; 
    }
    public String getItemCode() 
    { 
        return itemCode; 
    }
    public String getStatus() 
    { 
        return status; 
    }
    public int getQuantity()
    {
        return quantity;
    }
    public int getOriginalQuantity()
    {
        return originalQuantity;
    }
    public String getUnit()
    {
        return unit;
    }


    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
    public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setOriginalQuantity(int originalQuantity)
    {
        this.originalQuantity = originalQuantity;
    }
    
}
