package com.southsalt.blerakecontroller;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProgramViewModel extends AndroidViewModel {
    private ProgramRepository mRepository;
    private LiveData<List<ProgramData>> mProgramData;

    public ProgramViewModel (Application application) {
        super(application);
        mRepository = new ProgramRepository(application);
        mProgramData = mRepository.getProgramData();
    }

    LiveData<List<ProgramData>> getProgramData() {return mProgramData; }

    public void insert(Program program) {mRepository.insert(program);}
    public void insert(Program[] programs) { mRepository.insert(programs);}
    public void insert(MotorState[] states) {mRepository.insert(states);}
}
