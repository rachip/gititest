package com.news.kikar.biosense;

/**
 * Created by R23 on 24/05/2016.
 */
public class MsgWithBody extends Message {
    protected short bufferP;
    protected short dataLength;

    public MsgWithBody() {
    }

    public MsgWithBody(String msgId, short dataLength, short bufferP) {
        super(msgId);
        setDataLength(dataLength);
        setBufferP(bufferP);
    }

    public short getDataLength() {
        return dataLength;
    }

    public void setDataLength(short dataLength) {
        if(dataLength<0||dataLength>1024)
            return;
        this.dataLength = dataLength;
    }

    public short getBufferP() {
        return bufferP;

    }

    public void setBufferP(short bufferP) {
        if(bufferP<0||bufferP>1024)
            return;
        this.bufferP = bufferP;
    }
}
