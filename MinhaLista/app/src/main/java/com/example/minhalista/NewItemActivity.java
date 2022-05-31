package com.example.minhalista;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class NewItemActivity extends AppCompatActivity {

    static int PHOTO_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        Log.i(LOG_TAG_INFO, "Metodo onStart() criado.");

        NewItemActivityViewModel vm = new ViewModelProvider(this).get(NewItemActivityViewModel.class);
        Uri selectPhotoLocation = vm.getSelectPhotoLocation();

        if (selectPhotoLocation != null){
            ImageView imvPhotoPreview = findViewById(R.id.imvPhotoPreview);
            imvPhotoPreview.setImageURI(selectPhotoLocation);
        }
        //Quando este botao é clicado, uma imagem deve ser selecionada
        ImageButton imgChooseImage = findViewById(R.id.imbChooseImage);
        imgChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PHOTO_PICKER_REQUEST);
            }
        });

        //Quando este botao é clicado um item é adicionado
        Button btnAddNewItem = findViewById(R.id.btnAddItem);
        btnAddNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewItemActivityViewModel vm = new ViewModelProvider(NewItemActivity.this).get(NewItemActivityViewModel.class);
                Uri selectPhotoLocation = vm.getSelectPhotoLocation();
                //Caso nenhuma imagem seja selecionada, uma mensagem de erro é emitida
                if (selectPhotoLocation == null){
                    Toast.makeText(NewItemActivity.this, "Voce precisa selecionar uma imagem", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText etTitle = findViewById(R.id.etTitle);
                String title = etTitle.getText().toString();

                //Caso o titulo não seja digitado, uma mensagem de erro é emitida
                if (title.isEmpty()){
                    Toast.makeText(NewItemActivity.this, "Voce precisa definir um titulo", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText etDescription = findViewById(R.id.etDescription);
                String description = etTitle.getText().toString();

                //Caso a descrição não seja digitada, uma mensagem de erro é emitida
                if (description.isEmpty()){
                    Toast.makeText(NewItemActivity.this, "Voce precisa definir uma descrição", Toast.LENGTH_LONG).show();
                    return;
                }

                //Uma intenção é enviada, contendo o local da imagem, o titulo e a descrição
                Intent i = new Intent();
                i.setData(selectPhotoLocation);
                i.putExtra("title", title);
                i.putExtra("description", description);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTO_PICKER_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                Uri selectPhotoLocation = data.getData();
                ImageView imvPhotoPreview = findViewById(R.id.imvPhotoPreview);
                imvPhotoPreview.setImageURI(selectPhotoLocation);

                NewItemActivityViewModel vm = new ViewModelProvider(this).get(NewItemActivityViewModel.class);
                vm.setSelectPhotoLocation(selectPhotoLocation);
            }

        }
    }

    String LOG_TAG_INFO = "Ciclo de Vida NewItemActivity";
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG_INFO, "Metodo onStart() criado.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG_INFO, "Metodo onRestart() criado.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG_INFO, "Metodo onResume() criado.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG_INFO, "Metodo onPause() criado.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG_INFO, "Metodo onStop() criado.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG_INFO, "Metodo onDestroy() criado.");
    }
}