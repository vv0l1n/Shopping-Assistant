package com.wolin.warehouseapp.utils.model;

public class GroupInvite {

    private String groupId;
    private String inviterUid;
    private String targetEmail;

    public GroupInvite(String groupId, String inviterUid, String targetEmail) {
        this.groupId = groupId;
        this.inviterUid = inviterUid;
        this.targetEmail = targetEmail;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getInviterUid() {
        return inviterUid;
    }

    public void setInviterUid(String inviterUid) {
        this.inviterUid = inviterUid;
    }

    public String getTargetEmail() {
        return targetEmail;
    }

    public void setTargetEmail(String targetEmail) {
        this.targetEmail = targetEmail;
    }
}
