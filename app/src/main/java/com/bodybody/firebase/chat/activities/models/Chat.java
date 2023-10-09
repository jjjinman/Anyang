package com.bodybody.firebase.chat.activities.models;

import static com.bodybody.firebase.chat.activities.constants.IConstants.STARTED;

import org.jetbrains.annotations.NotNull;

public class Chat {

    private String id;
    private String sender;
    private String receiver;
    private String message;
    private boolean msgseen;
    private String datetime;
    private String type; //이미지
    private String imgPath; //전체 이미지 경로

    //녹음 업로드
    private String attachmentType;
    private String attachmentName;
    private String attachmentFileName;
    private String attachmentPath;
    private String attachmentData;
    private String attachmentDuration;
    private long attachmentSize;

    // 기본적으로 시작되고,다운로드 된 파일이 완료되면 완료된 상태로 알립니다 :
    // 이 변수는 FireBase DB에 저장되지 않았습니다.
    private int downloadProgress = STARTED;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public boolean isMsgseen() {
        return msgseen;
    }

    public void setMsgseen(boolean msgseen) {
        this.msgseen = msgseen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentFileName() {
        return attachmentFileName;
    }

    public void setAttachmentFileName(String attachmentFileName) {
        this.attachmentFileName = attachmentFileName;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getAttachmentData() {
        return attachmentData;
    }

    public void setAttachmentData(String attachmentData) {
        this.attachmentData = attachmentData;
    }

    public String getAttachmentDuration() {
        return attachmentDuration;
    }

    public void setAttachmentDuration(String attachmentDuration) {
        this.attachmentDuration = attachmentDuration;
    }

    public long getAttachmentSize() {
        return attachmentSize;
    }

    public void setAttachmentSize(long attachmentSize) {
        this.attachmentSize = attachmentSize;
    }

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    @NotNull
    @Override
    public String toString() {
        return "Chat{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", message='" + message + '\'' +
                ", msgseen=" + msgseen +
                ", datetime='" + datetime + '\'' +
                ", type='" + type + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", attachmentFileName='" + attachmentFileName + '\'' +
                ", attachmentName='" + attachmentName + '\'' +
                ", attachmentPath='" + attachmentPath + '\'' +
                '}';
    }
}
