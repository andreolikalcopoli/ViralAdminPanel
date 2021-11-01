package com.magma.viraladminpanel;

import java.util.Date;

public class BlockedUser {
    private String userId;
    private Date durationTo;
    private String reasonForBlocking;

    public BlockedUser() { }

    public BlockedUser(String userId, Date durationTo, String reasonForBlocking) {
        this.userId = userId;
        this.durationTo = durationTo;
        this.reasonForBlocking = reasonForBlocking;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Date getDurationTo() { return durationTo; }
    public void setDurationTo(Date durationTo) { this.durationTo = durationTo; }

    public String getReasonForBlocking() { return reasonForBlocking; }
    public void setReasonForBlocking(String reasonForBlocking) { this.reasonForBlocking = reasonForBlocking; }
}
