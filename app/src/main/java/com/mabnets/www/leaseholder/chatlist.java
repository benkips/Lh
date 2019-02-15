package com.mabnets.www.leaseholder;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class chatlist extends Fragment {
    private RecyclerView rvchatlist;
    private ProgressDialog progressDialog;
    private Mycommand mycommand;
    private SharedPreferences preff;
    private  String em;
    private  String pass;
    private LinearLayout lychatlist;
    final String Tag=this.getClass().getName();

    public chatlist() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View chatlisti=inflater.inflate(R.layout.fragment_chatlist, container, false);
        preff=getActivity().getSharedPreferences("Landlord.conf", Context.MODE_PRIVATE);
        em=preff.getString("landlordemail","");
        pass=preff.getString("landlordPassword","");

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("loading chats..");
        mycommand=new Mycommand(getContext());
        rvchatlist=(RecyclerView)chatlisti.findViewById(R.id.rvchatlist);
        lychatlist=(LinearLayout)chatlisti.findViewById(R.id.lychatlist);

        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        rvchatlist.setLayoutManager(manager);
        rvchatlist.setHasFixedSize(true);
        show_list();

     return  chatlisti;
    }
    private void show_list(){
        String url = "http://10.0.2.2/Lease/Listingchats.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d(Tag, response);
                if(!response.equals("no messagees availble")){
                    ArrayList<chatslistitems> chatslistitems=new JsonConverter<chatslistitems>().toArrayList(response,chatslistitems.class);
                    chatlistAdapter chatlistAdapter=new chatlistAdapter(getContext(),chatslistitems);
                    rvchatlist.setAdapter(chatlistAdapter);
                }else{
                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                }

                /*Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof TimeoutError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "error time out ", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "error no connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "error network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "errorin Authentication", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "error while parsing", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "error  in server", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ClientError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "error with Client", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "error while loading", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",em);
                params.put("pass",pass);
                return params;
            }
        };
        mycommand.add(stringRequest);
        progressDialog.show();
        mycommand.execute();
        mycommand.remove(stringRequest);
    }
}
