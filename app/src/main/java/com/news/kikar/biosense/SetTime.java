package com.news.kikar.biosense;

/**
 * Created by R23 on 24/05/2016.
 */
public class SetTime extends Message {

    protected short year;
    protected byte month;
    protected byte day;
    protected byte hour;
    protected byte minute;
    protected short seconds;

    public SetTime() {
    }

    public SetTime(String msgId, short year, byte month, byte day, byte hour, byte minute, short seconds) {
        super(msgId);
        setYear(year);
        setMonth(month);
        setDay(day);
        setHour(hour);
        setMinute(minute);
        setSeconds(seconds);
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
        if(month<0x01||month>0x0c)
            return;
        this.month = month;
    }

    public byte getDay() {
        return day;
    }

    public void setDay(byte day) {
        if(day<0x01||day<0x1f)
            return;
        this.day = day;
    }

    public byte getHour() {
        return hour;
    }

    public void setHour(byte hour) {
        if(hour<0x00||hour<0x17)
            return;
        this.hour = hour;
    }

    public byte getMinute() {
        return minute;
    }

    public void setMinute(byte minute) {
        if(minute<0x00||minute<0x3b)
            return;
        this.minute = minute;
    }

    public short getSeconds() {
        return seconds;
    }

    public void setSeconds(short seconds) {
        if(seconds<0x0000||seconds>0x003b)
            return;
        this.seconds = seconds;
    }
}



