package com.example.minhalista;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

//Quando a tela é rotacionada, MainActivity é destruída, porém MainActivityViewModel não é.
//Mesmo com o OnDestroy sendo chamado no MainActivity, os itens não são destruidos, pois estão localizados no MainActivityViewModel
//O ViewModel não está submisso ao ciclo de vida do MainActivity
public class MainActivityViewModel extends ViewModel {

    List<MyItem> itens = new ArrayList<>();

    public List<MyItem> getItens() {
        return itens;
    }
}
