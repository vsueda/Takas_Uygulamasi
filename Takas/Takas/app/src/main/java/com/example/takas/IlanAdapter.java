package com.example.takas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.takas.ui.Ilanlar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class IlanAdapter extends RecyclerView.Adapter<IlanAdapter.ViewHolder>{

    public Context mContext;
    public List<Ilanlar> mIlan;
    public String kullaniciMail,kullaniciTel;
    private DatabaseReference kokReference;
    private FirebaseUser user;
    public IlanAdapter(Context mContext, List<Ilanlar> mIlan) {
        this.mContext = mContext;
        this.mIlan = mIlan;
    }
    private void kullaniciBilgi(String kullaniciId, TextView isim_soyisim){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(kullaniciId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("isim").getValue().toString() + " " + snapshot.child("soyisim").getValue().toString();
                isim_soyisim.setText(name);
                 kullaniciMail = snapshot.child("mail").getValue().toString();
                 kullaniciTel = snapshot.child("tel").getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void ilanBilgi(String goneriId, TextView baslik, TextView il, TextView kategori, TextView aciklama){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Ilanlar").child(goneriId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sBaslik = snapshot.child("gonderiBaslik").getValue().toString();
                baslik.setText(sBaslik);
                String sIl = snapshot.child("gonderiIl").getValue().toString();
                il.setText(sIl);
                String sKategori = snapshot.child("gonderiKategori").getValue().toString();
                kategori.setText(sKategori);
                String sAciklama = snapshot.child("goneriAciklama").getValue().toString();
                aciklama.setText(sAciklama);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ilanlar,parent,false);
        return new IlanAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    user = FirebaseAuth.getInstance().getCurrentUser();
    Ilanlar ilanlar = mIlan.get(position);
        Glide.with(mContext).load(ilanlar.getGonderiResim()).into(holder.ilan_img);
        kullaniciBilgi(ilanlar.getKullaniciId(),holder.ilan_isim_soyisim);
        ilanBilgi(ilanlar.getGoneriId(),holder.ilan_baslik,holder.ilan_yer,holder.ilan_kategori,holder.ilan_aciklama);
        holder.ilan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("İletişim");
                builder.setMessage("Lütfen iletişim kurmak istediğiniz alanı seçiniz.");
                builder.setPositiveButton("E-mail", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Takas Teklifi");                                                                    //Email konusu
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Bu Mail Takas uygulaması aracılığı ile gönderilmektedir.");                       //Email içeriği
                        mContext.startActivity(Intent.createChooser(emailIntent, "E-mail Göndermek için Seçiniz:"));                              //birden fazla email uygulaması varsa seçmek için
                        String aEmailList[] = { kullaniciMail};                                                                                              //Mail gönderielecek kişi.Birden fazla ise virgülle ayırarak yazılır
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
                        mContext.startActivity(emailIntent);
                    }
                });
                builder.setNegativeButton("Tel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent mesajGonder = new Intent(Intent.ACTION_VIEW);
                        mesajGonder.setData(Uri.parse("sms:" + kullaniciTel));
                        mesajGonder.putExtra("sms_body", "Bu mesaj Takas uygulaması aracılığı ile gönderilmektedir.");
                        mContext.startActivity(mesajGonder);
                    }
                });
                AlertDialog goster = builder.create();
                goster.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mIlan.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ilan_img;
        public TextView ilan_baslik, ilan_yer, ilan_kategori, ilan_isim_soyisim, ilan_aciklama;
        public Button ilan_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ilan_img = itemView.findViewById(R.id.listeleImage);
            ilan_baslik = itemView.findViewById(R.id.listeleBaslik);
            ilan_yer = itemView.findViewById(R.id.listeleYer);
            ilan_kategori = itemView.findViewById(R.id.listeleKategori);
            ilan_isim_soyisim = itemView.findViewById(R.id.listeleIsimSoyIsim);
            ilan_aciklama = itemView.findViewById(R.id.listeleAciklama);
            ilan_button = itemView.findViewById(R.id.listeleButton);

        }


    }


}
