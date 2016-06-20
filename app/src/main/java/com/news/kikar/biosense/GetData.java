package com.news.kikar.biosense;

/**
 * Created by R23 on 25/05/2016.
 */
public class GetData extends Message {
    protected EcgDataType dataType;

    public GetData() {
    }

    public GetData(String msgId, EcgDataType dataType) {
        super(msgId);
        this.dataType = dataType;
    }

    public EcgDataType getDataType() {
        return dataType;
    }

    public void setDataType(EcgDataType dataType) {
        this.dataType = dataType;
    }
}
