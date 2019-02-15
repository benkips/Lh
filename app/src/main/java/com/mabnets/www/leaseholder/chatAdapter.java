package com.mabnets.www.leaseholder;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.chatHolder> {
    private Context context;
    private List<chatmodel> listchat;
    public static final int SENDER=0;
    public static final int RECIEVER=1;

    public chatAdapter(Context context,List<chatmodel> clist){
        this.context=context;
        this.listchat=clist;
    }
    @Override
    public chatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==1){
            View V= LayoutInflater.from(parent.getContext()).inflate(R.layout.leftchats,parent,false);
            chatHolder iv=new chatHolder(V);
            return iv;
        }else{
            View V= LayoutInflater.from(parent.getContext()).inflate(R.layout.rightchats,parent,false);
            chatHolder iv=new chatHolder(V);
            return iv;
        }

    }

    @Override
    public void onBindViewHolder(chatHolder holder, int position) {
        holder.textv.setText(listchat.get(position).messagee);
    }

    @Override
    public int getItemCount() {
      if(listchat!=null){
          return listchat.size();
      }
      return 0;
    }

    @Override
    public int getItemViewType(int position) {
       if(listchat.get(position).issend){
           return SENDER;
       }else{
           return RECIEVER;
       }
    }

    public static class chatHolder extends RecyclerView.ViewHolder{
        public TextView textv;

        public chatHolder(View itemView) {
            super(itemView);
            textv=(TextView)itemView.findViewById(R.id.textmessage);
        }
    }

}

