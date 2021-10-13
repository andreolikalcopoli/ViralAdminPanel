package com.magma.viraladminpanel;

public class BlockedUser {
    private String userId;
    private DateTime durationTo;
    private String reasonForBlocking;

    public BlockedUser() {
    }

    public BlockedUser(String userId, DateTime durationTo, String reasonForBlocking) {
        this.userId = userId;
        this.durationTo = durationTo;
        this.reasonForBlocking = reasonForBlocking;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public DateTime getDurationTo() { return durationTo; }
    public void setDurationTo(DateTime durationTo) { this.durationTo = durationTo; }

    public String getReasonForBlocking() { return reasonForBlocking; }
    public void setReasonForBlocking(String reasonForBlocking) { this.reasonForBlocking = reasonForBlocking; }
}
