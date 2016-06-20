package com.news.kikar.biosense;

/**
 * Created by R23 on 24/05/2016.
 */
public class Message {
    protected String msgId;

    public Message() {

    }
    public Message(String msgId) {
        setMsgId(msgId);
    }


    public String getMsgId() {
        return msgId;
    }


    public void setMsgId(String msgId) {
        for(int i=0;i<msgId.length();i++)
            if(msgId.charAt(i)>'Z'||msgId.charAt(i)<'A')
                return;
        this.msgId = msgId;
    }
}
