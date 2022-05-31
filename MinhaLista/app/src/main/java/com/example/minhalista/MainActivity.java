package com.example.minhalista;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

    public class MainActivity extends AppCompatActivity {

        static int NEW_ITEM_REQUEST = 1;

        MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(LOG_TAG_INFO, "Metodo onStart() criado.");

        //Quando o botão de adicionar é clicado, uma tela é aberta para se selecionar uma imagem, um titulo e uma descrição
        FloatingActionButton fabAddItem = findViewById(R.id.fabAddNewItem);
        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewItemActivity.class);
                startActivityForResult(i, NEW_ITEM_REQUEST);
            }
        });

        MainActivityViewModel vm = new ViewModelProvider(this).get(MainActivityViewModel.class);
        List<MyItem> itens =  vm.getItens();

        myAdapter = new MyAdapter(this, itens);  //cria-se um novo objeto da classe MyAdapter

        RecyclerView rvItens = findViewById(R.id.rvItens);
        rvItens.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvItens.setLayoutManager(layoutManager);

        rvItens.setAdapter(myAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvItens.getContext(), DividerItemDecoration.VERTICAL);
        rvItens.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_ITEM_REQUEST){
            if(resultCode == Activity.RESULT_OK){
                Uri selectedPhotoLocation = data.getData();
                String title = data.getStringExtra("title");
                String description = data.getStringExtra("description");

                MyItem newItem = new MyItem();
                newItem.photo = selectedPhotoLocation;
                newItem.title = title;
                newItem.description = description;

                MainActivityViewModel vm = new ViewModelProvider(this).get(MainActivityViewModel.class);
                List<MyItem> itens =  vm.getItens();

                itens.add(newItem);

                myAdapter.notifyItemInserted(itens.size()-1);
            }
        }
        }

        //Criando os outros métodos que compoem o ciclo de vida da Activity
        //OnCreate: Cria a activity e prepara a interface
        //OnStart: Inicia a activity, gera a interface, mas não permite que oo usuário faça nada nela
        //OnRestart: Reinicia a activity
        //OnResume: Permite que o usuário interaja com a activity
        //OnPause: Pausa a activity, mas sem que a interface seja removida
        //OnStop: A interface é removida, mas a activity ainda não é destruida
        //OnDestroy: Destroi a activity

        //Quando a tela é rotacionada, a activity é destruida para que outra seja construida

        String LOG_TAG_INFO = "Ciclo de Vida MainActivity";
        @Override
        protected void onStart() {
            super.onStart();
            Log.i(LOG_TAG_INFO, "Metodo onStart() criado.");   //Estabelcendo ponto de parada, para que o codigo pare nessa linha quando debugado
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