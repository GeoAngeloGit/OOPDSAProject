/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.models;

import com.mycompany.managers.*;
import com.mycompany.auxiliary.*;
import com.mycompany.models.*;
import com.mycompany.gui.*;

import java.time.LocalDate;

/**
 *
 * @author USER
 */
public class Transaction {
    private String transactionId;
    private TransactionType type;
    private String userName;
    private String itemCode;
    private String itemName;
    private int quantity; //quantity requested/delivered
    private int quantityToBeReleased;
    private String unit;
    private String source; //department/supplier
    private String releasedOrDeliveredBy; //admin releaser/supplier deliverer
    private LocalDate dateCreated;
    private LocalDate dateApproved;
    private LocalDate dateCompleted;
    private String status;

    public Transaction(String transactionId,
                       TransactionType type,
                       String userName,
                       String itemCode,
                       String itemName,
                       int quantity,
                       int quantityToBeReleased, 
                       String unit,
                       String source,
                       String releasedOrDeliveredBy,
                       String status,
                       LocalDate dateCreated,
                       LocalDate dateApproved,
                       LocalDate dateCompleted) {

        this.transactionId = transactionId;
        this.type = type;
        this.userName = userName;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.quantity = quantity;
        this.quantityToBeReleased = quantityToBeReleased;
        this.unit = unit;
        this.source = source;
        this.releasedOrDeliveredBy = releasedOrDeliveredBy;
        this.dateCreated = dateCreated;
        this.dateApproved = dateApproved;
        this.dateCompleted = dateCompleted;
        this.status = status;
    }

    public String getTransactionId() { return transactionId; }
    public TransactionType getType() { return type; }
    public String getUserName() { return userName; }
    public String getItemCode() { return itemCode; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public int getQuantityToBeReleased() { return quantityToBeReleased; }
    public String getUnit() { return unit; }
    public String getSource() { return source; }
    public String getReleasedOrDeliveredBy() { return releasedOrDeliveredBy; }
    public LocalDate getDateCreated() { return dateCreated; }
    public LocalDate getDateApproved() { return dateApproved; }
    public LocalDate getDateCompleted() { return dateCompleted; }
    public String getStatus() { return status; }

    public void setUserName(String userName) { this.userName = userName; }
}
