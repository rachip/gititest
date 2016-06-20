package com.news.kikar.biosense;

/**
 * Created by R23 on 24/05/2016.
 */
public class EpisodeList extends EcgData {
    protected short year;
    protected byte month;
    protected byte day;
    protected byte hour;
    protected byte minute;
    protected byte seconds;
    protected MedicalTearm medicalTearm;
    protected short lenInSeconds;

    public EpisodeList() {
    }

    public EpisodeList(String msgId, short dataLength, short bufferP, EcgDataType type, short year, byte month, byte day, byte hour, byte minute, byte seconds, MedicalTearm medicalTearm, short lenInSeconds) {
        super(msgId, dataLength, bufferP, type);
        setYear(year);
        setMonth(month);
        setDay(day);
        setHour(hour);
        setMinute(minute);
        setSeconds(seconds);
        setMedicalTearm(medicalTearm);
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

    public EpisodeList(String msgId, short dataLength, short bufferP, EcgDataType type) {
        super(msgId, dataLength, bufferP, type);
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
        if(day<1||day>31)
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

    public byte getSeconds() {
        return seconds;
    }

    public void setSeconds(byte seconds) {
        if(seconds<0||seconds<59)
            return;
        this.seconds = seconds;
    }

    public MedicalTearm getMedicalTearm() {
        return medicalTearm;
    }

    public void setMedicalTearm(MedicalTearm medicalTearm) {
        this.medicalTearm = medicalTearm;
    }

    public short getLenInSeconds() {
        return lenInSeconds;
    }

    public void setLenInSeconds(short lenInSeconds) {
        this.lenInSeconds = lenInSeconds;
    }
}
