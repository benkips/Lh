package com.mabnets.www.leaseholder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class mgmtAdapter extends RecyclerView.Adapter<mgmtAdapter.mgmtViewholder> {

    private Context context;
    private ArrayList mgmtitems;

    public  mgmtAdapter(Context context,ArrayList<mgmtt> mgmtitem){
    this.context=context;
    this.mgmtitems=mgmtitem;
    }

    @Override
    public mgmtViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater Inflat0r=LayoutInflater.from(parent.getContext());
        View view =Inflat0r.inflate(R.layout.housemgmt,parent,false);
        mgmtViewholder iv=new mgmtViewholder(view);


        return iv;
    }

    @Override
    public void onBindViewHolder(mgmtViewholder holder, int position) {
        final mgmtt mgmtt= (mgmtt) mgmtitems.get(position);
        ImageLoader.getInstance().displayImage("http://10.0.2.2/Lease/"+mgmtt.housepic,holder.hsiv);
        holder.nm.setText(mgmtt.premise);
        holder.dt.setText("created on:"+mgmtt.time);
        
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=((AppCompatActivity)context).getSupportFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putInt("id",mgmtt.id);
                bundle.putInt("number",mgmtt.number);
                Fragment fragment=new Mgmtdetails();
                fragment.setArguments(bundle);
                manager.beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mgmtitems!=null){
            return mgmtitems.size();
        }
        return 0;
    }

    public static class mgmtViewholder extends RecyclerView.ViewHolder{
        public ImageView hsiv;
        public TextView nm;
        public TextView dt;
        public CardView cv;
        public mgmtViewholder(View itemView) {
            super(itemView);
            cv=(CardView)itemView.findViewById(R.id.cvmgmt);
            hsiv=(ImageView)itemView.findViewById(R.id.mghouseiv);
            nm=(TextView)itemView.findViewById(R.id.mgnametv);
            dt=(TextView)itemView.findViewById(R.id.mgdatetv);
        }
    }
}
