package com.example.takas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class KayitOl extends AppCompatActivity {
    private EditText et_isim, et_soyisim, et_email, et_tel, et_sifre, et_sifreTekrar;
    private Button kayit_btn;
    private FirebaseAuth auth;
    private DatabaseReference kokReference;
    private String userID;

    private void kullaniciOlustur(){
    String isim = et_isim.getText().toString();
        String soyisism = et_soyisim.getText().toString();
        String mail = et_email.getText().toString();
        String tel = et_tel.getText().toString();
        String sifre = et_sifre.getText().toString();
        String sifreTekrar = et_sifreTekrar.getText().toString();
        if(TextUtils.isEmpty(isim)&&TextUtils.isEmpty(soyisism)&&TextUtils.isEmpty(mail)&&TextUtils.isEmpty(sifre)&&TextUtils.isEmpty(sifreTekrar)){
            Toast.makeText(this,"Lütfen hiçbir alanı boş bırakmayın",Toast.LENGTH_LONG).show();
        }
        else{
            auth.createUserWithEmailAndPassword(mail,sifre).addOnCompleteListener(KayitOl.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        userID = auth.getCurrentUser().getUid();
                        kokReference.child("Kullanicilar").child(userID).setValue("");
                        kullaniciBilgileriKaydet();
                        Toast.makeText(KayitOl.this,"Hesabınız başarılı bir şekile oluştu.",Toast.LENGTH_LONG).show();

                    }
                    else{
                        Toast.makeText(KayitOl.this,"Bir hata oluştu.",Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(KayitOl.this,""+e,Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    private void kullaniciBilgileriKaydet() {
        String isim = et_isim.getText().toString();
        String soyisim = et_soyisim.getText().toString();
        String mail = et_email.getText().toString();
        String tel = et_tel.getText().toString();
        String sifre = et_sifre.getText().toString();
        String sifreTekrar = et_sifreTekrar.getText().toString();
        if(sifre.equals(sifreTekrar)){
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
                        Toast.makeText(KayitOl.this,"Hesabınız başarılı bir şekile oluştu.",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(KayitOl.this, GirisEkrani.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(KayitOl.this,"Bir hata oluştu",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(KayitOl.this,"Şifreleriniz aynı olmalı.",Toast.LENGTH_LONG).show();
        }
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        auth = FirebaseAuth.getInstance();
        et_isim = findViewById(R.id.kayitİsim);
        et_soyisim = findViewById(R.id.kayitSoyisim);
        et_email = findViewById(R.id.kayitMail);
        et_tel = findViewById(R.id.kayitTel);
        et_sifre = findViewById(R.id.kayitSifre);
        et_sifreTekrar = findViewById(R.id.kayitSifreTekrar);
        kayit_btn = findViewById(R.id.kayitButon);
        kokReference = FirebaseDatabase.getInstance().getReference();
        kayit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kullaniciOlustur();
            }
        });
    }
}