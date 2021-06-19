package com.clemilton.firebaseappsala;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;


public class StorageActivity extends AppCompatActivity {
    //referencia p/ FirebaseStorage
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Button btnUpload,btnGaleria;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        btnUpload = findViewById(R.id.storage_btn_upload);
        imageView = findViewById(R.id.storage_image_cel);
        btnGaleria = findViewById(R.id.storage_btn_galeria);

        btnUpload.setOnClickListener(v ->{
            uploadImagemByte();
        });

        btnGaleria.setOnClickListener( v -> {
            Intent intent = new Intent();
            //intent implicita -> pegar um arquivo do celular
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,1);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public byte[] convertImage2Byte(ImageView imageView){
        //Converter ImageView -> byte[]
        Bitmap bitmap = ( (BitmapDrawable) imageView.getDrawable() ).getBitmap();
        //objeto baos -> armazenar a imagem convertida
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        return baos.toByteArray();
    }
    //fazer um upload de uma imagem convertida p/ bytes
    public void uploadImagemByte(){
        byte[] data = convertImage2Byte(imageView);

        //Criar uma referencia p/ imagem no Storage
        StorageReference imagemRef = storage.getReference()
                                        .child("imagens/01.jpeg");
        //Realiza o upload da imagem
        imagemRef.putBytes(data)
        .addOnSuccessListener(taskSnapshot -> {

            Toast.makeText(this, "Upload feito com sucesso!",
                    Toast.LENGTH_SHORT).show();
            Log.i("UPLOAD","Sucesso");

        })
        .addOnFailureListener(e -> {
            e.printStackTrace();
        });


    }
}
