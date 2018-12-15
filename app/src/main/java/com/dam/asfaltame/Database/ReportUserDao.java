package com.dam.asfaltame.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dam.asfaltame.Model.ReportUser;

import java.util.List;

@Dao
public interface ReportUserDao {
    @Query("SELECT * FROM ReportUser")
    List<ReportUser> getAll();

    @Query("SELECT * FROM ReportUser WHERE r_id LIKE :reportId AND u_id LIKE :userId")
    List<ReportUser> getReportUser(long reportId, long userId);

    @Insert
    long insert(ReportUser ru);

    @Update
    void update(ReportUser ru);

    @Delete
    void delete(ReportUser ru);
}
