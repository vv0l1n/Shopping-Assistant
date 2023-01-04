package com.wolin.warehouseapp.utils.model;

public class GroupInvite {

    private String groupId;
    private String groupName;
    private String inviterEmail;
    private String targetEmail;
    private String date;

    public GroupInvite(){}

    public GroupInvite(String groupId, String groupName, String inviterEmail, String targetEmail, String date) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.inviterEmail = inviterEmail;
        this.targetEmail = targetEmail;
        this.date = date;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getInviterEmail() {
        return inviterEmail;
    }

    public void setInviterEmail(String inviterEmail) {
        this.inviterEmail = inviterEmail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTargetEmail() {
        return targetEmail;
    }

    public void setTargetEmail(String targetEmail) {
        this.targetEmail = targetEmail;
    }

    @Override
    public String toString() {
        return "GroupInvite{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", inviterEmail='" + inviterEmail + '\'' +
                ", targetEmail='" + targetEmail + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
