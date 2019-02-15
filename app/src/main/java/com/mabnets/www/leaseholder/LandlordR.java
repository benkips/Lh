package com.mabnets.www.leaseholder;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class LandlordR extends Fragment {
    private TextView loginbtn;
    private EditText fnames;
    private EditText idno;
    private EditText lpassword;
    private EditText Email;
    private EditText phoneee;
    private Button signbtn;
    private ProgressDialog progressDialog;
    private Mycommand mycommand;
    private View sn;
    public LandlordR() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View landr=inflater.inflate(R.layout.fragment_landlord_r, container, false);
        loginbtn=(TextView) landr.findViewById(R.id.loginrefbtn);
        fnames=(EditText) landr.findViewById(R.id.lname);
        idno=(EditText) landr.findViewById(R.id.lidno);
        lpassword=(EditText) landr.findViewById(R.id.lpass);
        Email=(EditText) landr.findViewById(R.id.lemail);
        phoneee=(EditText) landr.findViewById(R.id.lpphone);
        signbtn=(Button) landr.findViewById(R.id.signuplr);
        sn=getActivity().findViewById(android.R.id.content);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("loading..");
        mycommand=new Mycommand(getContext());

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag=new LandlordL();
                getFragmentManager().beginTransaction().replace(R.id.framelayout,frag).addToBackStack(null).commit();
            }
        });
        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String fulln= fnames.getText().toString().trim();
               String idnumber= idno.getText().toString().trim();
               String passs=lpassword.getText().toString().trim();
                String emale=Email.getText().toString().trim();
                String phone2=phoneee.getText().toString().trim();

            landreg(fulln,idnumber,passs,emale,phone2);
            }
        });
        return landr;
    }
    private void landreg(final String f, final String i, final String p, final String e, final String ph){
        if(f.isEmpty()){
            fnames.setError("name is invalid");
            fnames.requestFocus();
            return;
        }else if(i.isEmpty()){
            idno.setError("idno is invalid");
            idno.requestFocus();
            return;
        }else if(p.isEmpty()){
            lpassword.setError("password is invalid");
            lpassword.requestFocus();
            return;
        }else if(e.isEmpty()){
            Email.setError("email is invalid");
            Email.requestFocus();
            return;
        }else if(ph.isEmpty()){
            phoneee.setError("phone is invalid");
            phoneee.requestFocus();
            return;
        }else {
            if (p.length() != 6) {
                lpassword.setError("password should be 6 characters");
                lpassword.requestFocus();
                return;
            } else if (!isValidEmail(e)) {
                Email.setError("email is invalid");
                Email.requestFocus();
                return;
            } else if (!isphone(ph) || (ph.length() != 10 || !ph.startsWith("07"))) {
                phoneee.setError("phone is invalid");
                phoneee.requestFocus();
                return;
            } else {
                String url = "http://10.0.2.2/Lease/LandlordReg.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Snackbar.make(sn, response, Snackbar.LENGTH_LONG).show();


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
                        params.put("fname", f);
                        params.put("idno", i);
                        params.put("pass", p);
                        params.put("email", e);
                        params.put("phone", ph);
                        return params;
                    }
                };
                mycommand.add(stringRequest);
                progressDialog.show();
                mycommand.execute();
                mycommand.remove(stringRequest);
            }

        }
    }
    public final static boolean isValidEmail(String target) {

        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public static boolean isphone(CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches();
    }
}
