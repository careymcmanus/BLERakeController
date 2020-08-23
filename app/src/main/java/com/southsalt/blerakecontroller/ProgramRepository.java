package com.southsalt.blerakecontroller;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ProgramRepository {

    private ProgramDao mProgramDao;
    private LiveData<List<ProgramData>> mAllProgramData;


    // Note that in order to unit test the ProgramRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    ProgramRepository(Application application) {
        ProgramRoomDatabase db = ProgramRoomDatabase.getDatabase(application);
        mProgramDao = db.programDao();
        mAllProgramData = mProgramDao.getProgramData();



    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<ProgramData>> getProgramData() { return mAllProgramData;}

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Program[] programs) {
        ProgramRoomDatabase.databaseWriteExecutor.execute(() -> {
            mProgramDao.insertProgram(programs);
        });
    }
    void insert(Program program){
        ProgramRoomDatabase.databaseWriteExecutor.execute(() -> {
            mProgramDao.insertProgram(program);
        });
    }

    void insert(MotorState[] states){
        ProgramRoomDatabase.databaseWriteExecutor.execute(() -> {
            mProgramDao.insertStates(states);
        });
    }
}
