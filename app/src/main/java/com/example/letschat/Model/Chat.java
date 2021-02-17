package com.example.letschat.Model;

public class Chat {

    private  String sender,receiver,message;
    private boolean isseen;

    public Chat(String sender,String receiver,String  message,boolean isseen)
    {
        this.sender=sender;
        this.receiver=receiver;
        this.message=message;
        this.isseen=isseen;
    }
    public Chat(){

    }
    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
