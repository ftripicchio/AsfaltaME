package com.dam.asfaltame.Modelo;

import android.arch.persistence.room.TypeConverter;

public class StatusConverter {

    @TypeConverter
    public static Status  toStatus(String status) {
        return Status .valueOf(status);
    }

    @TypeConverter
    public static String  toString(Status status) {
        return status.toString();
    }
}
