package com.mabnets.www.leaseholder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class refreshsearch extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout srrr2;
    public refreshsearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View sz=inflater.inflate(R.layout.fragment_refreshsearch, container, false);
        srrr2=(SwipeRefreshLayout)sz.findViewById(R.id.srr);
        srrr2.setOnRefreshListener(this);

        return sz;
    }

    @Override
    public void onRefresh() {
        android.support.v4.app.Fragment fragment=new Search();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();
        srrr2.setRefreshing(false);
    }
}
