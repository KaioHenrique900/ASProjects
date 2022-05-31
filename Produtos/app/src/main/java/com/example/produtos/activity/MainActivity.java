package com.example.produtos.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.produtos.R;
import com.example.produtos.adapter.myAdapter;
import com.example.produtos.model.MainViewModel;
import com.example.produtos.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static int RESULT_REQUEST_PERMISSION = 2;
    static int ADD_PRODUCT_ACTIVITY_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            List<String> permissions = new ArrayList<>();
            permissions.add(Manifest.permission.CAMERA);

            checkForPermissions(permissions);

        RecyclerView rvProduct = findViewById(R.id.rvProduct);
        rvProduct.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvProduct.setLayoutManager(layoutManager);

        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        LiveData<List<Product>> products = mainViewModel.getProducts();
        products.observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                myAdapter myAdapter = new myAdapter(MainActivity.this, products);
                rvProduct.setAdapter(myAdapter);
            }
        });

        Button btnNewProduct = findViewById(R.id.btnNewProduct);
        btnNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddProductActivity.class);
                startActivityForResult(i, ADD_PRODUCT_ACTIVITY_RESULT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PRODUCT_ACTIVITY_RESULT){
            if(resultCode == Activity.RESULT_OK){
                MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
                mainViewModel.refreshProducts();
            }
        }
    }

    private void checkForPermissions(List<String> permissions){
        List<String> permissionsNotGranted = new ArrayList<>();

        for (String permission : permissions){
            if ( !hasPermission(permission)){
                permissionsNotGranted.add(permission);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(permissionsNotGranted.size() > 0){
                requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]), RESULT_REQUEST_PERMISSION);
            }
        }
    }

    private boolean hasPermission(String permission){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){  //Antes da versão 23 não seria necessário esse código
            return ActivityCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
        }
        else{
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        List<String> permissionsRejected = new ArrayList<>();
        if (requestCode == RESULT_REQUEST_PERMISSION) {
            for (String permission : permissions) {
                if (!hasPermission(permission)){
                    permissionsRejected.add(permission);
                }
            }
        }
        if(permissionsRejected.size() > 0){
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                if(shouldShowRequestPermissionRationale(permissionsRejected.get(0))){
                    new AlertDialog.Builder(MainActivity.this).setMessage("Para usar essa aplicação é preciso conceder essas permissões").
                            setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]),RESULT_REQUEST_PERMISSION);
                                }
                            }).create().show();
                }
            }
        }

    }
}