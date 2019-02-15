package com.mabnets.www.leaseholder;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.utils.L;


/**
 * A simple {@link Fragment} subclass.
 */
public class LandlordMenu extends Fragment {
    private ListView lvone;

    public LandlordMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View Lmenu=inflater.inflate(R.layout.fragment_landlord_menu, container, false);
        lvone=(ListView)Lmenu.findViewById(R.id.lv);
        String [] titlee=getResources().getStringArray(R.array.Landlordmenu);
        String [] Description=getResources().getStringArray(R.array.LandlordDescription);
        SimpleAdapter adapter=new SimpleAdapter(getContext(),titlee,Description);
        lvone.setAdapter(adapter);
        lvone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:{
                        Fragment fragment=new Landlordupload();
                        getFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).addToBackStack("null").commit();
                        break;
                    }
                    case 1:{
                        Fragment fragment=new Landlordmgmt();
                        getFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).addToBackStack("null").commit();
                        break;
                    }
                    case 2:{
                   /*     Fragment fragment=new chats();*/
                        Fragment fragment=new chatlist();
                        getFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).addToBackStack("null").commit();
                        break;
                    }
                    case 3:{
                        Fragment fragment=new Update_profile();
                        getFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).addToBackStack("null").commit();
                        break;
                    }
                    case 4:{
                        Fragment fragment=new view_proff();
                        getFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).addToBackStack("null").commit();
                        break;
                    }
                }
            }
        });
        return Lmenu;
    }
    public class SimpleAdapter extends BaseAdapter{
        private Context mcontext;
        private LayoutInflater layoutInflater;
        private TextView  title,description;
        private String [] titleArray;
        private String [] Description;
        private ImageView imv;

        public SimpleAdapter(Context context,String [] title ,String [] description){
            mcontext=context;
            titleArray=title;
            Description=description;
            layoutInflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return titleArray.length;

        }

        @Override
        public Object getItem(int i) {
            return titleArray[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null){
               view=layoutInflater.inflate(R.layout.menu_items,null);

           }
           title=(TextView)view.findViewById(R.id.tvmenu);
           description=(TextView)view.findViewById(R.id.tvdescription);
           imv=(ImageView)view.findViewById(R.id.ivmenu);
           title.setText(titleArray[i]);
           description.setText(Description[i]);
           if(titleArray[i].equalsIgnoreCase("Register your premises")){
               imv.setImageResource(R.drawable.upload);
           }else if(titleArray[i].equalsIgnoreCase("Manage premise profile")){
               imv.setImageResource(R.drawable.rename);
           }else if(titleArray[i].equalsIgnoreCase("chats")){
               imv.setImageResource(R.drawable.chat);
            }else if(titleArray[i].equalsIgnoreCase("Update my profile")){
               imv.setImageResource(R.drawable.refresh);
           }else{
               imv.setImageResource(R.drawable.view);
           }
           return view;

        }
    }

}
