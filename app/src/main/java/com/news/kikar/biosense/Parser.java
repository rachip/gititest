package com.news.kikar.biosense;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by R23 on 30/05/2016.
 */
public class Parser {

    ArrayList<Byte> inputBuffer;
    int indexInptBuff;
    RawEcgData rawEcgMsg;
    EcgData EcgMsg;
    RateData rateDataMsg;
    EpisodeList episodeListMsg;
    EpisodeRawData episodeRawDataMsg;
    public Parser() {
    }



    public StringBuilder getMsgId(ArrayList<Byte> data,int index)
    {
        byte b;
        StringBuilder id=new StringBuilder();
        for (int i = 0; i < 6; i++) {
            b = data.get(index++);
            id.append((char) b);
        }
        return id;
    }

    public void parseRawECGData(ArrayList<Byte> inBuff,int index)
    {
        inputBuffer=inBuff;
        int localIndex=0;
        short bufferP;
        short dataLength;
        EcgDataType type = null;
        short idType;
        StringBuilder id=new StringBuilder();
        short val;
        int counter=0;
        while (counter<rawEcgMsg.dataLength)
        {
            val=(short) ((inputBuffer.get(index++) & 0xFF) | inputBuffer.get(index++) << 8);
            rawEcgMsg.data.add((int) val);
            counter++;
        }
        //localIndex=index;
        //StringBuilder s=new StringBuilder();
        //s=getMsgId(inputBuffer,localIndex++);
       // while (s.toString()!="ECGDAT")
       // {
       //     s=getMsgId(inputBuffer,localIndex++);
       // }

    }


    public EcgData parseEcgData(ArrayList<Byte> inBuff,int index)
    {
        short bufferP;
        short dataLength;
        EcgDataType type = null;
        short idType;
        StringBuilder id=new StringBuilder();
        byte val;
        int localInd=0;

        bufferP = (short) ((inBuff.get(index+localInd++) & 0xFF) | inBuff.get(index+localInd++) << 8);
        dataLength = (short) ((inBuff.get(index+localInd++) & 0xFF) | inBuff.get(index+localInd++) << 8);
        idType = (short) ((inBuff.get(index+localInd++) & 0xFF) | inBuff.get(index+localInd++) << 8);
        for (EcgDataType e:EcgDataType.values()) {
             if(e.getId()==idType) {
                 type = e;
                 break;
             }
        }
        //EcgMsg=new EcgData("ECGDAT",dataLength,bufferP,type);
        RateData rateData;
        index+=26;
        switch (type)
        {
            case RAW_ECG:
                rawEcgMsg=new RawEcgData("ECGDAT",dataLength,bufferP,type);
                parseRawECGData(inBuff,index);
                EcgMsg=rawEcgMsg;
                break;
            case RATA_DATA:
                rateDataMsg=new RateData("ECGDAT",dataLength,bufferP,type);
                parseRateData(inBuff,index);
                EcgMsg=rateDataMsg;
                break;
            case EPISODE_LIST:
                episodeListMsg=new EpisodeList("ECGDAT",dataLength,bufferP,type);
                parseEpisodeList(inBuff,index);
                EcgMsg=episodeListMsg;
                break;
            case EPISODE_RAW_DATA:
                episodeRawDataMsg=new EpisodeRawData("ECGDAT",dataLength,bufferP,type);
                parseEpisodeRawData(inBuff,index);
                EcgMsg=episodeRawDataMsg;
                break;
        }
        return EcgMsg;
    }


    public void parseRateData(ArrayList<Byte> inBuff,int index)
    {
        short val;
        int counter=0;
        //rateDataMsg=new RateData(orgMsg.msgId,orgMsg.dataLength,orgMsg.bufferP,orgMsg.type);
       // rateDataMsg=orgMsg;
        short year = (short) ((inBuff.get(index++) & 0xFF) | inBuff.get(index++) << 8);
        rateDataMsg.setYear(year);
        rateDataMsg.setMonth(inBuff.get(index++));
        rateDataMsg.setDay(inBuff.get(index++));
        rateDataMsg.setHour(inBuff.get(index++));
        rateDataMsg.setMinute(inBuff.get(index++));

        while (counter<rateDataMsg.dataLength)
        {
            val=(short) ((inBuff.get(index++) & 0xFF) | inBuff.get(index++) << 8);
            rateDataMsg.data.add((short)(val*2));
            counter++;
        }
    }


    public void parseEpisodeList(ArrayList<Byte> inBuff, int index)
    {
        byte val;

        episodeListMsg.setYear((short) ((inBuff.get(index++) & 0xFF) | inBuff.get(index++) << 8));
        episodeListMsg.setMonth(inBuff.get(index++));
        episodeListMsg.setDay(inBuff.get(index++));
        episodeListMsg.setHour(inBuff.get(index++));
        episodeListMsg.setMinute(inBuff.get(index++));
        episodeListMsg.setSeconds(inBuff.get(index++));
        short medicalT = (short) ((inBuff.get(index++) & 0xFF) | inBuff.get(index++) << 8);
        for (MedicalTearm m : MedicalTearm.values()) {
            if (m.getId() == medicalT) {
                episodeListMsg.setMedicalTearm(m);
                break;
            }
        }
        episodeListMsg.setLenInSeconds((short) ((inBuff.get(index++) & 0xFF) | inBuff.get(index++) << 8));
    }

    public void parseEpisodeRawData(ArrayList<Byte> inBuff,int index)
    {
        short val;
        int counter=0;

        episodeRawDataMsg.setYear((short) ((inBuff.get(index++) & 0xFF) | inBuff.get(index++) << 8));
        episodeRawDataMsg.setMonth(inBuff.get(index++));
        episodeRawDataMsg.setDay(inBuff.get(index++));
        episodeRawDataMsg.setHour(inBuff.get(index++));
        episodeRawDataMsg.setMinute(inBuff.get(index++));
        episodeRawDataMsg.setLenInSeconds((short) ((inBuff.get(index++) & 0xFF) | inBuff.get(index++) << 8));
        while (counter<episodeRawDataMsg.dataLength)
        {
            val=(short) ((inBuff.get(index++) & 0xFF) | inBuff.get(index++) << 8);
            episodeRawDataMsg.data.add(val);
            counter++;
        }

    }


    public StringBuilder getOneParamFromPatDat()
    {
        byte b=' ';
        boolean flag=true;
        StringBuilder s=new StringBuilder();
        while(flag)
        {

            b=inputBuffer.get(indexInptBuff++);
            if((char)b!='$')
            {
                s.append((char) b);
            }

            else
            {
                flag=false;
            }
        }
        return s;
    }

    PatientData patientDataMsg;
    public PatientData parsePatientData(ArrayList<Byte> inBuff,int ind)
    {
        inputBuffer=inBuff;
        indexInptBuff=ind;


        short bufferP;
        short dataLength;
        EcgDataType type = null;
        short idType;
        StringBuilder id=new StringBuilder();
        StringBuilder val=new StringBuilder();
        int serialNum;

       // id=getMsgId();
       // if(!id.toString().equals("PATDAT")) {
       //     return null;
       // }

        bufferP = (short) ((inputBuffer.get(indexInptBuff++) & 0xFF) | inputBuffer.get(indexInptBuff++) << 8);
        dataLength = (short) ((inputBuffer.get(indexInptBuff++) & 0xFF) | inputBuffer.get(indexInptBuff++) << 8);
        short LSW = (short) ((inputBuffer.get(indexInptBuff++) & 0xFF) | inputBuffer.get(indexInptBuff++) << 8);
        short MSW = (short) ((inputBuffer.get(indexInptBuff++) & 0xFF) | inputBuffer.get(indexInptBuff++) << 8);
        serialNum = (int) ((MSW & 0xFFFF) | LSW << 16);
        patientDataMsg= new PatientData("PATDAT",dataLength,bufferP,serialNum);
        indexInptBuff = 32;
        patientDataMsg.setImplantDate(getOneParamFromPatDat());

        patientDataMsg.setPatientName(getOneParamFromPatDat());

        patientDataMsg.setPatientphone(getOneParamFromPatDat());

        patientDataMsg.setPatientID(getOneParamFromPatDat());

        patientDataMsg.setBirthDate(getOneParamFromPatDat());

        patientDataMsg.setGender(getOneParamFromPatDat());

        patientDataMsg.setMedicalHistory(getOneParamFromPatDat());

        patientDataMsg.setPhysicianName(getOneParamFromPatDat());

        patientDataMsg.setPhysicianPhone(getOneParamFromPatDat());

        patientDataMsg.setHospital(getOneParamFromPatDat());

        patientDataMsg.setImplantFacility(getOneParamFromPatDat());

        return patientDataMsg;
    }
    public StringBuilder extractVal(ArrayList<Byte> inBuff, int index)
    {
        byte b;
        StringBuilder val=new StringBuilder();
        b= inBuff.get(index++);
        while((char)b!= '$'&& index<inBuff.size())
        {
            val.append((char)b);
        }
        return val;
    }
    GetParamsResponse getPrmResponseMsg;
    public GetParamsResponse parseGetParamsResponse(ArrayList<Byte> inBuff)
    {
        inputBuffer=inBuff;
        ParamID param=null;
        byte pId;
        byte val;
        getPrmResponseMsg=new GetParamsResponse();
        int index=6;
        StringBuilder id=new StringBuilder();

        //id=getMsgId();
        if(!id.toString().equals("GETPAR")) {
            return null;
        }

        while (index<inputBuffer.size())
        {
            pId=inputBuffer.get(index++);
            val=inputBuffer.get(index++);
            if(pId==-86||val==-86)
                break;
            for (ParamID p:ParamID.values()) {
                if(p.getId()==pId) {
                    param = p;
                    break;
                }
            }
           Params p=new Params(param,val);
           if(p.param==param)
               getPrmResponseMsg.paramsList.add(p);
        }
        return getPrmResponseMsg;
    }


}
