package com.example.galeriapublica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//Toda vez que uma Activity é criada, ela já aparece automaticamente no AndroidManifest, o que não ocorre na criação de um fragment
public class MainActivity extends AppCompatActivity {

    static int RESULT_REQUEST_PERMISSION = 2;
    static int ADD_PRODUCT_ACTIVITY_RESULT = 1;

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityViewModel vm = new ViewModelProvider(this).get(MainActivityViewModel.class);

        bottomNavigationView = findViewById(R.id.btNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {  //No momento em que a main acitivity cria os fragmentos, os mesmos criam a interface internamente
                switch (item.getItemId()){
                    case R.id.gridViewOp:
                        GridFragment gridFragment = GridFragment.newInstance();
                        setFragment(gridFragment);
                        vm.setNavigationOpSelected(R.id.gridViewOp);
                        break;
                    case R.id.listViewOp:
                        ListFragment listFragment = ListFragment.newInstance();
                        setFragment(listFragment);
                        vm.setNavigationOpSelected(R.id.listViewOp);
                        break;
                }
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        checkForPermissions(permissions);
    }

    void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction =   getSupportFragmentManager().beginTransaction();  //Toda activiy tem um fragment transaction
        fragmentTransaction.replace(R.id.fragContainer, fragment);  //Dentro do fragmentContainer eu coloco o fragment
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    void loadImageData(){   //Visualiza em que momento as imagens são carregadas
        Log.i("Galeria Publica", "Imagens carregadas da galeria publica do celular.");

        MainActivityViewModel vm = new ViewModelProvider(this).get(MainActivityViewModel.class);
        HashMap<Long, ImageData> imageDataList = vm.getImageDataList();

        int w = (int) getResources().getDimension(R.dimen.im_width);
        int h = (int) getResources().getDimension(R.dimen.im_height);

        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.SIZE
        };
        String selection = null;
        String selectionArgs[] = null;
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " ASC";

        try {
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder);

            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                if (!imageDataList.containsKey(id)){
                    String name = cursor.getString(nameColumn);
                    int date = cursor.getInt(dateAddedColumn);
                    int size = cursor.getInt(sizeColumn);

                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                    Bitmap thumb = Util.getBitmap(MainActivity.this, imageUri, w, h);

                    imageDataList.put(id, new ImageData(thumb, name, new Date(date), size));
                }
            }
        }catch(FileNotFoundException e){
                e.printStackTrace();
        }
        bottomNavigationView.setSelectedItemId(vm.getNavigationOpSelected());
        }


    private void checkForPermissions(List<String> permissions){
        List<String> permissionsNotGranted = new ArrayList<>();

        for (String permission : permissions){
            if ( !hasPermission(permission)){
                permissionsNotGranted.add(permission);
            }
        }

        if(permissionsNotGranted.size() > 0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]), RESULT_REQUEST_PERMISSION);
            }
        }
        else{
            loadImageData();
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

        else{
            loadImageData();
        }

    }
}