package com.southsalt.blerakecontroller;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class EditStatesActivity extends AppCompatActivity {

    private ProgramViewModel mProgramViewModel;
    private StateListAdapter stateAdapter;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_states_layout);
        stateAdapter = new StateListAdapter(this);
    }
}
