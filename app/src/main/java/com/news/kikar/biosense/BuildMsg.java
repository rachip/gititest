package com.news.kikar.biosense;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by R23 on 31/05/2016.
 */
public class BuildMsg {


    public ArrayList<Byte> buildMsgSetTime() {

        ArrayList<Byte> outMsgBuffer = new ArrayList<Byte>();

        char[] id = new char[6];
        id[0] = 'S';
        id[1] = 'E';
        id[2] = 'T';
        id[3] = 'T';
        id[4] = 'I';
        id[5] = 'M';
        for (int i = 0; i < id.length; i++) {
            outMsgBuffer.add((byte) id[i]);
        }
        int year = Calendar.getInstance().get(Calendar.YEAR);
        byte lsbY = (byte) year;
        byte msbY = (byte) (year >> 8);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int seconds = Calendar.getInstance().get(Calendar.SECOND);
        byte lsbS = (byte) seconds;
        byte msbS = (byte) (seconds >> 8);

        outMsgBuffer.add(lsbY);
        outMsgBuffer.add(msbY);
        outMsgBuffer.add((byte) month);
        outMsgBuffer.add((byte) day);
        outMsgBuffer.add((byte) hour);
        outMsgBuffer.add((byte) minute);
        outMsgBuffer.add(lsbS);
        outMsgBuffer.add(msbS);

        while (outMsgBuffer.size() < 32) {
            outMsgBuffer.add((byte) (-86));
        }
        return outMsgBuffer;
    }

    public ArrayList<Byte> buildMsgGetData(EcgDataType type ) {
        ArrayList<Byte> outMsgBuffer = new ArrayList<Byte>();
        char[] id = new char[6];
        id[0] = 'R';
        id[1] = 'E';
        id[2] = 'D';
        id[3] = 'D';
        id[4] = 'A';
        id[5] = 'T';
        for (int i = 0; i < id.length; i++) {
            outMsgBuffer.add((byte) id[i]);
        }
        outMsgBuffer.add((byte)type.getId());
        while (outMsgBuffer.size() < 32) {
            outMsgBuffer.add((byte) (-86));
        }
        return outMsgBuffer;
    }

    public ArrayList<Byte> buildMsgSetPatData(char[]data,short buffOfset)
    {
        ArrayList<Byte> outMsgBuffer = new ArrayList<Byte>();
        char[] id = new char[6];
        id[0] = 'P';
        id[1] = 'A';
        id[2] = 'T';
        id[3] = 'D';
        id[4] = 'A';
        id[5] = 'T';
        for (int i = 0; i < id.length; i++) {
            outMsgBuffer.add((byte) id[i]);
        }
        byte lsbB = (byte) buffOfset;
        byte msbB = (byte) (buffOfset >> 8);
        outMsgBuffer.add(lsbB);
        outMsgBuffer.add(msbB);
        outMsgBuffer.add((byte)(data.length));
        outMsgBuffer.add((byte)0);
        outMsgBuffer.add((byte)0);
        while (outMsgBuffer.size() < 32) {
            outMsgBuffer.add((byte) (-86));
        }
        return outMsgBuffer;
    }

    public ArrayList<Byte> buildMsgSetParams(List<Params> data)
    {
        ArrayList<Byte> outMsgBuffer = new ArrayList<Byte>();
        char[] id = new char[6];
        id[0] = 'S';
        id[1] = 'E';
        id[2] = 'T';
        id[3] = 'P';
        id[4] = 'A';
        id[5] = 'R';
        for (int i = 0; i < id.length; i++) {
            outMsgBuffer.add((byte) id[i]);
        }

        for (Params p:data) {
            outMsgBuffer.add(p.param.getId());
            outMsgBuffer.add(p.value);
        }
        while (outMsgBuffer.size() < 32) {
            outMsgBuffer.add((byte) (-86));
        }
        outMsgBuffer.add(31,(byte) (-86));//corner case
        return outMsgBuffer;
    }

    public ArrayList<Byte> buildMsgGetParams(List<ParamID> data)
    {
        ArrayList<Byte> outMsgBuffer = new ArrayList<Byte>();
        char[] id = new char[6];
        id[0] = 'G';
        id[1] = 'E';
        id[2] = 'T';
        id[3] = 'P';
        id[4] = 'A';
        id[5] = 'R';
        for (int i = 0; i < id.length; i++) {
            outMsgBuffer.add((byte) id[i]);
        }
        for (ParamID p:data) {
            outMsgBuffer.add(p.getId());
        }


        while (outMsgBuffer.size() < 32) {
            outMsgBuffer.add((byte) (-86));
        }
        outMsgBuffer.add(31,(byte) (-86));//corner case


        return outMsgBuffer;
    }

    public ArrayList<Byte> buildMsgPatientEventReq(List<Short> data)
    {
        ArrayList<Byte> outMsgBuffer = new ArrayList<Byte>();
        char[] id = new char[6];
        id[0] = 'S';
        id[1] = 'Y';
        id[2] = 'M';
        id[3] = 'P';
        id[4] = 'T';
        id[5] = 'M';
        for (int i = 0; i < id.length; i++) {
            outMsgBuffer.add((byte) id[i]);
        }
        for (short val: data) {
            byte lsbB = (byte) val;
            byte msbB = (byte) (val >> 8);
            outMsgBuffer.add(lsbB);
            outMsgBuffer.add(msbB);
        }
        while (outMsgBuffer.size() < 32) {
            outMsgBuffer.add((byte) (-86));
        }

        return outMsgBuffer;
    }

}
