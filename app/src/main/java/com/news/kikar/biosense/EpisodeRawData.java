package com.news.kikar.biosense;

import java.util.ArrayList;

/**
 * Created by R23 on 25/05/2016.
 */
public class EpisodeRawData extends EcgData {
    public EpisodeRawData(String msgId, short dataLength, short bufferP, EcgDataType type) {
        super(msgId, dataLength, bufferP, type);
    }

    protected short year;
    protected byte month;
    protected byte day;
    protected byte hour;
    protected byte minute;
    protected short lenInSeconds;
    protected ArrayList<Short> data = new ArrayList<Short>();

    public EpisodeRawData() {
    }

    public EpisodeRawData(String msgId, short dataLength, short bufferP, EcgDataType type, short year, byte month, byte day, byte hour, byte minute, short lenInSeconds) {
        super(msgId, dataLength, bufferP, type);
        setYear(year);
        setMonth(month);
        setDay(day);
        setHour(hour);
        setMinute(minute);
        setLenInSeconds(lenInSeconds);

    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public byte getMonth() {
        return month;
    }

    public void setMonth(byte month) {
        if(month<1||month>12)
            return;
        this.month = month;
    }

    public byte getDay() {
        return day;
    }

    public void setDay(byte day) {
        if(day<0||day>31)
            return;
        this.day = day;
    }

    public byte getHour() {
        return hour;
    }

    public void setHour(byte hour) {
        if(hour<0||hour>23)
            return;
        this.hour = hour;
    }

    public byte getMinute() {
        return minute;
    }

    public void setMinute(byte minute) {
        if(minute<0||minute>59)
            return;
        this.minute = minute;
    }

    public short getLenInSeconds() {
        return lenInSeconds;
    }

    public void setLenInSeconds(short lenInSeconds) {
        this.lenInSeconds = lenInSeconds;
    }




}
