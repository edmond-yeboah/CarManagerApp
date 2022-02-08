package com.example.carmanagerapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Objects;

public class CarDetailsBottomSheet extends BottomSheetDialogFragment {

    private View view;
    private String make,model,transmission,color,chassis,date;
    private TextView carmake,carmodel,cartransmission,carcolor,carchassis,cardate,bothnames;
    private SwitchMaterial oil,air,tyre;

    static CarDetailsBottomSheet newInstance(){
        return new CarDetailsBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cardetailsbottomsheet,container,false);

        carchassis = view.findViewById(R.id.btscarchassis);
        carcolor = view.findViewById(R.id.btscarcolor);
        cardate = view.findViewById(R.id.btscaraddedon);
        carmake = view.findViewById(R.id.btscarmake);
        carmodel = view.findViewById(R.id.btscarmodel);
        cartransmission = view.findViewById(R.id.btscartransmission);
        bothnames = view.findViewById(R.id.bothnames);
        oil = view.findViewById(R.id.oilchange);
        tyre = view.findViewById(R.id.tyrechange);
        air = view.findViewById(R.id.airchange);

        createNotificationChannel();

        Bundle bundle = getArguments();
        if (bundle !=null){
            make = bundle.getString("name");
            model = bundle.getString("model");
            transmission = bundle.getString("transmission");
            color = bundle.getString("color");
            chassis = bundle.getString("chassis");
            date = bundle.getString("addedon");


            carmake.setText(make);
            carchassis.setText(chassis);
            carcolor.setText(color);
            cardate.setText(date);
            carmodel.setText(model);
            cartransmission.setText(transmission);
            bothnames.setText(String.format("%s %s", model, make));
        }

        oil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Intent intent = new Intent(getContext(),ReminderBroadcast.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0,intent,0);

                    AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
                    long curenttime = System.currentTimeMillis();
                    long fivesecs = 1000 * 5;

                    alarmManager.set(AlarmManager.RTC_WAKEUP,
                            curenttime + fivesecs,
                            pendingIntent);

                }else {
                    //do nothing
                }
            }
        });

        air.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Intent intent = new Intent(getContext(),ReminderBroadcast.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0,intent,0);

                    AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
                    long curenttime = System.currentTimeMillis();
                    long fivesecs = 1000 * 5;

                    alarmManager.set(AlarmManager.RTC_WAKEUP,
                            curenttime + fivesecs,
                            pendingIntent);
                }
            }
        });

        tyre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Intent intent = new Intent(getContext(),ReminderBroadcast.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0,intent,0);

                    AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
                    long curenttime = System.currentTimeMillis();
                    long fivesecs = 1000 * 5;

                    alarmManager.set(AlarmManager.RTC_WAKEUP,
                            curenttime + fivesecs,
                            pendingIntent);
                }
            }
        });

        return view;
    }

    private  void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Carchannel";
            String description = "Channel for car maintenance reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("notifyme",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
