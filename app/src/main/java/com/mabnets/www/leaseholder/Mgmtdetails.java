package com.mabnets.www.leaseholder;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Mgmtdetails extends Fragment {
    private int number;
    private int id;
    private Mycommand mycommand;
    private ImageView iv;
    private TextView house;
    private TextView Date;
    private TextView county;
    private TextView Town;
    private TextView Description;
    private RecyclerView rv;
    public Mgmtdetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mgmtdtls= inflater.inflate(R.layout.fragment_mgmtdetails, container, false);
        mycommand=new Mycommand(getContext());
        iv =(ImageView)mgmtdtls.findViewById(R.id.a_mghouseiv);
        house=(TextView)mgmtdtls.findViewById(R.id.a_mgnametv);
        Date=(TextView)mgmtdtls.findViewById(R.id.a_mgdatetv);
        county=(TextView)mgmtdtls.findViewById(R.id.a_mgcounty);
        Town=(TextView)mgmtdtls.findViewById(R.id.a_mgtown);
        Description=(TextView)mgmtdtls.findViewById(R.id.a_mgDescription);
        rv=(RecyclerView)mgmtdtls.findViewById(R.id.rvv);
        LinearLayoutManager manager=new GridLayoutManager(getContext(),2,LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(manager);
        Bundle bundle=getArguments();
        if(bundle!=null){
            number=bundle.getInt("number");
            id=bundle.getInt("id");
            String url="http://10.0.2.2/Lease/specificdetails.php";
            StringRequest rqs=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.contains("There exists no record of pictures")){
                        details(number, id);
                        final AlertDialog.Builder alertt=new AlertDialog.Builder(getContext());
                        alertt.setMessage(response);
                        alertt.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "please upload", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertt.show();
                    }else {

                        details(number, id);
                        ArrayList<mgmthouses> mgmthouses = new JsonConverter<mgmthouses>().toArrayList(response, com.mabnets.www.leaseholder.mgmthouses.class);
                        picsAdapter picsAdapter = new picsAdapter(getContext(), mgmthouses);
                        rv.setNestedScrollingEnabled(false);
                        rv.setHasFixedSize(false);
                        rv.getLayoutManager().setAutoMeasureEnabled(true);
                        rv.setAdapter(picsAdapter);
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String > paramms=new HashMap<>();
                    paramms.put("number",String.valueOf("0"+number));
                    paramms.put("id",String.valueOf(id));
                    return  paramms;
                }
            };
            mycommand.add(rqs);
            mycommand.execute();
            mycommand.remove(rqs);

        }

        return  mgmtdtls;
    }
    private void details(final int no, final int id){

        String url="http://10.0.2.2/Lease/specificover.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<mgmtt> cvdetilz=new JsonConverter<mgmtt>().toArrayList(response,mgmtt.class);
                ArrayList<String> tittle=new ArrayList<String>();
                for(mgmtt value:cvdetilz){
                    tittle.add(value.premise);
                    tittle.add(value.time);
                    tittle.add(value.county);
                    tittle.add(value.town);
                    tittle.add(value.housepic);
                    tittle.add(value.description);

                }
                ImageLoader.getInstance().displayImage("http://10.0.2.2/Lease/"+tittle.get(4),iv);
                house.setText(tittle.get(0));
                Date.setText(tittle.get(1));
                county.setText(tittle.get(2));
                Town.setText(tittle.get(3));
                Description.setText(tittle.get(5));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
               HashMap<String,String> params=new HashMap<>();
               params.put("id",String.valueOf(id));
               params.put("number",String.valueOf("0"+no));
               return params;
            }
        };
        mycommand.add(request);
        mycommand.execute();
        mycommand.remove(request);
    }
}
