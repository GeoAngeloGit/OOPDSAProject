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
public class Transaction {
    private String transactionId;
    private TransactionType type;
    private String itemCode;
    private String itemName;
    private int quantity; //quantity requested/delivered
    private String unit;
    private String source; //department/supplier
    private LocalDate dateCreated;
    private LocalDate dateCompleted;
    private String status;

    public Transaction(String transactionId,
                       TransactionType type,
                       String itemCode,
                       String itemName,
                       int quantity,
                       String unit,
                       String source,
                       String status,
                       LocalDate dateCreated,
                       LocalDate dateCompleted) {

        this.transactionId = transactionId;
        this.type = type;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unit = unit;
        this.source = source;
        this.dateCreated = dateCreated;
        this.dateCompleted = dateCompleted;
        this.status = status;
    }

    public String getTransactionId() { return transactionId; }
    public TransactionType getType() { return type; }
    public String getItemCode() { return itemCode; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getSource() { return source; }
    public LocalDate getDateCreated() { return dateCreated; }
    public LocalDate getDateCompleted() { return dateCompleted; }
    public String getStatus() { return status; }
}
