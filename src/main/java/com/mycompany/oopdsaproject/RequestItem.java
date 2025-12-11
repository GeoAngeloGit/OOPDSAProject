/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oopdsaproject;

/**
 *
 * @author USER
 */
public class RequestItem {
    private String itemCode;
    private String itemName;
    private int quantity;
    private String unit;

    public RequestItem(String itemCode, String itemName, int quantity, String unit)
    {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getItemCode()
    {
        return itemCode;
    }
    public String getItemName()
    {
        return itemName;
    }
    public int getQuantity()
    {
        return quantity;
    }
    public String getUnit()
    {
        return unit;
    }
    
    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}
