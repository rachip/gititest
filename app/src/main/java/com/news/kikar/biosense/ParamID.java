package com.news.kikar.biosense;

/**
 * Created by R23 on 24/05/2016.
 */
public enum ParamID {

    Input_Offset                        ((byte)1) ,   //0x01
    R_Min_Sensitivity                   ((byte)16),   //0x10
    Shortest_Detectable_RR_Interval     ((byte)17),   //0x11
    Sensing_Decay_Delay                 ((byte)18),   //0x12
    Episode_Collection                  ((byte)32),   //0x20
    Episode_Types_To_Detect             ((byte)33),   //0x21
    Tachy_Interval                      ((byte)48),   //0x30
    Tachy_Min_Number_Of_Beats           ((byte)49),   //0x31
    Bardy_Interval                      ((byte)64),   //0x40
    Bardy_Min_Number_Of_Beats           ((byte)65),   //0x41
    Pause_Min_Length                    ((byte)81);   //0x51

    private final byte id;

    ParamID(byte id) {
        this.id = id;
    }

    public byte getId() {
        return id;
    }
}
