package com.southsalt.blerakecontroller;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface ProgramDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProgram(Program[] programs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProgram(Program program);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStates(MotorState[] states);

    @Query("DELETE FROM Program")
    void deletePrograms();
    @Query("DELETE FROM MotorState")
    void deleteStates();

    @Query("SELECT * FROM Program")
    LiveData<List<Program>> getAllPrograms();

    @Query("SELECT * FROM MotorState WHERE ProgramID == :id")
    LiveData<List<MotorState>> getMotorStates(int id);

    @Transaction
    @Query("SELECT * FROM Program")
    LiveData<List<ProgramData>> getProgramData();

}
