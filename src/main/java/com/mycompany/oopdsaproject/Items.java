package com.mycompany.oopdsaproject;

public class Items {
    private String itemCode;
    private String itemName;
    private String dateOfLastRestock;
    private int quantity;
    private String unit;
    private String status;

    public Items(String itemCode, String itemName, String dateOfLastRestock, 
        int quantity, String unit, String status)
    {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.dateOfLastRestock = dateOfLastRestock;
        this.quantity = quantity;
        this.unit = unit;
        this.status = status;
    }

    public String getItemCode()
    {
        return itemCode;
    }
    public String getItemName()
    {
        return itemName;
    }
    public String getDateOfLastRestock()
    {
        return dateOfLastRestock;
    }
    public int getQuantity()
    {
        return quantity;
    }
    public String getUnit()
    {
        return unit;
    }
    public String getStatus()
    {
        return status;
    }


    public void setItemCode(String itemCode)
    {
        this.itemCode = itemCode;
    }
    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }
    public void setDateOfLastRestock(String dateOfLastRestock)
    {
        this.dateOfLastRestock = dateOfLastRestock;
    }
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
    public void setUnit(String unit)
    {
        this.unit = unit;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

}
