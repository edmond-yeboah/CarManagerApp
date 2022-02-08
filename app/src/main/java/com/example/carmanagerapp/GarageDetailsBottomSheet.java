package com.example.carmanagerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class GarageDetailsBottomSheet extends BottomSheetDialogFragment {

    private View view;
    private TextView name,closingtime,openingtime,address,date,disname;
    private String carname,carclosetime,caropentime,caraddress,cardate;

    static BottomSheetDialogFragment newInstance(){
        return  new BottomSheetDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.garagedetailslayout,container,false);

        Bundle bundle = getArguments();

        name = (TextView)view.findViewById(R.id.btsgaragenames);
        disname = (TextView)view.findViewById(R.id.btsgaragename);
        closingtime = (TextView)view.findViewById(R.id.btsclosetime);
        openingtime = (TextView)view.findViewById(R.id.btsopentime);
        date = (TextView)view.findViewById(R.id.btsdate);
        address = (TextView)view.findViewById(R.id.btsaddress);

        if (bundle!=null){
            carname = bundle.getString("name");
            caraddress = bundle.getString("address");
            carclosetime = bundle.getString("close");
            caropentime = bundle.getString("open");
            cardate = bundle.getString("addeon");

            name.setText(carname);
            disname.setText(carname);
            closingtime.setText(carclosetime);
            openingtime.setText(carclosetime);
            date.setText(cardate);
            address.setText(caraddress);
        }
        return view;
    }
}
