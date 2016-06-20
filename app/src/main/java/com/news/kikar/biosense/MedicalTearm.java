package com.news.kikar.biosense;

/**
 * Created by R23 on 24/05/2016.
 */
public enum MedicalTearm {

        TACHY       ((short)1) ,
        BARDY       ((short)2),
        PAUSE       ((short)4),
        AF_AT       ((short)8),
        USER_EVENT  ((short)16) ;

        private final short id;

        MedicalTearm(short id) {
                this.id = id;
        }

        public short getId() {
                return id;
        }
}
