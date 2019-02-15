package com.mabnets.www.leaseholder;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Landlordupload extends Fragment {
    private EditText premises;
    private EditText countyy;
    private EditText town;
    private EditText Description;
    private Button next;
    private ImageView hompic;
    Handler updatorr;
    private ImageButton glrybtn;
    GalleryPhoto galleryPhotoo;
    final int GALLERY_REQUESST=14677;
    String selectedpic="";
    private String emaill;
    private String passwrd;
    private  SharedPreferences prefff;
    private Bitmap bitmap =null;
    private Mycommand mycomando;
    private ProgressDialog pd;
    public Landlordupload() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View Lupload= inflater.inflate(R.layout.fragment_landlordupload, container, false);
        premises=(EditText)Lupload.findViewById(R.id.premise);
        countyy=(EditText)Lupload.findViewById(R.id.county);
        town=(EditText)Lupload.findViewById(R.id.town);
        Description=(EditText)Lupload.findViewById(R.id.descriptn);
        next=(Button)Lupload.findViewById(R.id.neeext);
        glrybtn=(ImageButton)Lupload.findViewById(R.id.gallrybtnn) ;
        hompic=(ImageView)Lupload.findViewById(R.id.homepic);
        updatorr=new Handler();
        mycomando=new Mycommand(getContext());
        pd=new ProgressDialog(getContext());
        pd.setMessage("processing");
        prefff=getActivity().getSharedPreferences("Landlord.conf", Context.MODE_PRIVATE);
         emaill=prefff.getString("landlordemail","");
        passwrd=prefff.getString("landlordPassword","");
        glrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryPhotoo=new GalleryPhoto(getContext());
                startActivityForResult(galleryPhotoo.openGalleryIntent(),GALLERY_REQUESST);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread up=new Thread(){
                    @Override
                    public void run() {
                        updatorr.post(new Runnable() {
                            @Override
                            public void run() {
                            if(selectedpic.isEmpty()){
                                AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                                alert.setMessage("please upload your main premise picture for tenants to view");
                                alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getContext(), "thank you", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                alert.show();
                            }else{
                                try {
                                    bitmap=ImageLoader.init().from(selectedpic).requestSize(512,512).getBitmap();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                String path= ImageBase64.encode(bitmap);
                                String premise=premises.getText().toString().trim();;
                                String countty=countyy.getText().toString().trim();
                                String townntn=town.getText().toString().trim();
                                String desc=Description.getText().toString().trim();
                                String e=emaill;
                                String p=passwrd;

                               inserthouse(premise,countty,townntn,e,p,desc,path);
                            }

                            }
                        });
                    }
                };
                up.start();
            }
        });
        return Lupload;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==GALLERY_REQUESST){
                Uri urii=data.getData();
                galleryPhotoo.setPhotoUri(urii);
                String picpathh=galleryPhotoo.getPath();
                selectedpic=picpathh;
                Bitmap bitmap=null;
                try {
                    bitmap= ImageLoader.init().from(picpathh).requestSize(512,512).getBitmap();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
               hompic.setImageBitmap(bitmap);
            }
        }
    }
    private void inserthouse(final String pre, final String cunty, final String townn, final String em, final String ps, final String description, final String picselect){
        if(pre.isEmpty()){
            premises.setError("name is required");
            premises.requestFocus();
            return;
        }else if(cunty.isEmpty()){
            countyy.setError("county is required");
            premises.requestFocus();
            return;

        }else if(townn.isEmpty()){
            town.setError("town is required");
            town.requestFocus();
            return;

        }else if(description.isEmpty()){
            Description.setError("description is required");
            Description.requestFocus();
            return;

        }else{
            String url="http://10.0.2.2/Lease/houseprofile.php";
            StringRequest strq=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if(response.contains("success")){
                        pd.dismiss();
                        AlertDialog.Builder alertt=new AlertDialog.Builder(getContext());
                        alertt.setMessage(response);
                        alertt.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Fragment fragment=new Landlordpictures();
                                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                getFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).add(new LandlordMenu(),"lmenu").addToBackStack("lmenu").commit();
                            }
                        });
                        alertt.show();
                    }else{
                        pd.dismiss();
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> params=new HashMap<>();
                    params.put("pname",pre);
                    params.put("county",cunty);
                    params.put("town",townn);
                    params.put("description",description);
                    params.put("email",em);
                    params.put("pass",ps);
                    params.put("pic",picselect);
                    return  params;

                }
            };

            mycomando.add(strq);
            pd.show();
            mycomando.execute();
            mycomando.remove(strq);
        }
    }
}
