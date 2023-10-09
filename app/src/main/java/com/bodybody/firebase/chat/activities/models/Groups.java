package com.bodybody.firebase.chat.activities.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;


//그룹 채팅에 필요한 그룹 클래스로 그룹명과 아이디, 그룹 관리자,
//그룹 이미지, 마지막 메세지, 마지막 메세지 작성 시간, 타입,
//그룹 멤버, 그룹 추가, 활동여부, 메세지 보내기 허용되는 사람 등을
//지정 하도록 합니다.
public class Groups implements Serializable {
    private String id;
    private String groupName;
    private String admin;
    private String groupImg;
    private String lastMsg;
    private String lastMsgTime;
    private String type;
    private List<String> members;
    private String createdAt;
    private boolean active;
    private int sendMessageSetting; // 0 = 모든 유저 허용, 1 = 관리자만 허용

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getGroupImg() {
        return groupImg;
    }

    public void setGroupImg(String groupImg) {
        this.groupImg = groupImg;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(String lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSendMessageSetting() {
        return sendMessageSetting;
    }

    public void setSendMessageSetting(int sendMessageSetting) {
        this.sendMessageSetting = sendMessageSetting;
    }

    @NotNull
    @Override
    public String toString() {
        return "Groups{" +
                "id='" + id + '\'' +
                ", groupName='" + groupName + '\'' +
                ", admin='" + admin + '\'' +
                ", groupImg='" + groupImg + '\'' +
                ", lastMsg='" + lastMsg + '\'' +
                ", lastMsgTime='" + lastMsgTime + '\'' +
                ", members=" + members +
                ", createdAt='" + createdAt + '\'' +
                ", active='" + active + '\'' +
                ", sendMessageSetting='" + sendMessageSetting + '\'' +
                '}';
    }
}
