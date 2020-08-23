package com.southsalt.blerakecontroller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Buttons {
    MainActivity mActivity;
    private List<FloatingActionButton> buttons;
    private static final int buttonIDs[] = {
        R.id.selPrgmBtn,
            R.id.fwdBtn,
            R.id.bckBtn,
            R.id.rfshBtn,
            R.id.srtBtn,
            R.id.toSrtBtn,
            R.id.toEndBtn,
            R.id.edStsBtn
    };

    public Buttons(MainActivity activity){
        this.mActivity = activity;
        buttons = new ArrayList<>();
        initBtns();
    }

    private void initBtns(){
        for (int i = 0; i < buttonIDs.length; i++){
            FloatingActionButton tempBtn = mActivity.findViewById(buttonIDs[i]);
            tempBtn.setOnClickListener(onClickListener);
            buttons.add(tempBtn);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case(R.id.selPrgmBtn):
                    break;
                case(R.id.srtBtn):
                    break;
                case(R.id.toSrtBtn):
                    break;
                case(R.id.toEndBtn):
                    break;
                case(R.id.bckBtn):
                    break;
                case(R.id.fwdBtn):
                    break;
                case(R.id.edStsBtn):
                    Intent intent = new Intent(mActivity, EditStatesActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case(R.id.rfshBtn):
                    mActivity.scanLeDevice(true);
                    break;
                default:
                    return;
            }
        }
    };
}
