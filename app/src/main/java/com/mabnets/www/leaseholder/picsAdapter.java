package com.mabnets.www.leaseholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class picsAdapter extends RecyclerView.Adapter<picsAdapter.picviewHolder> {
    private Context context;
    private ArrayList picItems;

    public picsAdapter(Context context,ArrayList<mgmthouses> picitems){
        this.context=context;
        this.picItems=picitems;
    }

    @Override
    public picviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inf=LayoutInflater.from(parent.getContext());
        View view=inf.inflate(R.layout.pic_details,parent,false);
        picviewHolder ivv=new picviewHolder(view);
        return ivv;
    }

    @Override
    public void onBindViewHolder(picviewHolder holder, int position) {
    mgmthouses mgmthouses=(mgmthouses)picItems.get(position);
        ImageLoader.getInstance().displayImage("http://10.0.2.2/Lease/"+mgmthouses.houseimgs,holder.piciv);



    }

    @Override
    public int getItemCount() {
        if(picItems!=null){
            return picItems.size();
        }
        return 0;
    }

    public static class picviewHolder extends RecyclerView.ViewHolder{
       private ImageView piciv;
        public picviewHolder(View itemView) {
            super(itemView);
            piciv=(ImageView)itemView.findViewById(R.id.pic_pic);
        }
    }
}
