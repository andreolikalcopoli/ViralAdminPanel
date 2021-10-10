package com.magma.viraladminpanel;

public class Post {
    private String id;
    private String user;
    private String location;
    private String type;
    private String post;
    private String description;
    private Boolean optionShare;
    private Boolean optionComments;

    public static final String POST_TYPE_IMAGE = "image";
    public static final String POST_TYPE_VIDEO = "video";
    public static final String POST_TYPE_TEXT = "text";

    public Post() { }

    public Post(String id, String user, String location, String type, String post, String description, Boolean optionShare, Boolean optionComments) {
        this.id = id;
        this.user = user;
        this.location = location;
        this.type = type;
        this.post = post;
        this.description = description;
        this.optionShare = optionShare;
        this.optionComments = optionComments;
    }

    public String getId() { return id; }
    public String getUser() { return user; }
    public String getLocation() { return location; }
    public String getType() { return type; }
    public String getPost() { return post; }
    public String getDescription() { return description; }
    public Boolean getOptionShare() { return optionShare; }
    public Boolean getOptionComments() { return optionComments; }
}
