package com.mabnets.www.leaseholder;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.EachExceptionsHandler;
import com.kosalgeek.asynctask.ExceptionHandler;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.security.Permission;
import java.util.HashMap;

import maes.tech.intentanim.CustomIntent;

public class Registration extends AppCompatActivity {
    private ImageButton imgbtn;
    private Dialog mydialog;
    private ImageView profpic;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryphoto;
    final int CAMERA_REQUEST =12211;
    final int GALLERY_REQUEST =12868;
    String selectedphoto="";
    private EditText username;
    private EditText passwrd;
    private EditText phone;
    private TextView login;
    private Button signup;
    Handler uploadereg;
    final String Tag=this.getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        imgbtn=(ImageButton)findViewById(R.id.camerabtn);
        profpic=(ImageView)findViewById(R.id.profilepiciv);
        mydialog=new Dialog(this);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopup();
            }
        });

       username=(EditText)findViewById(R.id.etusername);
       passwrd=(EditText)findViewById(R.id.etpass);
       phone=(EditText)findViewById(R.id.etphone);
       login=(TextView) findViewById(R.id.logintxt);
        signup=(Button)findViewById(R.id.btnsignupr);
        uploadereg=new Handler();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this,Login.class));
                CustomIntent.customType(Registration.this,"right-to-left");
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedphoto.isEmpty()){
                    AlertDialog.Builder alert=new AlertDialog.Builder(Registration.this);
                    alert.setMessage("please  upload your picture for postive identification");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(Registration.this, "thank you", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alert.show();
                }else{
                    String name=username.getText().toString();
                    String pass=passwrd.getText().toString();
                    String phoneno=phone.getText().toString();
                    Bitmap bitmap= null;
                    try {
                        bitmap = ImageLoader.init().from(selectedphoto).requestSize(110,110).getBitmap();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    String encodedstring= ImageBase64.encode(bitmap);
                    validate(name,phoneno,pass,encodedstring);
                }


            }
        });


    }
    public void validate (final String u, final String ph, final String p, final String pic){
        Thread uploadthreadreg=new Thread(){
            @Override
            public void run() {
                uploadereg.post(new Runnable() {
                    @Override
                    public void run() {
                        if(u.isEmpty()){
                            YoYo.with(Techniques.Shake)
                                    .duration(700)
                                    .repeat(2)
                                    .playOn(username);
                            username.setError("invalid username");
                            username.requestFocus();
                            return;
                        }else if(ph.isEmpty()){
                            YoYo.with(Techniques.Shake)
                                    .duration(700)
                                    .repeat(2)
                                    .playOn(phone);
                            phone.setError("invalid phonenumber");
                            phone.requestFocus();
                            return;
                        }else if(p.isEmpty()){
                            YoYo.with(Techniques.Shake)
                                    .duration(700)
                                    .repeat(2)
                                    .playOn(passwrd);
                            passwrd.setError("invalid password");
                            passwrd.requestFocus();
                            return;
                        }else{
                            if(!isphone(phone.getText().toString()) || (ph.length()!=10 || !ph.startsWith("07"))){
                                phone.setError("invalid phonenumber");
                                phone.requestFocus();
                                return;
                            }else if(p.length()!=6){
                                YoYo.with(Techniques.Shake)
                                        .duration(700)
                                        .repeat(2)
                                        .playOn(passwrd);
                                passwrd.setError("should be 6 characters");
                                passwrd.requestFocus();
                                return;
                            }else{
                            String url="http://10.0.2.2/Lease/registration.php";
                                HashMap postdata=new HashMap();
                                postdata.put("name",u);
                                postdata.put("phone",ph);
                                postdata.put("pass",p);
                                postdata.put("profile",pic);

                                PostResponseAsyncTask Regtask=new PostResponseAsyncTask(Registration.this, postdata, "processing....", new AsyncResponse() {
                                    @Override
                                    public void processFinish(String s) {
                                        if(s.equals("registation is successfull")){
                                            Toast.makeText(Registration.this, "registation was successfull", Toast.LENGTH_LONG).show();

                                    }else if(s.equals("The number is already registered")){
                                            Toast.makeText(Registration.this, s, Toast.LENGTH_SHORT).show();
                                        }else{
                                            Log.d(Tag, s);
                                    }
                                    }
                                });
                                Regtask.execute(url);
                                Regtask.setEachExceptionsHandler(new EachExceptionsHandler() {
                                    @Override
                                    public void handleIOException(IOException e) {
                                        Toast.makeText(Registration.this, "internet error", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void handleMalformedURLException(MalformedURLException e) {
                                        Toast.makeText(Registration.this, "url malformation", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void handleProtocolException(ProtocolException e) {
                                        Toast.makeText(Registration.this, "protocol error", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                                        Toast.makeText(Registration.this, "unsupport encoding", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        };
        uploadthreadreg.start();

    }
    public static boolean isphone(CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches();
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
                if(ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED ){
                    cameraPhoto=new CameraPhoto(Registration.this);

                    try {
                        startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                        cameraPhoto.addToGallery();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(Registration.this, "permission is required", Toast.LENGTH_SHORT).show();
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
                galleryphoto=new GalleryPhoto(Registration.this);
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
    public void finish() {
        super.finish();
        CustomIntent.customType(this,"right-to-left");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if(resultCode== RESULT_OK){
          if(requestCode==CAMERA_REQUEST){
              String picpath=cameraPhoto.getPhotoPath();
              try {
                  Bitmap bitmap= ImageLoader.init().from(picpath).requestSize(110,110).getBitmap();
                   profpic.setImageBitmap(bitmap);
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
                  profpic.setImageBitmap(bitmap);
                  mydialog.dismiss();
              }
          }

      }
    }
}
