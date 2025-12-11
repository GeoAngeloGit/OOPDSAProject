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
    private String department;
    private List<RequestItem> items;
    private String status;
    private LocalDateTime dateCreated;

    public Request(String requestId, String department, 
        List<RequestItem> items, String status, LocalDateTime dateCreated)
        {
            this.requestId = requestId;
            this.department = department;
            this.items = items;
            this.status = status;
            this.dateCreated = dateCreated;
        }
    
    public LocalDateTime getDateCreated() 
    { 
        return dateCreated; 
    }
    public String getRequestId() 
    { 
        return requestId; 
    }
    public List<RequestItem> getItems() 
    { 
        return items; 
    }
    public String getDepartment() 
    { 
        return department; 
    }
    public String getStatus() 
    { 
        return status; 
    }
    
}
