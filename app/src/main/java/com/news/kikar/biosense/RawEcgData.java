package com.news.kikar.biosense;

import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by R23 on 24/05/2016.
 */
public class RawEcgData extends EcgData {
    protected ArrayList<Integer> data = new ArrayList<Integer>();

    public RawEcgData() {
    }

    public RawEcgData(String msgId, short dataLength, short bufferP, EcgDataType type) {
        super(msgId, dataLength, bufferP, type);
    }


}
