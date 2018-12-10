package com.dam.asfaltame.Modelo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ReportUser {
    @PrimaryKey(autoGenerate = true)
    long id;
    @Embedded(prefix = "r_")
    Report report;
    @Embedded(prefix = "u_")
    User user;
    int action;

    public ReportUser(Report report, User user, int action) {
        this.report = report;
        this.user = user;
        this.action = action;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
