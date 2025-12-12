/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oopdsaproject;

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
    private String dateCreated;
    private int quantity;
    private String unit;
    private String status;

    public Request(String requestId, String itemCode, String itemName, 
        String dateCreated, int quantity, String unit, String status)
        {
            this.requestId = requestId;
            this.itemCode = itemCode;
            this.itemName = itemName;
            this.dateCreated = dateCreated;
            this.quantity = quantity;
            this.unit = unit;
            this.status = status;
            this.dateCreated = dateCreated;
        }
    
    public String getDateCreated() 
    { 
        return dateCreated; 
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
    public String getUnit()
    {
        return unit;
    }
    
}
