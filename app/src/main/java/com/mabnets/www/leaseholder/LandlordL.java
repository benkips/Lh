package com.mabnets.www.leaseholder;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class LandlordL extends Fragment {
    private Button signupbtn;
    private Button loginbtn;
    private EditText email;
    private EditText pass;
    private Mycommand mycommand;
    private View snbar;
    private ProgressDialog progressDialog;
    private SharedPreferences pref;
    public LandlordL() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment


        View landL=inflater.inflate(R.layout.fragment_landlord, container, false);
        ((AppCompatActivity)getContext()).getSupportActionBar().setTitle("LeaseHolder");
        email=(EditText)landL.findViewById(R.id.emaillogin);
        pass=(EditText)landL.findViewById(R.id.passlogin);
        signupbtn=(Button)landL.findViewById(R.id.regrefbtn);
        loginbtn=(Button)landL.findViewById(R.id.loginbtnlog);
        mycommand=new Mycommand(getContext());
        snbar=getActivity().findViewById(android.R.id.content);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("processing..");
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag=new LandlordR();
                getFragmentManager().beginTransaction().replace(R.id.framelayout,frag).addToBackStack(null).commit();
               
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String em=email.getText().toString().trim();
              String passs=pass.getText().toString().trim();
              loginnn(em,passs);
            }
        });

        return landL;
    }
    private void loginnn(final String e, final String p){
        if(e.isEmpty()){
            email.setError("email is invalid");
            email.requestFocus();
            return;
        }else if(p.isEmpty()){
           pass.setError("password is invalid");
           pass.requestFocus();
           return;
        }else{
            if(!isvalidemail(e)){
                email.setError("email is invalid");
                email.requestFocus();
                return;
            }else if(p.length()!=6){
                pass.setError("password is invalid");
                pass.requestFocus();
                return;
            }else{
                String url="http://10.0.2.2/Lease/LandlordLogin.php";
                StringRequest str=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                     if(response.contains("welcome")){
                         progressDialog.dismiss();
                         Snackbar.make(snbar,response,Snackbar.LENGTH_LONG).show();
                         pref=getActivity().getSharedPreferences("Landlord.conf", Context.MODE_PRIVATE);
                         SharedPreferences.Editor editorr=pref.edit();
                         editorr.putString("landlordemail",e);
                         editorr.putString("landlordPassword",p);
                         editorr.apply();
                         Fragment frag=new LandlordMenu();
                         getFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                         getFragmentManager().beginTransaction().replace(R.id.framelayout,frag).add(new LandlordL(),"login").addToBackStack("login").commit();
                     }else{
                         progressDialog.dismiss();
                         Snackbar.make(snbar,response,Snackbar.LENGTH_LONG).show();
                     }
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
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> param=new HashMap<>();
                        param.put("email",e);
                        param.put("pass",p);
                        return param;
                    }
                };
                mycommand.add(str);
                progressDialog.show();
                mycommand.execute();
                mycommand.remove(str);

            }
        }
    }
    private final boolean isvalidemail(String target){
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
