package com.mabnets.www.leaseholder;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Landlordpictures extends Fragment {
    private ImageButton gallerybtnn;
    private ImageButton uploadbtnn;
    private GalleryPhoto gp;
    final  int GALLERY_CODE=19142;
    private LinearLayout linearLayout;
    ArrayList<String> imagelist=new ArrayList<>();
    private String e;
    private String p;
     SharedPreferences prefff;
     private StringRequest strr;
     ProgressDialog progressDialog;
    public Landlordpictures() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View landuplodpic= inflater.inflate(R.layout.fragment_landlordpictures, container, false);
        gallerybtnn=(ImageButton) landuplodpic.findViewById(R.id.gallerybtn);
        uploadbtnn=(ImageButton) landuplodpic.findViewById(R.id.uploadbtn);
        linearLayout=(LinearLayout)landuplodpic.findViewById(R.id.linlay);
        final Mycommand mycommand=new Mycommand(getContext());
        prefff=getActivity().getSharedPreferences("Landlord.conf", Context.MODE_PRIVATE);
        e=prefff.getString("landlordemail","");
        p=prefff.getString("landlordPassword","");
        progressDialog=new ProgressDialog(getContext());
         gallerybtnn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 gp=new GalleryPhoto(getContext());
                startActivityForResult(gp.openGalleryIntent(),GALLERY_CODE);


             }
         });
         uploadbtnn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Toast.makeText(getContext(), "upload", Toast.LENGTH_SHORT).show();
                 if(imagelist.size()==4) {
                     for (final String img : imagelist) {
                         String url = "http://10.0.2.2/Lease/morehouses.php";
                         try {
                             Bitmap btmap = ImageLoader.init().from(img).requestSize(512, 512).getBitmap();
                             final String encoded = ImageBase64.encode(btmap);
                             strr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                 @Override
                                 public void onResponse(String response) {
                                     progressDialog.dismiss();
                                     Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                                     linearLayout.removeAllViews();


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
                                     HashMap<String, String> param = new HashMap<>();
                                     param.put("email", e);
                                     param.put("pass", p);
                                     param.put("pic", encoded);
                                     return param;
                                 }
                             };

                             mycommand.add(strr);
                             progressDialog.show();
                             /*imagelist.removeAll(imagelist);
                              */
                         } catch (FileNotFoundException e) {
                             e.printStackTrace();
                         }

                     }
                     mycommand.execute();
                     mycommand.remove(strr);
                     imagelist.clear();
                 }else{
                     Toast.makeText(getContext(), "please select strictly 4 pictures then click upload", Toast.LENGTH_LONG).show();
                 }




             }
         });


        return landuplodpic;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==GALLERY_CODE){
                Uri uri=data.getData();
                gp.setPhotoUri(uri);
                String pic=gp.getPath();
                imagelist.add(pic);
                Bitmap bitmap=null;
                try {
                    bitmap= ImageLoader.init().from(pic).requestSize(512,512).getBitmap();
                    ImageView imageView=new ImageView(getContext());
                    LinearLayout.LayoutParams LinearLayout=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                    imageView.setLayoutParams(LinearLayout);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setPadding(0,0,0,10);
                    imageView.setAdjustViewBounds(true);
                    imageView.setImageBitmap(bitmap);
                    linearLayout.addView(imageView);



                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }

        }
    }
}
