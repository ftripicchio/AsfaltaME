package com.dam.asfaltame.Modelo;

import android.arch.persistence.room.TypeConverter;

public class ReportTypeConverter {

    @TypeConverter
    public static ReportType  toReportType(String reportType) {
        return ReportType .valueOf(reportType);
    }

    @TypeConverter
    public static String  toString(ReportType reportType) {
        return reportType.toString();
    }
}
