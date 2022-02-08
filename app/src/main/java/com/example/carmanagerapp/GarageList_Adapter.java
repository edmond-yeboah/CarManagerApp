package com.example.carmanagerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GarageList_Adapter extends RecyclerView.Adapter<GarageList_Adapter.GarageList_AdapterVH> {
    private List<GarageClass> garageClassList;
    private Context context;
    private SelectedGarage selectedgarage;

    public GarageList_Adapter(List<GarageClass> garageClassList, SelectedGarage selectedgarage) {
        this.garageClassList = garageClassList;
        this.selectedgarage = selectedgarage;
    }

    @NonNull
    @Override
    public GarageList_Adapter.GarageList_AdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        return new GarageList_AdapterVH(LayoutInflater.from(context).inflate(R.layout.garagelist_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull GarageList_Adapter.GarageList_AdapterVH holder, int position) {
        GarageClass garageClass = garageClassList.get(position);
        String name = garageClass.getName();
        String date = garageClass.getAddedon();

        holder.garagedate.setText(date);
        holder.namegarage.setText(name);

    }

    @Override
    public int getItemCount() {
        return garageClassList.size();
    }

    public interface SelectedGarage{
        void selectedgarage(GarageClass garageClass);

    }

    public class GarageList_AdapterVH extends RecyclerView.ViewHolder {
        TextView namegarage,garagedate;
        public GarageList_AdapterVH(@NonNull View itemView) {
            super(itemView);
            namegarage = itemView.findViewById(R.id.namegarage);
            garagedate = itemView.findViewById(R.id.garagedate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedgarage.selectedgarage(garageClassList.get(getBindingAdapterPosition()));
                }
            });
        }
    }
}
