package com.magma.viraladminpanel;

public class Admin {
    private String adminId;
    private String adminEmail;
    private String adminLevel;

    public Admin() { }

    public Admin(String adminId, String adminEmail, String adminLevel) {
        this.adminId = adminId;
        this.adminEmail = adminEmail;
        this.adminLevel = adminLevel;
    }

    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }

    public String getAdminEmail() { return adminEmail; }
    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }

    public String getAdminLevel() { return adminLevel; }
    public void setAdminLevel(String adminLevel) { this.adminLevel = adminLevel; }
}
