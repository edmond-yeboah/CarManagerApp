package com.example.carmanagerapp;

import android.app.DownloadManager;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GarageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GarageFragment extends Fragment implements GarageList_Adapter.SelectedGarage{


    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private View view;
    private FirebaseAuth mAuth;
    private String email;
    private List<GarageClass> garageClassList;
    private GarageList_Adapter garageListAdapter;

    public GarageFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static GarageFragment newInstance() {
        return new GarageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        view = inflater.inflate(R.layout.fragment_garage, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.fabgarage);
        recyclerView = (RecyclerView) view.findViewById(R.id.mygaragercv);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        garageClassList = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference("garage").orderByChild("owneremail").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                garageClassList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    GarageClass data = ds.getValue(GarageClass.class);
                    garageClassList.add(data);
                }
//                if (garageClassList.size()<=0){
//                    Snackbar.make(getActivity().findViewById(android.R.id.content),"No garages added",Snackbar.LENGTH_SHORT).show();
//                }
                updateAdapter();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open dialog fragment
                DialogFragment dialogFragment = AddGarage_fullscreenDialog.newInstance();
                dialogFragment.show(getParentFragmentManager(),"tagg");
            }
        });

        return view;
    }

    private void updateAdapter() {
        try {
            garageListAdapter = new GarageList_Adapter(garageClassList,this);
            recyclerView.setAdapter(garageListAdapter);
        }catch (Exception e){
            Snackbar.make(getActivity().findViewById(android.R.id.content),"Error: "+e,Snackbar.LENGTH_SHORT).show();

        }

    }

    @Override
    public void selectedgarage(GarageClass garageClass) {
        Bundle bundle = new Bundle();
        bundle.putString("name",garageClass.getName());
        bundle.putString("address",garageClass.getAddress());
        bundle.putString("open",garageClass.getOpenhours());
        bundle.putString("close",garageClass.getOpenhours());
        bundle.putString("addeon",garageClass.getAddedon());

//        DialogFragment dialogFragment = GarageDetailsBottomSheet.newInstance();
//        dialogFragment.setArguments(bundle);
//        dialogFragment.show(getChildFragmentManager(),"new");

    }
}