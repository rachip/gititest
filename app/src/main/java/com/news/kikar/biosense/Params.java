package com.news.kikar.biosense;

/**
 * Created by R23 on 25/05/2016.
 */
public class Params {

    protected ParamID param;
    protected byte value;

    public Params() {
    }

    public Params(ParamID param, byte value) {
        if(isLegit(param,value)) {
            this.param = param;
            this.value = value;
        }
    }

    public boolean isLegit(ParamID param, byte value)
    {
        switch (param) {
            case Input_Offset:
                if(value<(-128)||value>127)
                    return false;
                return true;

            case R_Min_Sensitivity:
                if(value<1||value>12)
                    return false;
                return true;

            case Shortest_Detectable_RR_Interval:
                if(value<32||value>128)
                    return false;
                return true;

            case Sensing_Decay_Delay:
                if(value<32||value>128)
                    return false;
                return true;

            case Episode_Collection:
                if(value<0||value<1)
                    return false;
                return true;

            case Episode_Types_To_Detect:
                for (ParamID p:ParamID.values()) {
                     if(value==p.getId())
                        return true;
                }
                return false;

            case Tachy_Interval:
                if(value<64||value>141)
                    return false;
                return true;

            case Tachy_Min_Number_Of_Beats:
                if(value<4||value>64)
                    return false;
                return true;

            case Bardy_Interval:
                if(value<0||value>255)
                    return false;
                return true;

            case Bardy_Min_Number_Of_Beats:
                if (value<4||value>16)
                    return false;
                return true;

            case Pause_Min_Length:
                if(value<12||value>40)
                    return false;
                return true;

            default:return false;
        }

    }

}
