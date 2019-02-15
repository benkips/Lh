package com.mabnets.www.leaseholder;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.EachExceptionsHandler;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Houses extends Fragment {
    private RecyclerView rvv;
    private Mycommand mycommand;
    private Handler handler;

    public Houses() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
   View h=inflater.inflate(R.layout.fragment_houses, container, false);
        ((AppCompatActivity)getContext()).getSupportActionBar().setTitle("LeaseHolder");
   mycommand=new Mycommand(getContext());
   rvv=(RecyclerView)h.findViewById(R.id.rvhouse);
   handler=new Handler();
   LinearLayoutManager manager=new LinearLayoutManager(getContext());
   rvv.setLayoutManager(manager);
   rvv.setHasFixedSize(true);
    updator();
   return h;
    }
    private void updator(){
        Thread up=new Thread(){
            @Override
            public void run() {
               handler.post(new Runnable() {
                   @Override
                   public void run() {
                       String url="http://10.0.2.2/Lease/leasemain.php";
                PostResponseAsyncTask housetask=new PostResponseAsyncTask(getContext(), new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(!s.isEmpty()) {
                            ArrayList<mainitems> mainitems = new JsonConverter<mainitems>().toArrayList(s, mainitems.class);
                            mainadapter mainadapter = new mainadapter(getContext(), mainitems);
                            rvv.setAdapter(mainadapter);
                        }
            }
        });
        housetask.execute(url);
        housetask.setEachExceptionsHandler(new EachExceptionsHandler() {
                           @Override
                           public void handleIOException(IOException e) {
                               Toast.makeText(getContext(), "internet error", Toast.LENGTH_SHORT).show();
                               android.support.v4.app.Fragment fragment=new refresh_icon();
                               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();
                           }

                           @Override
                           public void handleMalformedURLException(MalformedURLException e) {
                               Toast.makeText(getContext(), " url error ", Toast.LENGTH_SHORT).show();
                           }

                           @Override
                           public void handleProtocolException(ProtocolException e) {
                               Toast.makeText(getContext(), " protocol error", Toast.LENGTH_SHORT).show();
                           }

                           @Override
                           public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                               Toast.makeText(getContext(), " encoding error ", Toast.LENGTH_SHORT).show();
                           }
                       });
                   }
               }) ;
            }
        };
      up.start();
    }
}
