package com.magma.viraladminpanel;

import java.util.HashMap;
import java.util.Map;

public class People {
    private String userId;

    private Map<String, String> blocked;
    private Map<String, String> blockedBy;
    private Map<String, String> postReports;
    private Map<String, String> profileReports;

    public People() {
        this.blocked = new HashMap<>();
        this.blockedBy = new HashMap<>();
        this.postReports = new HashMap<>();
        this.profileReports = new HashMap<>();
    }

    public People(String userId, Map<String, String> blocked, Map<String, String> blockedBy, Map<String, String> postReports, Map<String, String> profileReports) {
        this.userId = userId;
        this.blocked = blocked;
        this.blockedBy = blockedBy;
        this.postReports = postReports;
        this.profileReports = profileReports;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Map<String, String> getBlocked() { return blocked; }
    public void setBlocked(Map<String, String> blocked) { this.blocked = blocked; }

    public Map<String, String> getBlockedBy() { return blockedBy; }
    public void setBlockedBy(Map<String, String> blockedBy) { this.blockedBy = blockedBy; }

    public Map<String, String> getPostReports() { return postReports; }
    public void setPostReports(Map<String, String> postReports) { this.postReports = postReports; }

    public Map<String, String> getProfileReports() { return profileReports; }
    public void setProfileReports(Map<String, String> profileReports) { this.profileReports = profileReports; }
}
