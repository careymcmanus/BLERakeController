package com.southsalt.blerakecontroller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StateListAdapter extends RecyclerView.Adapter<StateListAdapter.StateViewHolder> {
    class StateViewHolder extends RecyclerView.ViewHolder {
        private final TextView StateItemView;

        private StateViewHolder(View itemView) {
            super(itemView);
            StateItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<MotorState> mStates; // Cached copy of States

    StateListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public StateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new StateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StateViewHolder holder, int position) {
        if (mStates != null) {
            MotorState current = mStates.get(position);
            holder.StateItemView.setText(current.getId());
        } else {
            // Covers the case of data not being ready yet.
            holder.StateItemView.setText("No States");
        }
    }

    void setStates(List<MotorState> states){
        mStates = states;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mPrograms has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mStates != null)
            return mStates.size();
        else {
            return 0;
        }
    }
}