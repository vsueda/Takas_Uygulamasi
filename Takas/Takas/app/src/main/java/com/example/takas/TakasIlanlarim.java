package com.example.takas;

import android.net.sip.SipSession;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.takas.ui.Ilanlar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TakasIlanlarim#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TakasIlanlarim extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private RecyclerView recyclerView;
    private  IlanAdapter ilanAdapter;
    private List<Ilanlar> ilanlarList;
    private DatabaseReference kokreferans;
    private FirebaseAuth auth;
    private String userID;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TakasIlanlarim() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TakasIlanlarim.
     */
    // TODO: Rename and change types and number of parameters
    public static TakasIlanlarim newInstance(String param1, String param2) {
        TakasIlanlarim fragment = new TakasIlanlarim();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_takas_ilanlarim, container, false);
        kokreferans= FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        recyclerView = root.findViewById(R.id.TakasIlanlarim);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        ilanlarList = new ArrayList<>();
        ilanAdapter = new IlanAdapter((getContext()),ilanlarList);
        recyclerView.setAdapter(ilanAdapter);
        ilanOku();
        return root;
    }


    private void ilanOku(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Ilanlar");
        databaseReference.orderByChild("kullaniciId").equalTo(userID).
        addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ilanlarList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Ilanlar ilanlar = dataSnapshot.getValue(Ilanlar.class);
                    ilanlarList.add(ilanlar);

                }
                ilanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}