package com.news.kikar.biosense;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by R23 on 24/05/2016.
 */
public class PatientData extends MsgWithBody {
    protected int serialNum;
    protected ArrayList<String> data =new ArrayList<String>();
    protected StringBuilder PatientName, Patientphone,PatientID ,Gender, MedicalHistory, PhysicianName;
    protected StringBuilder PhysicianPhone, Hospital, ImplantFacility;
    protected /*Date*/ StringBuilder ImplantDate, BirthDate;

    public PatientData() {

    }

    public StringBuilder getPatientName() {
        return PatientName;
    }

    public void setPatientName(StringBuilder patientName) {
        PatientName = patientName;
    }

    public StringBuilder getPatientphone() {
        return Patientphone;
    }

    public void setPatientphone(StringBuilder patientphone) {
        Patientphone = patientphone;
    }

    public StringBuilder getPatientID() {
        return PatientID;
    }

    public void setPatientID(StringBuilder patientID) {
        PatientID = patientID;
    }

    public StringBuilder getGender() {
        return Gender;
    }

    public void setGender(StringBuilder gender) {
        Gender = gender;
    }

    public StringBuilder getMedicalHistory() {
        return MedicalHistory;
    }

    public void setMedicalHistory(StringBuilder medicalHistory) {
        MedicalHistory = medicalHistory;
    }

    public StringBuilder getPhysicianName() {
        return PhysicianName;
    }

    public void setPhysicianName(StringBuilder physicianName) {
        PhysicianName = physicianName;
    }

    public StringBuilder getPhysicianPhone() {
        return PhysicianPhone;
    }

    public void setPhysicianPhone(StringBuilder physicianPhone) {
        PhysicianPhone = physicianPhone;
    }

    public StringBuilder getHospital() {
        return Hospital;
    }

    public void setHospital(StringBuilder hospital) {
        Hospital = hospital;
    }

    public StringBuilder getImplantFacility() {
        return ImplantFacility;
    }

    public void setImplantFacility(StringBuilder implantFacility) {
        ImplantFacility = implantFacility;
    }

   /* public Date getImplantDate() {
        return ImplantDate;
    }

    public void setImplantDate(Date implantDate) {
        ImplantDate = implantDate;
    }

    public Date getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(Date birthDate) {
        BirthDate = birthDate;
    }*/

    public StringBuilder getImplantDate() {
        return ImplantDate;
    }

    public void setImplantDate(StringBuilder implantDate) {
        ImplantDate = implantDate;
    }

    public StringBuilder getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(StringBuilder birthDate) {
        BirthDate = birthDate;
    }

    public PatientData(String msgId, short dataLength, short bufferP, int serialNum) {
        super(msgId, dataLength, bufferP);
        this.serialNum = serialNum;


    }

    public int getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

}
