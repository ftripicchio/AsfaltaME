package com.dam.asfaltame.Database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class AppRepository {
    private static AppRepository _REPO = null;
    public ReportDao reportDao;
    public UserDao userDao;
    public ReportUserDao reportUserDao;

    private AppRepository(Context ctx) {
        AppDatabase db = Room.databaseBuilder(ctx,
                AppDatabase.class, "dam-pry-2018").fallbackToDestructiveMigration()
                .build();
        reportDao = db.reportDao();
        userDao = db.userDao();
        reportUserDao = db.reportUserDao();
    }

    public static AppRepository getInstance(Context ctx) {
        if (_REPO == null) _REPO = new AppRepository(ctx);
        return _REPO;
    }
}
