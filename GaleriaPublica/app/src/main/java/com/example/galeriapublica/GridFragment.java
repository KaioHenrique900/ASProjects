package com.example.galeriapublica;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridFragment extends Fragment {

    public GridFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static GridFragment newInstance() {
        GridFragment fragment = new GridFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Para criar a parte de interface do fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {  //O recycler view ainda não existe nesse ponto
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grid, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivityViewModel mainActivityViewModel= new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);

        List<ImageData> imageDataList = new ArrayList<>(mainActivityViewModel.getImageDataList().values());

        GridAdapter gridAdapter = new GridAdapter(getContext(), imageDataList);

        float w = getResources().getDimension(R.dimen.im_width);
        int nColumns = Util.calculateNoOfColumns(getContext(), w);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), nColumns);

        RecyclerView rvGrid = getView().findViewById(R.id.rvGrid);
        rvGrid.setAdapter(gridAdapter);
        rvGrid.setLayoutManager(gridLayoutManager);
    }
}