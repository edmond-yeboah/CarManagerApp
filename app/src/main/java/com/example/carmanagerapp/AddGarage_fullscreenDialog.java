package com.example.carmanagerapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.text.format.DateFormat.format;
import static java.text.DateFormat.*;

public class AddGarage_fullscreenDialog extends DialogFragment {
    private View view;
    private TextView cancelgarage, addgarage, city, tvcity, country, tvcountry, address, tvaddress, location;
    private TextInputLayout garagename;
    private TextView openhrs, closehrs;
    private Context context;
    int Hour, Minute;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private FirebaseAuth mAuth;
    private GarageClass garageClass;
    private AVLoadingIndicatorView avi;
    private DatabaseReference reference;
    private String email,City,Country,Address,addedon,Openhours,Closehours,name;
    private Double lat,lon;


    static AddGarage_fullscreenDialog newInstance() {
        return new AddGarage_fullscreenDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.addgarage_layout, container, false);
        context = getContext();
        cancelgarage = (TextView) view.findViewById(R.id.cancelgarage);
        addgarage = (TextView) view.findViewById(R.id.addgarage);
        garagename = (TextInputLayout) view.findViewById(R.id.garagename);
        openhrs = (TextView) view.findViewById(R.id.openhours);
        closehrs = (TextView) view.findViewById(R.id.closehours);
        country = (TextView) view.findViewById(R.id.country);
        tvcountry = (TextView) view.findViewById(R.id.tvcountry);
        city = (TextView) view.findViewById(R.id.city);
        tvcity = (TextView) view.findViewById(R.id.tvcity);
        avi = (AVLoadingIndicatorView) view.findViewById(R.id.aviaddgarage);
        //address = view.findViewById(R.id.address);
        tvaddress = (TextView) view.findViewById(R.id.tvaddress);
        mAuth = FirebaseAuth.getInstance();
        location = (TextView) view.findViewById(R.id.location);

        reference = FirebaseDatabase.getInstance().getReference("garage");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        if (currentuser != null){
            email = currentuser.getEmail();
            Log.d("email", "onCreate: "+email);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking permision
                startAnim();

                getlocation();
            }
        });

        cancelgarage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        openhrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Hour = hourOfDay;
                                Minute = minute;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, Hour, Minute);

                                Openhours = (String) android.text.format.DateFormat.format("hh:mm aa",calendar);

                                openhrs.setText(android.text.format.DateFormat.format("hh:mm aa", calendar));
                            }
                        }, 12, 0, false

                );
                timePickerDialog.updateTime(Hour, Minute);
                timePickerDialog.show();

            }
        });


        closehrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Hour = hourOfDay;
                                Minute = minute;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, Hour, Minute);

                                Closehours = (String) android.text.format.DateFormat.format("hh:mm aa",calendar);

                                closehrs.setText(android.text.format.DateFormat.format("hh:mm aa", calendar));
                            }
                        }, 12, 0, false

                );
                timePickerDialog.updateTime(Hour, Minute);
                timePickerDialog.show();

            }
        });

        addgarage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                startAnim();
                //getting all the needed variables to save in database
                name = garagename.getEditText().getText().toString();
                if (TextUtils.isEmpty(name)){
                    //Snackbar.make(getView(),"Garage name is empty",Snackbar.LENGTH_LONG).show();
                    stopAnim();
                    return;
                }
                if (TextUtils.isEmpty(Openhours) | TextUtils.isEmpty(Closehours)){
                    //Snackbar.make(getView(),"Set opening and closing hours",Snackbar.LENGTH_LONG).show();
                    stopAnim();
                    return;
                }
                if (TextUtils.isEmpty(Country)){
                    //Snackbar.make(getView(),"Did not get location details",Snackbar.LENGTH_SHORT).show();
                    stopAnim();
                    return;
                }
                Log.d("insert", "onClick: everything okay");
                LocalDate date = LocalDate.now();
                String dateformat = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withZone(ZoneId.systemDefault()));

                String id = reference.push().getKey();
                garageClass = new GarageClass(name,Openhours,Closehours,email,dateformat,lat,lon,Country,City,Address,id);
                reference.child(id).setValue(garageClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        stopAnim();
                        getDialog().dismiss();
                    }
                });

            }
        });

        return view;
    }

    private void startAnim() {
        avi.smoothToShow();
    }


    private void getlocation() {
        Log.d("location", "getlocation: in location");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("location", "getlocation: permission not granted");
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

        }else {
            Log.d("location", "getlocation: permission granted");
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Log.d("location", "onComplete: got location");

                        try {
                            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            lat = addresses.get(0).getLatitude();
                            lon = addresses.get(0).getLongitude();
                            Country = addresses.get(0).getCountryName();
                            City    = addresses.get(0).getLocality();
                            Address = addresses.get(0).getAddressLine(0);

                            tvcountry.setVisibility(View.VISIBLE);
                            country.setVisibility(View.VISIBLE);
                            tvcountry.setText(addresses.get(0).getCountryName());
                            Log.d("location", "onComplete: " + addresses.get(0).getCountryName());

                            tvcity.setVisibility(View.VISIBLE);
                            city.setVisibility(View.VISIBLE);
                            tvcity.setText(addresses.get(0).getLocality());

                            tvaddress.setVisibility(View.VISIBLE);
                           // address.setVisibility(View.VISIBLE);
                            tvaddress.setText(addresses.get(0).getAddressLine(0));
                            stopAnim();
                        } catch (IOException e) {
                            stopAnim();
                            e.printStackTrace();
                            //Snackbar.make(getView(),"Error: "+e,Snackbar.LENGTH_LONG).show();
                        }
                    }else {
                        stopAnim();
                       // Snackbar.make(getView(),"Could not get location",Snackbar.LENGTH_LONG).show();
                    }

                }
            });
        }

    }

    private void stopAnim() {
        avi.hide();
    }
}
