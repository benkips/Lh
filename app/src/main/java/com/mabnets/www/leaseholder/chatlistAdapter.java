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
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class chatlistAdapter extends RecyclerView.Adapter<chatlistAdapter.chatlistHolder> {
    private Context context;
    private ArrayList chatlistitems;

    public chatlistAdapter (Context context,ArrayList<chatslistitems> chatlistchat) {
    this.context=context;
    this.chatlistitems=chatlistchat;
    }

    @Override
    public chatlistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflat0r=LayoutInflater.from(parent.getContext());
        View view= inflat0r.inflate(R.layout.chatlist,parent,false);
        chatlistHolder iv=new chatlistHolder(view);
        return iv;
    }

    @Override
    public void onBindViewHolder(chatlistHolder holder, int position) {
    final chatslistitems chatslistitems=(chatslistitems)  chatlistitems.get(position);
        ImageLoader.getInstance().displayImage("http://10.0.2.2/Lease/"+chatslistitems.picture,holder.chatlistiv);
        holder.tenat.setText(chatslistitems.name);
        holder.nooftxt.setText(chatslistitems.numbertxt+"messages");
        holder.cvchatits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=((AppCompatActivity)context).getSupportFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("sid",chatslistitems.id);
                Fragment fragment=new chats();
                fragment.setArguments(bundle);
                manager.beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(chatlistitems!=null){
            return  chatlistitems.size();
        }
        return 0;
    }

    public static class chatlistHolder extends RecyclerView.ViewHolder{
        public ImageView chatlistiv;
        public TextView tenat;
        public TextView nooftxt;
        public CardView cvchatits;

        public chatlistHolder(View itemView) {
            super(itemView);
            chatlistiv=(ImageView)itemView.findViewById(R.id.customerchatisiv);
            tenat=(TextView)itemView.findViewById(R.id.tvcustomerchatis);
            nooftxt=(TextView)itemView.findViewById(R.id.tvcustomercount);
            cvchatits=(CardView)itemView.findViewById(R.id.cvchatis);
        }
    }
}
