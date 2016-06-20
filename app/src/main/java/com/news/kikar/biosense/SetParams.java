package com.news.kikar.biosense;

import java.util.ArrayList;

/**
 * Created by R23 on 25/05/2016.
 */
public class SetParams extends Message {

    protected ArrayList<Params> paramsList = new ArrayList<Params>();

    public SetParams() {
    }

    public SetParams(String msgId, ArrayList<Params> paramsList) {
        super(msgId);
        this.paramsList = paramsList;
    }
}
