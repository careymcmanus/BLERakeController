package com.southsalt.blerakecontroller;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Program {
    @PrimaryKey
    private int id;
    private String name;

    public Program(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){return this.id;}
    public String getName(){return this.name;}
}
