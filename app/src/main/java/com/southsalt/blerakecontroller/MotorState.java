package com.southsalt.blerakecontroller;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MotorState {
    @PrimaryKey
    private int id;
    private int programId;

    private int speed;
    private int time;
    private int dir; //Direction either 0 or 1
    private int gate; //Gate either 0 or 1

    public MotorState(int id,int programId ,int speed,int time,int dir,int gate){
        this.id = id;
        this.programId = programId;
        this.speed = speed;
        this.time = time;
        this.dir = dir;
        this.gate = gate;
    }

    public int getId(){return this.id;}
    public int getProgramId(){return this.programId;}
    public int getSpeed(){return this.speed;}
    public int getTime(){return this.time;}
    public int getDir(){return this.dir;}
    public int getGate(){return this.gate;}

}
