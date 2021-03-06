package com.dam.asfaltame.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.dam.asfaltame.Model.Report;
import com.dam.asfaltame.Model.ReportUser;
import com.dam.asfaltame.Model.User;

@Database(entities = {Report.class, User.class, ReportUser.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ReportDao reportDao();
    public abstract UserDao userDao();
    public abstract ReportUserDao reportUserDao();

}
