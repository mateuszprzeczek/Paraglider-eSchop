package com.hfad.kociczat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RepertoireFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView repertoireRecycler = (RecyclerView) inflater.inflate(
                R.layout.fragment_repertoire, container, false);

        String[] performanceName = new String[Spectacle.performances.length];
        for (int i = 0; i < performanceName.length; i++) {
            performanceName[i] = Spectacle.performances[i].getName();
        }
        int[] performanceImages = new int[Spectacle.performances.length];
        for (int i = 0; i < performanceImages.length; i++) {
            performanceImages[i] = Spectacle.performances[i].getImageResourceId();
        }
        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(performanceName, performanceImages);
        repertoireRecycler.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        repertoireRecycler.setLayoutManager(layoutManager);
        return repertoireRecycler;
    }
}