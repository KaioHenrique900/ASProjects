package com.example.produtos.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.produtos.util.HttpRequest;
import com.example.produtos.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewModel extends ViewModel {
    MutableLiveData<List<Product>> products;  //mutable lvedata é um livedata que se pode alterar

    public LiveData<List<Product>> getProducts(){
        if (products == null){
            products = new MutableLiveData<List<Product>>();   //chamando uma instancia
            loadProducts();
        }

        return products;
    }

    public void refreshProducts(){
        loadProducts();
    }

    void loadProducts(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                List<Product> productsList = new ArrayList<>();
                HttpRequest httpRequest = new HttpRequest("https://productifes.herokuapp.com/get_all_products.php", "GET", "UTF-8");  //url deve ser alterada

                try {
                    InputStream is = httpRequest.execute();   //IntputStrem é um fluxo de dados
                    String result = Util.inputStream2String(is, "UTF-8");
                    httpRequest.finish();

                    Log.d("HTTP_REQUEST_RESULT", result);

                    JSONObject jsonObject = new JSONObject(result);
                    int success = jsonObject.getInt("success");
                    if(success == 1){
                        JSONArray jsonArray = jsonObject.getJSONArray("products");
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject jProduct = jsonArray.getJSONObject(i);

                            String pid = jProduct.getString("pid");
                            String name = jProduct.getString("name");

                            Product product = new Product(pid, name);
                            productsList.add(product);  //min:35
                        }
                        products.postValue(productsList);  //Para uma nova thread
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
