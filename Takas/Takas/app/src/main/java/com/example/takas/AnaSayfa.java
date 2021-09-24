package com.example.takas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.takas.ui.Ilanlar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnaSayfa#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnaSayfa extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private RecyclerView recyclerView;
    private  IlanAdapter ilanAdapter;
    private List<Ilanlar> ilanlarList;
    Spinner spin_il,spin_kategori;
    ArrayAdapter<String> dataAdapterIller;
    ArrayAdapter<String> dataAdapterKategoriler;
    String il,kategori;
    public String [] iller={"İl","Antalya", "Ankara", "Eskişehir","İstanbul", "Konya"};
    public String [] kategoriler={"Kategoriler","Elektronik", "Giyim", "Kırtasiye", "Kitap","Mobilya","Organik Ürünler","Diğer"};
    DatabaseReference kokreferans;



    public AnaSayfa() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnaSayfa.
     */
    // TODO: Rename and change types and number of parameters
    public static AnaSayfa newInstance(String param1, String param2) {
        AnaSayfa fragment = new AnaSayfa();
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
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_ana_sayfa, container, false);
        kokreferans=FirebaseDatabase.getInstance().getReference();

        recyclerView = root.findViewById(R.id.AnaSayfa);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        ilanlarList = new ArrayList<>();
        ilanAdapter = new IlanAdapter((getContext()),ilanlarList);
        recyclerView.setAdapter(ilanAdapter);
        ilanOku();


        root.findViewById(R.id.arama_FAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.findViewById(R.id.arama_button).setVisibility(View.VISIBLE);
                root.findViewById(R.id.arama_il_spinner).setVisibility(View.VISIBLE);
                root.findViewById(R.id.arama_kategori_spinner).setVisibility(View.VISIBLE);
                root.findViewById(R.id.arama_il_text).setVisibility(View.VISIBLE);
                root.findViewById(R.id.arama_kategori_text).setVisibility(View.VISIBLE);
                root.findViewById(R.id.AnaSayfa).setVisibility(View.INVISIBLE);
                root.findViewById(R.id.takasText).setVisibility(View.INVISIBLE);

            }
        });


        spin_il = root.findViewById(R.id.arama_il_spinner);
        spin_kategori = root.findViewById(R.id.arama_kategori_spinner);
        dataAdapterIller = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,iller);
        dataAdapterIller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_il.setAdapter(dataAdapterIller);
        dataAdapterKategoriler = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,kategoriler);
        dataAdapterKategoriler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_kategori.setAdapter(dataAdapterKategoriler);
        spin_il.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                il=parent.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spin_kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kategori=parent.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        root.findViewById(R.id.arama_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Ilanlar");
                databaseReference.orderByChild("gonderiIl").equalTo(il)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.clear();
                          ilanlarList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Ilanlar ilanlar = dataSnapshot.getValue(Ilanlar.class);
                            hashMap.put(dataSnapshot.getKey(),dataSnapshot.getValue());
                            ilanlarList.add(ilanlar);
                            kokreferans.child("aramaGecici").setValue(hashMap);
                        }
                        ilanAdapter.notifyDataSetChanged();
                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("aramaGecici");
                        databaseReference2.orderByChild("gonderiKategori").equalTo(kategori)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                         ilanlarList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Ilanlar ilanlar = dataSnapshot.getValue(Ilanlar.class);
                            ilanlarList.add(ilanlar);
                            root.findViewById(R.id.AnaSayfa).setVisibility(View.VISIBLE);
                        }
                        ilanAdapter.notifyDataSetChanged();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    return root;
    }

    private void ilanOku(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Ilanlar");
        databaseReference.addValueEventListener(new ValueEventListener() {
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