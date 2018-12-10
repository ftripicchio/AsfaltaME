package com.dam.asfaltame.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dam.asfaltame.Modelo.ReportUser;

import java.util.List;

@Dao
public interface ReportUserDao {
    @Query("SELECT * FROM ReportUser")
    List<ReportUser> getAll();

    @Insert
    long insert(ReportUser ru);

    @Update
    void update(ReportUser ru);

    @Delete
    void delete(ReportUser ru);
}
