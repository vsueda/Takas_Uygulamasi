package com.example.takas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilEkrani#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilEkrani extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private FirebaseAuth auth;
    private DatabaseReference kokReference;
    private String userID;


    private EditText  et_isim, et_soyisim, et_email, et_tel, et_sifre, et_sifreTekrar;
    private Button guncelle_btn;
    private TextView et_isim_p, et_soyisim_p;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfilEkrani() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilEkrani.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilEkrani newInstance(String param1, String param2) {
        ProfilEkrani fragment = new ProfilEkrani();
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

    private void kullaniciBilgileri() {
    kokReference.child("Kullanicilar").child(userID).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
           String kullaniciAdi = snapshot.child("isim").getValue().toString();
            String kullaniciSoyisim = snapshot.child("soyisim").getValue().toString();
            String kullaniciMail = snapshot.child("mail").getValue().toString();
            String kullaniciTel = snapshot.child("tel").getValue().toString();

            et_isim_p.setText(kullaniciAdi+" "+kullaniciSoyisim);
            et_isim.setText(kullaniciAdi);

            et_soyisim.setText(kullaniciSoyisim);
            et_email.setText(kullaniciMail);
            et_tel.setText(kullaniciTel);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profil_ekrani, container, false);
        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        kokReference = FirebaseDatabase.getInstance().getReference();
        et_isim_p = root.findViewById(R.id.Profilİsim);

        et_isim = root.findViewById(R.id.GuncelleIsim);
        et_soyisim = root.findViewById(R.id.GuncelleSoyIsım);
        et_email = root.findViewById(R.id.GuncelleMail);
        et_tel = root.findViewById(R.id.GuncelleTel);
        /*et_sifre = root.findViewById(R.id.kayitSifre);
        et_sifreTekrar = root.findViewById(R.id.kayitSifreTekrar);*/
        guncelle_btn = root.findViewById(R.id.GuncelleButon);
        guncelle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            kullaniciGuncelle();
            }
        });
        kullaniciBilgileri();
        return root;
    }

    private void kullaniciGuncelle() {
        String isim = et_isim.getText().toString();
        String soyisim = et_soyisim.getText().toString();
        String mail = et_email.getText().toString();
        String tel = et_tel.getText().toString();
        /*String sifre = et_sifre.getText().toString();
        String sifreTekrar = et_sifreTekrar.getText().toString();*/

        HashMap<String,String> profil = new HashMap<>();
        profil.put("uid",userID);
        profil.put("isim",isim);
        profil.put("soyisim",soyisim);
        profil.put("mail",mail);
        profil.put("tel",tel);
        kokReference.child("Kullanicilar").child(userID).setValue(profil).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(),"Hesabınız başarılı bir şekile güncelleni.",Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getContext(),"Bir hata oluştu",Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}