package com.mabnets.www.leaseholder;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.kosalgeek.android.caching.FileCacher;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Update_profile extends Fragment {
    private ImageView  changeiv;
    private EditText change_name;
    private Button changebtn;
    private ImageButton uploadiv;
    private CameraPhoto cameraPhoto;
    private GalleryPhoto galleryphoto;
    private final int CAMERA_REQUEST =12211;
    private final int GALLERY_REQUEST =12868;
    private Dialog mydialog;
    private ProgressDialog progressDialog;
    private Mycommand mycommand;
    private SharedPreferences pref;
    String selectedphoto="";
    private String emm;
    private String pasx;
    final String Tag = this.getClass().getName();
    public Update_profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View upd=inflater.inflate(R.layout.fragment_update_profile, container, false);

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        pref=getActivity().getSharedPreferences("Landlord.conf", Context.MODE_PRIVATE);
         emm=pref.getString("landlordemail","");
         pasx=pref.getString("landlordPassword","");

        changeiv=(ImageView)upd.findViewById(R.id.chngpic);
        changebtn=(Button)upd.findViewById(R.id.chngbtn);
        change_name=(EditText)upd.findViewById(R.id.namechng);
        uploadiv=(ImageButton)upd.findViewById(R.id.change_uploadbtn);
        mydialog=new Dialog(getContext());
        mycommand=new Mycommand(getContext());
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("updating...");
        showpic();
        uploadiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopup();
            }
        });


        changebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nm=change_name.getText().toString().trim();
                if(selectedphoto.equals("") || nm.equals("")){
                    Toast.makeText(getContext(), "please select a picture", Toast.LENGTH_SHORT).show();
                }else{
                    updateproff(nm,selectedphoto);
            }
            }
        });
       return upd;
    }
    public void showpopup(){
        ImageView campop;
        final ImageView gallerypop;
        TextView closebtn;
        mydialog.setContentView(R.layout.camerapopup);
        campop=(ImageView) mydialog.findViewById(R.id.camerabtnp);
        campop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED ){
                    cameraPhoto=new CameraPhoto(getContext());

                    try {
                        startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                        cameraPhoto.addToGallery();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getContext(), "permission is required", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_REQUEST);
                    }

                }

            }
        });
        gallerypop=(ImageView) mydialog.findViewById(R.id.gallerybtnp);
        gallerypop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryphoto=new GalleryPhoto(getContext());
                startActivityForResult(galleryphoto.openGalleryIntent(),GALLERY_REQUEST);
            }
        });
        closebtn=(TextView)mydialog.findViewById(R.id.closebtn);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydialog.dismiss();
            }
        });
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== RESULT_OK){
            if(requestCode==CAMERA_REQUEST){
                String picpath=cameraPhoto.getPhotoPath();
                try {
                    Bitmap bitmap= ImageLoader.init().from(picpath).requestSize(110,110).getBitmap();
                    changeiv.setImageBitmap(bitmap);
                    selectedphoto=picpath;
                    mydialog.dismiss();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else{
                if(requestCode==GALLERY_REQUEST){
                    Uri uri=data.getData();
                    galleryphoto.setPhotoUri(uri);
                    String picpath=galleryphoto.getPath();
                    selectedphoto=picpath;
                    Bitmap bitmap=null;
                    try {
                        bitmap=ImageLoader.init().from(picpath).requestSize(110,110).getBitmap();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    changeiv.setImageBitmap(bitmap);
                    mydialog.dismiss();
                }
            }

        }
    }
    private void showpic(){
        String url = "http://10.0.2.2/Lease/Landlordproff.php";
        StringRequest rqst = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                /*Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();*/
                if(response.contains("no registered persons under that name")){
                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                }else{
                    ArrayList<landdetails> cvdetilz=new JsonConverter<landdetails>().toArrayList(response,landdetails.class);
                    ArrayList<String> tittle=new ArrayList<String>();
                    for(landdetails value:cvdetilz){
                        tittle.add(value.fullnames);
                        tittle.add(value.img);
                    }
                    if(tittle.get(1).equals("")){
                        changeiv.setImageResource(R.drawable.person);
                        change_name.setText(tittle.get(0));
                    }else{
                        change_name.setText(tittle.get(0));
                        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage("http://10.0.2.2/Lease/"+tittle.get(1), changeiv);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                    alert.setMessage("please connect to the internet to update your profile picture");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Fragment fragment=new LandlordMenu();
                            getFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).addToBackStack("null").commit();
                        }
                    });
                    alert.show();
                    Toast.makeText(getContext(), "error time out ", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                    alert.setMessage("please connect to the internet to update your profile picture");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Fragment fragment=new LandlordMenu();
                            getFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).addToBackStack("null").commit();
                        }
                    });
                    alert.show();
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
                params.put("email", emm);
                params.put("pass", pasx);
                return params;
            }
        };
        mycommand.add(rqst);
        progressDialog.show();
        mycommand.execute();
        mycommand.remove(rqst);
    }
    private void updateproff(final String name, final String photo){


            String url = "http://10.0.2.2/Lease/proffupdateland.php";
            StringRequest rqst = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError) {
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
                    params.put("email", emm);
                    params.put("password", pasx);
                    params.put("nm", name);
                    try {
                        Bitmap bitmap=ImageLoader.init().from(photo).requestSize(512,512).getBitmap();
                        String pic=ImageBase64.encode(bitmap);
                        params.put("pic", pic);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }



                    return params;
                }
            };
            mycommand.add(rqst);
            progressDialog.show();
            mycommand.execute();
            mycommand.remove(rqst);
    }
}
