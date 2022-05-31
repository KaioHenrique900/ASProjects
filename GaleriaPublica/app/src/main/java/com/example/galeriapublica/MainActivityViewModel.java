package com.example.galeriapublica;

import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;

public class MainActivityViewModel extends ViewModel {

    int navigationOpSelected = R.id.gridViewOp;

    HashMap<Long, ImageData> imageDataList = new HashMap<>();

    public HashMap<Long, ImageData> getImageDataList() {
        return imageDataList;
    }

    public int getNavigationOpSelected() {
        return navigationOpSelected;
    }

    public void setNavigationOpSelected(int navigationOpSelected) {
        this.navigationOpSelected = navigationOpSelected;
    }
}
