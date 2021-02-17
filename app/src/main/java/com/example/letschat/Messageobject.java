package com.example.letschat;

public class Messageobject {

    String messageId,senderId,message;

    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public Messageobject(String messageId, String senderId, String message)
    {
        this.messageId=messageId;
        this.senderId=senderId;
        this.message=message;
    }

}
