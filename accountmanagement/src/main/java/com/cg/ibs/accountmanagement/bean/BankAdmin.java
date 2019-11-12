package com.cg.ibs.accountmanagement.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

 

@Entity
@Table(name = "bank_admins")
public class BankAdmin {
    
    @Id
    @Column(name = "admin_id", nullable = false)
    String adminId;
    @Column(name = "password", nullable = false)
    String password;
    
    public String getAdminId() {
        return adminId;
    }
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public BankAdmin() {
        super();
    }
    
    public BankAdmin(String adminId, String password) {
        super();
        this.adminId = adminId;
        this.password = password;
    }
}