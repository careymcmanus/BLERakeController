package com.southsalt.blerakecontroller;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ProgramData {
    @Embedded public Program program;
    @Relation(
            parentColumn = "id",
            entityColumn = "programId"
    )

    public List<MotorState> states;
}
