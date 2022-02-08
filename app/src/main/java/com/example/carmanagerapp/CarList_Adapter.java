package com.example.carmanagerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarList_Adapter extends RecyclerView.Adapter<CarList_Adapter.CarList_AdapterVH> {
    private List<CarClass> carClassList;
    private Context context;
    private SelectedCar selectedcar;

    public CarList_Adapter(List<CarClass> carClassList, SelectedCar selectedcar) {
        this.carClassList = carClassList;
        this.selectedcar = selectedcar;
    }

    @NonNull
    @Override
    public CarList_Adapter.CarList_AdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CarList_AdapterVH(LayoutInflater.from(context).inflate(R.layout.mycarlist_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull CarList_Adapter.CarList_AdapterVH holder, int position) {
        CarClass carClass = carClassList.get(position);
        String name = carClass.getMake();
        String date = carClass.getDate();
        String model = carClass.getModel();

        holder.carname.setText(String.format("%s %s", model, name));
        holder.cardate.setText(date);

    }

    @Override
    public int getItemCount() {
        return carClassList.size();
    }

    public interface SelectedCar{
        void selectedcar(CarClass carClass);
    }

    public class CarList_AdapterVH extends RecyclerView.ViewHolder {
        TextView carname,cardate;
        public CarList_AdapterVH(@NonNull View itemView) {
            super(itemView);

            carname = itemView.findViewById(R.id.carname);
            cardate = itemView.findViewById(R.id.cardate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedcar.selectedcar(carClassList.get(getBindingAdapterPosition()));
                }
            });
        }
    }
}
