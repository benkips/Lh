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
public class refresh_icon extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout srr;
    public refresh_icon() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rfz=inflater.inflate(R.layout.fragment_refresh_icon, container, false);
        srr=(SwipeRefreshLayout)rfz.findViewById(R.id.sr);
        srr.setOnRefreshListener(this);
        return rfz;

    }

    @Override
    public void onRefresh() {
        android.support.v4.app.Fragment fragment=new Houses();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();
        srr.setRefreshing(false);
    }
}
