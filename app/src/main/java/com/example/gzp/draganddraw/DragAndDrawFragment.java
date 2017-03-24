package com.example.gzp.draganddraw;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ben on 2017/3/23.
 */

public class DragAndDrawFragment extends Fragment {

    public static DragAndDrawFragment newInstance() {

//        Bundle args = new Bundle();

        DragAndDrawFragment fragment = new DragAndDrawFragment();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_drag_and_draw, container, false);


        return v;
    }
}
