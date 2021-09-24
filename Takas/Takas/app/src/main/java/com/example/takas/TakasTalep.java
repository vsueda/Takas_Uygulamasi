package com.example.takas;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TakasTalep#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TakasTalep extends Fragment {
    Uri img;
    String uri_isim="";
    String kategori, il;
    StorageTask storageTask;
    StorageReference storageReference;
    ImageView img_cikti;
    Spinner spin_il,spin_kategori;
    EditText ET_baslik, ET_aciklama;
    Button btn_img, btn_olustur;
    public String [] iller={"İl","Antalya", "Ankara", "Eskişehir","İstanbul", "Konya"};
    public String [] kategoriler={"Kategoriler","Elektronik", "Giyim", "Kırtasiye", "Kitap","Mobilya","Organik Ürünler","Diğer"};

    ArrayAdapter<String> dataAdapterIller;
    ArrayAdapter<String> dataAdapterKategoriler;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TakasTalep() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TakasTalep.
     */
    // TODO: Rename and change types and number of parameters
    public static TakasTalep newInstance(String param1, String param2) {
        TakasTalep fragment = new TakasTalep();
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
        View root= inflater.inflate(R.layout.fragment_takas_talep, container, false);
        spin_il = root.findViewById(R.id.takasTalepSpinnerIl);
        spin_kategori = root.findViewById(R.id.takasTalepSpinnerKategori);
        ET_aciklama = root.findViewById(R.id.takasTalepUrunAciklama);
        ET_baslik = root.findViewById(R.id.takasTalepBaslik);
        btn_img = root.findViewById(R.id.takasTalepImageBtn);
        btn_olustur = root.findViewById(R.id.takasTalepIlanBtn);
        img_cikti = root.findViewById(R.id.takasTalepImage);
        dataAdapterIller = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,iller);
        dataAdapterIller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_il.setAdapter(dataAdapterIller);
        dataAdapterKategoriler = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,kategoriler);
        dataAdapterKategoriler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_kategori.setAdapter(dataAdapterKategoriler);
        storageReference = FirebaseStorage.getInstance().getReference("Ilanlar");

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



        btn_olustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ilanOlustur();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(getContext(),TakasTalep.this);
            }
        });

    return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            img = result.getUri();
            img_cikti.setImageURI(img);

        }

        else{
            Toast.makeText(getContext(),"Resim seçilmedi.",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    private  String dosyaUzantisi(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }




    private void ilanOlustur() {
        if(img != null){
            StorageReference imageYol = storageReference.child(System.currentTimeMillis()+"-"+ dosyaUzantisi(img));
            storageTask = imageYol.putFile(img);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return imageYol.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        uri_isim = downloadUri.toString();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Ilanlar");
                        String gonderiId=databaseReference.push().getKey();
                        HashMap <String,Object> hashMap = new HashMap<>();
                        hashMap.put("kullaniciId",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("goneriId",gonderiId);
                        hashMap.put("gonderiResim",uri_isim);
                        hashMap.put("gonderiIl",il);
                        hashMap.put("gonderiKategori",kategori);
                        hashMap.put("gonderiBaslik",ET_baslik.getText().toString());
                        hashMap.put("goneriAciklama",ET_aciklama.getText().toString());
                        databaseReference.child(gonderiId).setValue(hashMap);
                    }
                    else{
                        Toast.makeText(getContext(),"Başarısız.",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(getContext(),"Resim yok.",Toast.LENGTH_LONG).show();

        }

    }
}