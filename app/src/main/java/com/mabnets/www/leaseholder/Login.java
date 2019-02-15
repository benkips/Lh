package com.mabnets.www.leaseholder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.EachExceptionsHandler;
import com.kosalgeek.asynctask.PostResponseAsyncTask;
import com.tapadoo.alerter.Alerter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;

import maes.tech.intentanim.CustomIntent;

import static com.mabnets.www.leaseholder.Registration.isphone;

public class Login extends AppCompatActivity {
        private EditText phone;
        private EditText pass;
        private Button loginbt;
        private Button signupbt;
        private TextView welcometexts;
        Handler Upadterlogin;
        SharedPreferences pref;
    final String Tag=this.getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone=(EditText)findViewById(R.id.phonel);
        pass=(EditText)findViewById(R.id.passl);
        loginbt=(Button)findViewById(R.id.loginbtn);
        signupbt=(Button)findViewById(R.id.signupbtn);
        welcometexts=(TextView)findViewById(R.id.welcometxt);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Upadterlogin=new Handler();
        YoYo.with(Techniques.FlipInX)
                .duration(700)
                .repeat(1)
                .playOn(welcometexts);
        pref=getSharedPreferences("login.conf",MODE_PRIVATE);
        String sphone=pref.getString("phone","");
        if(!sphone.equals("")){
            startActivity(new Intent(Login.this,index.class));
            CustomIntent.customType(Login.this,"right-to-left");
            Login.this.finish();
        }

        loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

        /*        Alerter.create(Login.this)
       .setTitle("Alert Title")
                        .setText("Alert text...")
                        .show();*/
            String ph=phone.getText().toString();
            String pas=pass.getText().toString();
            validatel(ph,pas);


            }
        });
        signupbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,Registration.class);
                startActivity(intent);
                CustomIntent.customType(Login.this,"left-to-right");
            }
        });
    }

    private  void validatel(final String p, final String ps){
        Thread Threadl=new Thread(){
            @Override
            public void run() {
                Upadterlogin.post(new Runnable() {
                    @Override
                    public void run() {
                       if(p.isEmpty()){
                           YoYo.with(Techniques.Shake)
                                   .duration(700)
                                   .repeat(3)
                                   .playOn(phone);
                           phone.setError("invalid number");
                           phone.requestFocus();
                           return;
                       }else if(ps.isEmpty()){
                           YoYo.with(Techniques.Shake)
                                   .duration(700)
                                   .repeat(3)
                                   .playOn(pass);
                           pass.setError("inavalid password");
                           pass.requestFocus();
                           return;
                       }else{
                           if(!isphone(phone.getText().toString()) || (p.length()!=10 || !p.startsWith("07"))) {
                               YoYo.with(Techniques.Shake)
                                       .duration(700)
                                       .repeat(3)
                                       .playOn(phone);
                               phone.setError("invalid number");
                               phone.requestFocus();
                               return;
                           }else if(ps.length()!=6){
                               pass.setError("password should be 6 characters");
                               pass.requestFocus();
                               return;
                           }else{
                               String url="http://10.0.2.2/Lease/login.php";
                               HashMap postdata=new HashMap();
                               postdata.put("phone",p);
                               postdata.put("password",ps);

                               PostResponseAsyncTask logintask =new PostResponseAsyncTask(Login.this, postdata, "please wait", new AsyncResponse() {
                                   @Override
                                   public void processFinish(String s) {
                                       if(s.contains("welcome")){
                                           pref=getSharedPreferences("login.conf",MODE_PRIVATE);
                                           SharedPreferences.Editor editor=pref.edit();
                                           editor.putString("phone",phone.getText().toString());
                                           editor.apply();
                                           Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
                                           startActivity(new Intent(Login.this,index.class));
                                           Login.this.finish();
                                       }else{
                                           Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
                                           Log.d(Tag, s);
                                       }

                                   }
                               });
                               logintask.execute(url);
                                logintask.setEachExceptionsHandler(new EachExceptionsHandler() {
                                    @Override
                                    public void handleIOException(IOException e) {
                                        Toast.makeText(Login.this, "internet error", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void handleMalformedURLException(MalformedURLException e) {
                                        Toast.makeText(Login.this, " url error ", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void handleProtocolException(ProtocolException e) {
                                        Toast.makeText(Login.this, " protocol error", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                                        Toast.makeText(Login.this, " encoding error ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                           }
                       }
                    }
                });
            }
        };
        Threadl.start();
    }
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(Login.this,"right-to-left");
    }
}
