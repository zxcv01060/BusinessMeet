package com.example.thematic;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ThematicListAdapter extends RecyclerView.Adapter<ThematicListAdapter.ViewHolder>{
    private Context context;
    private  List<DeviceItem> deviceItems;
    private int timeIndex  = 0;
    private LayoutInflater layoutInflater;
    private DevicesClickListener listener = null;
    ThematicListAdapter(Context context, List<DeviceItem> deviceItems) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.deviceItems = deviceItems;
    }

        // 入口
        @Override
        public  ThematicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 指定 layout
            View view = layoutInflater.inflate(R.layout.matched_device_item, parent, false);
            return new ViewHolder(view);
        }

        // 綁定資料

    @Override
    public void onBindViewHolder(@NonNull ThematicListAdapter.ViewHolder holder, int position) {
        DeviceItem deviceItem = deviceItems.get(position);
        holder.bindDevice(deviceItem.getDeviceName());
    }
    // 返回數目
    @Override
    public int getItemCount() {
        return deviceItems.size();
    }

    void setTimeIndex(int timeIndex){
        this.timeIndex = timeIndex;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgDevices;
        TextView devicesInfo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
//            imgWeather = itemView.findViewById(R.id.imgWeather);
            devicesInfo = itemView.findViewById(R.id.devicesInfo);
            itemView.setOnClickListener(this);
        }

        void bindDevice(String info) {
            devicesInfo.setText(info);

//            time.parameter.setElementName("Wx");
//            String weatherImageId = time.parameter.getValue();
//            int resourceWeatherImageId = context.getResources().getIdentifier(
//                    "ic_weather_" + weatherImageId,
//                    "drawable",
//                    context.getPackageName()
//            );
//            imgWeather.setImageResource(resourceWeatherImageId);
        }


        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onDevicesClick(view, getAdapterPosition());
            }
        }
    }


    void setClickListener(DevicesClickListener devicesClickListener) {
        this.listener = devicesClickListener;
    }

    public interface DevicesClickListener {
        void onDevicesClick(View view, int position);
    }
}

