package com.news.kikar.biosense;

/**
 * Created by R23 on 25/05/2016.
 */
public class SetPatientData extends MsgWithBody {

    protected int reserved;
    protected char[] data;

    public SetPatientData() {
        data=new char[1024];
    }

    public SetPatientData(String msgId, short dataLength, short bufferP, int reserved, char[] data) {
        super(msgId, dataLength, bufferP);
        this.reserved = reserved;
        this.data = data;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public char[] getData() {
        return data;
    }

    public void setData(char[] data) {
        this.data = data;
    }
}
