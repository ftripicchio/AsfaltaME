package com.dam.asfaltame.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dam.asfaltame.Model.Report;

import java.util.List;

@Dao
public interface ReportDao {
    @Query("SELECT * FROM Report")
    List<Report> getAll();

    @Query("SELECT * FROM Report WHERE id LIKE :id")
    List<Report> getById(long id);

    @Insert
    long insert(Report rep);

    @Update
    void update(Report rep);

    @Delete
    void delete(Report rep);
}
