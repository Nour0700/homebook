package com.mabem.homebook.Views.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.mabem.homebook.R;
import com.mabem.homebook.Utils.NavigationDrawer;

public class AboutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((NavigationDrawer) getActivity()).disableNavDrawer();

        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}