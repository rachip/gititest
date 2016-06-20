package com.news.kikar.biosense;

/**
 * Created by R23 on 25/05/2016.
 */
public class PatientEventRequest extends Message {

    protected short reserved;

    public PatientEventRequest() {
    }

    public PatientEventRequest(String msgId, short reserved) {
        super(msgId);
        this.reserved = reserved;
    }

    public short getReserved() {
        return reserved;
    }

    public void setReserved(short reserved) {
        this.reserved = reserved;
    }
}
