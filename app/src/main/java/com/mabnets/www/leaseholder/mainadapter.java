package com.mabnets.www.leaseholder;

import android.app.FragmentManager;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class mainadapter extends RecyclerView.Adapter<mainadapter.mainHolder> {
    private Context context;
    private ArrayList mainItems;


    public mainadapter(Context context,ArrayList<mainitems> mainitems){
        this.context=context;
        this.mainItems=mainitems;
    }
    @Override
    public mainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater Inflat0r=LayoutInflater.from(parent.getContext());
        View View =Inflat0r.inflate(R.layout.mainpointuser,parent,false);
        mainHolder iv=new mainHolder(View);
        return iv;
    }

    @Override
    public void onBindViewHolder(mainHolder holder, int position) {
       final mainitems mainitems=(mainitems) mainItems.get(position);
        holder.laname.setText(mainitems.names);
        holder.laphone.setText(mainitems.phone);
        ImageLoader.getInstance().displayImage("http://10.0.2.2/Lease/"+mainitems.image,holder.laimages);
        if(mainitems.lpic.equals("")) {
            holder.cimg.setImageResource(R.drawable.person);
        }else{
            ImageLoader.getInstance().displayImage("http://10.0.2.2/Lease/"+mainitems.image,holder.cimg);
        }

        holder.ladescription.setText(mainitems.description);
        holder.chatbtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               android.support.v4.app.FragmentManager manager=((AppCompatActivity)context).getSupportFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("lid",mainitems.lid);
                bundle.putString("names",mainitems.names);
                Fragment fragment=new chatcustomer();
                 fragment.setArguments(bundle);
                manager.beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();


            }
        });
        holder.labutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager manager=((AppCompatActivity)context).getSupportFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("lidd",mainitems.lid);
                bundle.putString("houseid",mainitems.photosid);
                Fragment fragment=new view_houses();
                fragment.setArguments(bundle);
                manager.beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        if(mainItems!=null){
            return mainItems.size();
        }
        return 0;
    }


    public static class mainHolder extends RecyclerView.ViewHolder{
        public ImageView cimg;
        public TextView laname;
        public TextView laphone;
        public ImageView laimages;
        public TextView ladescription;
        public ImageButton chatbtnn;
        public Button labutton;
        public mainHolder(View itemView) {
            super(itemView);
          cimg=(ImageView)itemView.findViewById(R.id.landimage);
          laname=(TextView)itemView.findViewById(R.id.landname);
          laphone=(TextView)itemView.findViewById(R.id.landphone);
          laimages=(ImageView)itemView.findViewById(R.id.landmainimg);
            ladescription=(TextView)itemView.findViewById(R.id.landdescription);
            labutton=(Button)itemView.findViewById(R.id.landphotos);
            chatbtnn=(ImageButton)itemView.findViewById(R.id.chatbtnn);
        }
    }


}
