package com.dam.asfaltame.Model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class Report implements Parcelable{
    @PrimaryKey(autoGenerate = true)
    long id;
    String address;
    private Double latitud;
    private Double longitud;
    @TypeConverters(ReportTypeConverter.class)
    ReportType reportType;
    @TypeConverters(StatusConverter.class)
    Status status;
    String description;
    String pictures;
    int apoyos;
    int solucionado;
    @Embedded(prefix = "u_")
    User propietario;

    public Report(){}

    public Report(long id, String address, Double latitud, Double longitud, ReportType reportType, Status status, String description, String pictures, int apoyos, int solucionado, User propietario) {
        this.id = id;
        this.address = address;
        this.latitud = latitud;
        this.longitud = longitud;
        this.reportType = reportType;
        this.status = status;
        this.description = description;
        this.pictures = pictures;
        this.apoyos = apoyos;
        this.solucionado = solucionado;
        this.propietario = propietario;
    }

    public Report(Parcel in){
        id = in.readLong();
        address = in.readString();
        latitud = in.readDouble();
        longitud = in.readDouble();
        reportType = ReportType.valueOf(in.readString());
        status = Status.valueOf(in.readString());
        description = in.readString();
        pictures = in.readString();
        apoyos = in.readInt();
        solucionado = in.readInt();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public int getApoyos() {
        return apoyos;
    }

    public void setApoyos(int apoyos) {
        this.apoyos = apoyos;
    }

    public int getSolucionado() {
        return solucionado;
    }

    public void setSolucionado(int solucionado) {
        this.solucionado = solucionado;
    }

    public User getPropietario() {
        return propietario;
    }

    public void setPropietario(User propietario) {
        this.propietario = propietario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(address);
        dest.writeDouble(latitud);
        dest.writeDouble(longitud);
        dest.writeString(reportType.toString());
        dest.writeString(status.toString());
        dest.writeString(description);
        dest.writeString(pictures);
        dest.writeInt(apoyos);
        dest.writeInt(solucionado);
    }

    public static final Parcelable.Creator<Report> CREATOR = new Parcelable.Creator<Report>() {
        public Report createFromParcel(Parcel source) {
            return new Report(source);
        }
        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };
}


