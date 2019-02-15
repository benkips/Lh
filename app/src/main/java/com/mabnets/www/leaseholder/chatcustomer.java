package com.mabnets.www.leaseholder;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
public class chatcustomer extends Fragment {
    private RecyclerView rvcustomerchats;
    private FloatingActionButton fl;
    private EditText etchatcustomer;
    private SharedPreferences preff;
    private Handler handler;
    private  Mycommand mycommand;
    private   String customerid;
    private String landlordid;
    private String lname;

    private List<chatmodel> list_modell=new ArrayList<>();

    public chatcustomer() {
        // Required empty public constructor'
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
       View chatas=inflater.inflate(R.layout.fragment_chatcustomer, container, false);
        ;

       rvcustomerchats=(RecyclerView)chatas.findViewById(R.id.rvchatscustomer);
        fl=(FloatingActionButton)chatas.findViewById(R.id.flchatscustomer);
        etchatcustomer=(EditText)chatas.findViewById(R.id.etchatcustomer);
        mycommand=new Mycommand(getContext());
        preff=getActivity().getSharedPreferences("login.conf",Context.MODE_PRIVATE);
        String phoneno=preff.getString("phone","");
         customerid=preff.getString("customerid","");
        handler=new Handler();
        Toast.makeText(getContext(), phoneno, Toast.LENGTH_SHORT).show();
        Bundle bundle=getArguments();
        if(bundle!=null){
            landlordid=bundle.getString("lid");
            lname=bundle.getString("names");
            Toast.makeText(getContext(), landlordid, Toast.LENGTH_SHORT).show();
        }

        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg=etchatcustomer.getText().toString().trim();
                String url="http://10.0.2.2/Lease/insertsnderchattentant.php";
                StringRequest rqs=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> params=new HashMap<>();
                        params.put("customer",customerid);
                        params.put("landlord",landlordid);
                        params.put("message",msg);
                        return params;


                    }
                };
                mycommand.add(rqs);
                mycommand.execute();
                mycommand.remove(rqs);
                etchatcustomer.setText("");
            }
        });

        refreshmsg();
        chatAdapter chatAdapter = new chatAdapter(getContext(), list_modell);
        rvcustomerchats.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);
        rvcustomerchats.setLayoutManager(manager);




        return  chatas;

    }

    private  void refreshmsg() {
        Thread up=new Thread(){
            @Override
            public void run() {
               handler.post(new Runnable() {
                   @Override
                   public void run() {
                       String url="http://10.0.2.2/Lease/customerchats.php";
                       HashMap<String,String> postdata=new HashMap<>();
                       postdata.put("cid",customerid);
                       postdata.put("lid",landlordid);
                       PostResponseAsyncTask task=new PostResponseAsyncTask(getContext(),postdata, false, new AsyncResponse() {
                           @Override
                           public void processFinish(String s) {
                              /* Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();*/
                               ArrayList<chatitems> navdetails = new JsonConverter<chatitems>().toArrayList(s, chatitems.class);
                               ArrayList<String> title = new ArrayList<String>();
                               /*list_modell.clear();*/
                               for (chatitems value : navdetails) {
                                   if (value.sender.equals(landlordid +"l") ) {
                                       title.add(value.message);
                                       chatmodel chatmodel=new chatmodel(value.message,false);
                                       list_modell.add(chatmodel);
                                       /*rvcustomerchats.setHasFixedSize(true);*/
                                       /*rvcustomerchats.setAdapter(chatAdapter);*/
                                       /*chatAdapter.notifyItemInserted(list_modell.size() - 1);*/
                                   }else {
                                       chatmodel chatmodell = new chatmodel(value.message, true);

                                       list_modell.add(chatmodell);
                                       /*rvcustomerchats.setHasFixedSize(true);*/
                                      /* rvcustomerchats.setAdapter(chatAdapter);*/
                                       /*chatAdapter.notifyItemInserted(list_modell.size() - 1);*/

                                   }

                                   /*rvcustomerchats.scrollToPosition(list_modell.size()-1);*/
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.proffpic,menu);
        ((AppCompatActivity)getContext()).getSupportActionBar().setTitle(lname);
        super.onCreateOptionsMenu(menu, inflater);

    }

}
