package com.news.kikar.biosense;

/**
 * Created by R23 on 24/05/2016.
 */
public enum EcgDataType {
    RAW_ECG                 ((short)0) ,
    RATA_DATA               ((short)1),
    EPISODE_LIST            ((short)2),
    EPISODE_RAW_DATA        ((short)3),
    STOP_TRANSMIT           ((short)127);

    private final short id;

    EcgDataType(short id) {
        this.id = id;
    }

    public short getId() {
        return id;
    }
}
