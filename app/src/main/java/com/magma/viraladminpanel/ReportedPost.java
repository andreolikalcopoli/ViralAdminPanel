package com.magma.viraladminpanel;

import java.util.HashMap;
import java.util.Map;

public class ReportedPost implements Comparable<ReportedPost> {
    private String postId;
    private Map<String, String> reports;

    private Post post;

    public ReportedPost() {
        reports = new HashMap<>();
    }

    public ReportedPost(String postId, Map<String, String> reports) {
        this.postId = postId;
        this.reports = reports;
    }

    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public Map<String, String> getReports() { return reports; }
    public void setReports(Map<String, String> reports) { this.reports = reports; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    @Override
    public int compareTo(ReportedPost reportedPost) {
        return reportedPost.getReports().size() - this.reports.size();
    }
}
