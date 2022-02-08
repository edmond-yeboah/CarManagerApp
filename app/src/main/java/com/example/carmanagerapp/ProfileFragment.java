package com.example.carmanagerapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements CarList_Adapter.SelectedCar{


    private FloatingActionButton addcarfab;
    private RecyclerView mycarsrcv;
    private FirebaseAuth mAuth;
    private String email;
    private View view;
    private List<CarClass> carClassList;
    private CarList_Adapter carListAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
//        ProfileFragment fragment = new ProfileFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        if (currentuser != null){
            email = currentuser.getEmail();
            Log.d("email", "onCreate: "+email);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        addcarfab = (FloatingActionButton) view.findViewById(R.id.fabaddcar);
        mycarsrcv = (RecyclerView)view.findViewById(R.id.mycarsrcv);

        mycarsrcv.setLayoutManager(new LinearLayoutManager(getContext()));
        mycarsrcv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        carClassList = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference("car").orderByChild("owneremail").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carClassList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    CarClass data = ds.getValue(CarClass.class);
                    carClassList.add(data);
                }

                //checking if user has added exercises
//                if (carClassList.size()<=0){
//                    Snackbar.make(view,"No cars added",Snackbar.LENGTH_LONG).show();
//                    return;
//                }

                updateAdapter();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mycarsrcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && addcarfab.getVisibility() == View.VISIBLE) {
                    addcarfab.hide();
                } else if (dy < 0 && addcarfab.getVisibility() != View.VISIBLE) {
                    addcarfab.show();
                }
            }
        });

        addcarfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                DialogFragment dialogFragment = AddCar_fullscreenDialog.newIntance();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getParentFragmentManager(),"tag");
            }
        });


        return  view;
    }

    private void updateAdapter() {
        try {
            carListAdapter = new CarList_Adapter(carClassList,this);
            mycarsrcv.setAdapter(carListAdapter);
        }catch (Exception e){
            Snackbar.make(view,""+e,Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public void selectedcar(CarClass carClass) {
        Bundle bundle = new Bundle();
        bundle.putString("name",carClass.getMake());
        bundle.putString("chassis",carClass.getChassis());
        bundle.putString("model",carClass.getModel());
        bundle.putString("transmission",carClass.getTransmission());
        bundle.putString("color",carClass.getColor());
        bundle.putString("addedon",carClass.getDate());


        DialogFragment dialogFragment = CarDetailsBottomSheet.newInstance();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getChildFragmentManager(),"taggg");
    }
}