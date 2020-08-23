package com.southsalt.blerakecontroller;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LeDeviceListAdapter extends RecyclerView.Adapter<LeDeviceListAdapter.LeDeviceViewHolder> {
    public static class LeDeviceViewHolder extends RecyclerView.ViewHolder {
        private final TextView DeviceNameView;
        private final TextView DeviceAddressView;

        private LeDeviceViewHolder(View v) {
            super(v);
            DeviceNameView = v.findViewById(R.id.device_name);
            DeviceAddressView = v.findViewById(R.id.device_address);
        }
    }

    private ArrayList<BluetoothDevice> mDevices; // Cached copy of Programs

    LeDeviceListAdapter(ArrayList<BluetoothDevice> devices) {
        setHasStableIds(true);
        mDevices = devices;

    }

    @Override
    public LeDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_view_item, parent, false);
        LeDeviceViewHolder holder = new LeDeviceViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeDeviceViewHolder holder, int position) {
        if (mDevices != null) {
            BluetoothDevice current = mDevices.get(position);
            holder.DeviceNameView.setText(current.getName());
            holder.DeviceAddressView.setText(current.getAddress());
        } else {
            // Covers the case of data not being ready yet.
            holder.DeviceNameView.setText("No Devices");
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (mDevices != null)
            return mDevices.size();
        else {
            return 0;
        }
    }
}

