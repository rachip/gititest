package com.news.kikar.biosense;

/**
 * Created by R23 on 24/05/2016.
 */
public class EcgData extends MsgWithBody {
    protected EcgDataType type;

    public EcgData() {
    }

    public EcgData(String msgId, short dataLength, short bufferP, EcgDataType type) {
        super(msgId, dataLength, bufferP);
        this.type = type;
    }

    public EcgDataType getType() {
        return type;
    }

    public void setType(EcgDataType type) {
        this.type = type;
    }
}
