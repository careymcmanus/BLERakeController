package com.southsalt.blerakecontroller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProgramListAdapter extends RecyclerView.Adapter<ProgramListAdapter.ProgramViewHolder> {
    class ProgramViewHolder extends RecyclerView.ViewHolder {
        private final TextView ProgramItemView;

        private ProgramViewHolder(View itemView) {
            super(itemView);
            ProgramItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Program> mPrograms; // Cached copy of Programs

    ProgramListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public ProgramViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ProgramViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProgramViewHolder holder, int position) {
        if (mPrograms != null) {
            Program current = mPrograms.get(position);
            holder.ProgramItemView.setText(current.getName());
        } else {
            // Covers the case of data not being ready yet.
            holder.ProgramItemView.setText("No Program");
        }
    }

    void setPrograms(Program program){
        mPrograms.add(program);
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mPrograms has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mPrograms != null)
            return mPrograms.size();
        else {
            return 0;
        }
    }
}
