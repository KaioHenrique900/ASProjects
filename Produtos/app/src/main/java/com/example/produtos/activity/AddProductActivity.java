package com.example.produtos.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.produtos.R;
import com.example.produtos.model.AddProductViewModel;
import com.example.produtos.util.Config;
import com.example.produtos.util.HttpRequest;
import com.example.produtos.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddProductActivity extends AppCompatActivity {

    static int RESULT_TAKE_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //Quando a tela é girada não perdemos a referência
        AddProductViewModel addProductViewModel = new ViewModelProvider(this).get(AddProductViewModel.class);
        String currentPhotoPath = addProductViewModel.getCurrentPhotoPath();
        if(!(currentPhotoPath.isEmpty())){
            ImageView imvPhoto = findViewById(R.id.imvPhoto);
            Bitmap bitmap = Util.getBitmap(currentPhotoPath, imvPhoto.getWidth(), imvPhoto.getHeight());
            imvPhoto.setImageBitmap(bitmap);
        }

        Button btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.setEnabled(false);

                EditText etName = findViewById(R.id.etProductName);
                String name = etName.getText().toString();
                if (name.isEmpty()){
                    Toast.makeText(AddProductActivity.this, "O campo nome do produto não foi preecnhido", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                EditText etPrice = findViewById(R.id.etPrice);
                String price = etPrice.getText().toString();
                if (price.isEmpty()){
                    Toast.makeText(AddProductActivity.this, "O campo preço do produto não foi preecnhido", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                EditText etDescription = findViewById(R.id.etDescription);
                String description = etDescription.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(AddProductActivity.this, "O campo descrição do produto não foi preecnhido", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                String currentPhotoPath = addProductViewModel.getCurrentPhotoPath();
                if (currentPhotoPath.isEmpty()){
                    Toast.makeText(AddProductActivity.this, "A foto não foi tirada", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                try {
                    Util.scaleImage(currentPhotoPath, 1000, 300);   //altera a escaka
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        HttpRequest httpRequest = new HttpRequest(Config.PRODUCTS_APP_URL + "create_product.php", "POST", "UTF-8");

                        httpRequest.addParam("name", name);
                        httpRequest.addParam("price", price);
                        httpRequest.addParam("description", description);
                        httpRequest.addFile("img", new File(currentPhotoPath));

                        try {
                            InputStream is = httpRequest.execute();
                            String result = Util.inputStream2String(is, "UTF-8");
                            httpRequest.finish();

                            Log.d("HTTP_REQUEST_RESULT", result);

                            JSONObject jsonObject = new JSONObject(result);
                            int success = jsonObject.getInt("success");
                            runOnUiThread(new Runnable() {   //Deve-se alterar a thread caso queira mexer com elementos de interface
                                @Override
                                public void run() {
                                    if(success == 1){
                                        Toast.makeText(AddProductActivity.this, "Produto adicionado com sucesso!", Toast.LENGTH_LONG).show();
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(AddProductActivity.this, "Produto não foi adicionado com sucesso!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        ImageView imvPhoto = findViewById(R.id.imvPhoto);
        imvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispacthTakePictureIntent();
            }
        });
    }

    private void dispacthTakePictureIntent(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File f = null;
        try {
            f = createImageFile();
        } catch (IOException e) {
            Toast.makeText(AddProductActivity.this, "Não foi possível criar o arquivo", Toast.LENGTH_LONG).show();
            return;
        }

        AddProductViewModel addProductViewModel = new ViewModelProvider(this).get(AddProductViewModel.class);
        addProductViewModel.setCurrentPhotoPath(f.getAbsolutePath());

        if(f != null){
            Uri fUri = FileProvider.getUriForFile(AddProductActivity.this, "com.example.produtos.fileprovider", f);
            i.putExtra(MediaStore.EXTRA_OUTPUT, fUri);
            startActivityForResult(i, RESULT_TAKE_PICTURE);
        }

    }

    private File createImageFile() throws IOException {  //Cria o arquivo de imagem e o local onde será guardado
        String timeStamp = new SimpleDateFormat("yyyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File f = File.createTempFile(imageFileName, ".jpg", storageDir);
        return f;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_TAKE_PICTURE){
            AddProductViewModel addProductViewModel = new ViewModelProvider(this).get(AddProductViewModel.class);
            String currentPhotoPath = addProductViewModel.getCurrentPhotoPath();
            if(resultCode == Activity.RESULT_OK){
                ImageView imvPhoto = findViewById(R.id.imvPhoto);
                Bitmap bitmap = Util.getBitmap(currentPhotoPath, imvPhoto.getWidth(), imvPhoto.getHeight());
                imvPhoto.setImageBitmap(bitmap);
            }
            else{   //Caso o usuário desista de tirar a foto
                File f = new File(currentPhotoPath);
                f.delete();
                addProductViewModel.setCurrentPhotoPath("");
            }
        }
    }
}