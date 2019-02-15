package com.mabnets.www.leaseholder;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class view_proff extends Fragment {

    private Boolean isOpen=false;
    private ConstraintSet constrain1,constrain2;
    private ConstraintLayout constraintLayout;
    private SharedPreferences predd;
    private ImageView imageViewphoto;
    private Mycommand mycommand;
    final String Tag=this.getClass().getName();
    private ProgressDialog pd;
    private String emm;
    private String pasx;
    private TextView name;
    private TextView idno;
    private TextView phone;
    private TextView email;



    public view_proff() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
 View vhh=inflater.inflate(R.layout.fragment_view_proff, container, false);
      /*  Window w=getActivity().getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);*/
        predd=getActivity().getSharedPreferences("Landlord.conf", Context.MODE_PRIVATE);
       emm=predd.getString("landlordemail","");
        pasx =predd.getString("landlordPassword","");


        pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        mycommand=new Mycommand(getContext());
        landdets();

        constrain1=new ConstraintSet();
        constrain2=new ConstraintSet();
        imageViewphoto=vhh.findViewById(R.id.photo);

        name=vhh.findViewById(R.id.nmq);
        idno=vhh.findViewById(R.id.idnoq);
        phone=vhh.findViewById(R.id.phoneq);
        email=vhh.findViewById(R.id.emailq);

        constraintLayout=vhh.findViewById(R.id.constraintlayout);
        constrain2.clone(getContext(),R.layout.view_proff_expand);
        constrain1.clone(constraintLayout);

        imageViewphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpen){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT  ) {
                        TransitionManager.beginDelayedTransition(constraintLayout);
                        constrain2.applyTo(constraintLayout);
                        isOpen=!isOpen;
                    }
                }
                    else{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            TransitionManager.beginDelayedTransition(constraintLayout);
                            constrain1.applyTo(constraintLayout);
                            isOpen=!isOpen;
                        }
                    }
            }
        });


 return vhh;


    }
 private void landdets(){
     String url="http://10.0.2.2/Lease/landalldetails.php";
     StringRequest strrq=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {

                 pd.dismiss();
             if(response.contains("no registered persons under that name")){
                 Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
             }else{
                 ArrayList<landdetails> cvdetilz=new JsonConverter<landdetails>().toArrayList(response,landdetails.class);
                 ArrayList<String> tittle=new ArrayList<String>();
                 for(landdetails value:cvdetilz){
                     tittle.add(value.fullnames);
                     tittle.add(value.idno);
                     tittle.add(value.phone);
                     tittle.add(value.img);
                     tittle.add(value.email);
                 }
                 if(tittle.get(3).equals("")){
                     imageViewphoto.setImageResource(R.drawable.person);

                 }else{
                     name.setText(tittle.get(0));
                     idno.setText(tittle.get(1));
                     phone.setText(tittle.get(2));
                     email.setText(tittle.get(4));
                     com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage("http://10.0.2.2/Lease/"+tittle.get(3),imageViewphoto);
                 }
             }
         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {
             if(error!=null) {
                 Log.d(TAG, error.toString());
                 if (error instanceof TimeoutError) {
                     pd.dismiss();
                     Toast.makeText(getContext(), "error time out ", Toast.LENGTH_SHORT).show();
                 } else if (error instanceof NoConnectionError) {
                     pd.dismiss();
                     Toast.makeText(getContext(), "error no connection", Toast.LENGTH_SHORT).show();
                 } else if (error instanceof NetworkError) {
                     pd.dismiss();
                     Toast.makeText(getContext(), "error network error", Toast.LENGTH_SHORT).show();
                 } else if (error instanceof AuthFailureError) {
                     pd.dismiss();
                     Toast.makeText(getContext(), "errorin Authentication", Toast.LENGTH_SHORT).show();
                 } else if (error instanceof ParseError) {
                     pd.dismiss();
                     Toast.makeText(getContext(), "error while parsing", Toast.LENGTH_SHORT).show();
                 } else if (error instanceof ServerError) {
                     pd.dismiss();
                     Toast.makeText(getContext(), "error  in server", Toast.LENGTH_SHORT).show();
                 } else if (error instanceof ClientError) {
                     pd.dismiss();
                     Toast.makeText(getContext(), "error with Client", Toast.LENGTH_SHORT).show();
                 } else {
                     pd.dismiss();
                     Toast.makeText(getContext(), "error while loading", Toast.LENGTH_SHORT).show();
                 }
             }
         }
     }){
         @Override
         protected Map<String, String> getParams() throws AuthFailureError {
             HashMap<String,String> mop=new HashMap<>();
             mop.put("email",emm);
             mop.put("pass",pasx);
             return mop;
         }
     };
     mycommand.add(strrq);
     pd.show();
     mycommand.execute();
     mycommand.remove(strrq);
 }
}
