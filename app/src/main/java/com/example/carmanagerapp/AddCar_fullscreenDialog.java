package com.example.carmanagerapp;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class AddCar_fullscreenDialog extends DialogFragment {
    private TextInputLayout make,chassis,model,transmission,color;
    private TextView cancel,addcar;
    private CarClass carClass;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private String owneremail;
    private AVLoadingIndicatorView avi;

    static AddCar_fullscreenDialog newIntance(){
        return new AddCar_fullscreenDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.addcar_layout,container,false);

        Bundle bundle = getArguments();

        if (bundle !=null){
            owneremail = bundle.getString("email");
        }

        //getting the inputs
        make = (TextInputLayout) view.findViewById(R.id.make);
        model = (TextInputLayout) view.findViewById(R.id.model);
        chassis = (TextInputLayout) view.findViewById(R.id.chassis);
        transmission = (TextInputLayout) view.findViewById(R.id.transmission);
        color = (TextInputLayout) view.findViewById(R.id.color);
        cancel = (TextView) view.findViewById(R.id.cancel);
        addcar = (TextView) view.findViewById(R.id.addcar);
        avi = (AVLoadingIndicatorView)view.findViewById(R.id.aviaddcar);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("car");

        addcar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                startAnim();
                //getting data from the edit texts
                String carmake = make.getEditText().getText().toString();
                String carmodel = model.getEditText().getText().toString();
                String carchassis = chassis.getEditText().getText().toString();
                String cartransmission = transmission.getEditText().getText().toString();
                String carcolor = color.getEditText().getText().toString();

                if (TextUtils.isEmpty(carchassis) | TextUtils.isEmpty(carmake) | TextUtils.isEmpty(carmodel) | TextUtils.isEmpty(carcolor) | TextUtils.isEmpty(cartransmission)){
                    stopAnim();
                    Snackbar.make(v,"Fill all fieds",Snackbar.LENGTH_LONG).show();
                }else {
                    //insert into database
                    //get the key
                    LocalDate date = LocalDate.now();
                    String dateformat = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withZone(ZoneId.systemDefault()));
                    String id = reference.push().getKey();
                    carClass = new CarClass(carmake,carmodel,cartransmission,carchassis,carcolor,owneremail,id,dateformat);
                    reference.child(id).setValue(carClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            stopAnim();
                            getDialog().dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            stopAnim();
                            Snackbar.make(v,"Error: "+e,Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    private void stopAnim() {
        avi.hide();
    }

    private void startAnim() {
        avi.smoothToShow();
    }
}
