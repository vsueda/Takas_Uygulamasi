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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GirisEkrani extends AppCompatActivity {
    private EditText et_mail, et_sifre;
    private Button giris_btn;
    private FirebaseAuth auth;
    private FirebaseUser user;
    public  void girisYap(){
        String mail = et_mail.getText().toString();
        String sifre = et_sifre.getText().toString();
        if(TextUtils.isEmpty(mail)&&TextUtils.isEmpty(sifre)){
            Toast.makeText(this,"Lütfen e-mail ve şifrenizi girin.",Toast.LENGTH_LONG).show();
        }
        else{
            auth.signInWithEmailAndPassword(mail,sifre).addOnCompleteListener(GirisEkrani.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Toast.makeText(GirisEkrani.this,"Giriş başarılı.",Toast.LENGTH_LONG).show();
                   Intent intent = new Intent(GirisEkrani.this, MainActivity.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   finish();
                   startActivity(intent);
               }
               else{
                   Toast.makeText(GirisEkrani.this,"Bir hata oluştu."+mail+sifre,Toast.LENGTH_LONG).show();
               }
                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_ekrani);
        et_mail = findViewById(R.id.girisMail);
        et_sifre = findViewById(R.id.girisSifre);
        giris_btn = findViewById(R.id.giris_btn);
        auth=FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        giris_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisYap();
            }
        });

        findViewById(R.id.textView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GirisEkrani.this, KayitOl.class);
                startActivity(intent);
            }
        });
    }
}