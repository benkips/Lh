package com.mabnets.www.leaseholder;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class chats extends Fragment {
    private RecyclerView recyclerV;
     private EditText msgg;
     private FloatingActionButton flsend;
     List<chatmodel> list_modell=new ArrayList<>();
     private SharedPreferences preff;
     private  String em;
     private  String pass;
     private Mycommand mycommand;
     private chatmodel modelsss;
     private  String sid;
    private Handler handler;
    public chats() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View vchats=inflater.inflate(R.layout.fragment_chats, container, false);
       recyclerV=(RecyclerView)vchats.findViewById(R.id.rvchats);
       msgg=(EditText)vchats.findViewById(R.id.etchat);
       flsend=(FloatingActionButton)vchats.findViewById(R.id.flchats);
       mycommand=new Mycommand(getContext());
        handler=new Handler();
        preff=getActivity().getSharedPreferences("Landlord.conf", Context.MODE_PRIVATE);
        em=preff.getString("landlordemail","");
        pass=preff.getString("landlordPassword","");
        Bundle bundle=getArguments();
        if(bundle!=null){
            sid=bundle.getString("sid");
        }
        refreshmsg();
        flsend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

              final String text=msgg.getText().toString();
              sendmessag(text);
              chatmodel model=new chatmodel(text,true);
              list_modell.add(model);
               recyclerV.setHasFixedSize(true);
               LinearLayoutManager manager=new LinearLayoutManager(getContext());
               recyclerV.setLayoutManager(manager);
               /*recyclerV.scrollToPosition(list_modell.size()-1);*/
               chatAdapter chatAdapter=new chatAdapter(getContext(),list_modell);
               recyclerV.setAdapter(chatAdapter);
               msgg.setText("");
               chatmodel modell=new chatmodel("welcome",false);
               list_modell.add(modell);


           }
       });




       return vchats;
    }
    private  void sendmessag(final String message) {
        String url = "http://10.0.2.2/Lease/insertsnderchat.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               /* Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("email",em);
                params.put("pass",pass);
                params.put("msg",message);
                params.put("SID",sid);
                return params;
            }
        };
        mycommand.add(stringRequest);
        mycommand.execute();
        mycommand.remove(stringRequest);
    }


    private  void refreshmsg() {
        Thread up=new Thread(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://10.0.2.2/Lease/retrivechat.php";
                        HashMap<String,String> postdata=new HashMap<>();
                        postdata.put("email",em);
                        postdata.put("pass",pass);
                        postdata.put("SID",sid);
                        PostResponseAsyncTask task=new PostResponseAsyncTask(getContext(),postdata, false, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                /* Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();*/
                                ArrayList<chatitems> navdetails = new JsonConverter<chatitems>().toArrayList(s, chatitems.class);
                                ArrayList<String> title = new ArrayList<String>();
                                list_modell.clear();
                                for (chatitems value : navdetails) {
                                    if (value.sender.equals(sid+"t") ) {
                                        title.add(value.message);
                                        chatmodel chatmodel=new chatmodel(value.message,false);
                                        list_modell.add(chatmodel);
                                        recyclerV.setHasFixedSize(true);
                                        chatAdapter chatAdapter=new chatAdapter(getContext(),list_modell);
                                        recyclerV.setAdapter(chatAdapter);
                                        chatAdapter.notifyItemInserted(list_modell.size() - 1);
                                    }else {
                                        chatmodel chatmodell = new chatmodel(value.message, true);

                                        list_modell.add(chatmodell);
                                        recyclerV.setHasFixedSize(true);
                                        chatAdapter chatAdapter = new chatAdapter(getContext(), list_modell);
                                        recyclerV.setAdapter(chatAdapter);
                                    }
                                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                                    manager.setReverseLayout(true);
                                    recyclerV.setLayoutManager(manager);
                                    /*recyclerV.scrollToPosition(0);*/

                                }


                            }
                        });
                        task.execute(url);
                        handler.postDelayed(this,2000);
                    }

                });
            }
        };
        up.start();
    }
}
