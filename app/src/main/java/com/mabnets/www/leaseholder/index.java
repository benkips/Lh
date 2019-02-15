package com.mabnets.www.leaseholder;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.caching.FileCacher;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.EachExceptionsHandler;
import com.kosalgeek.asynctask.PostResponseAsyncTask;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class index extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences preff;
    private SharedPreferences.Editor editor;
    private TextView pnamee;
    private ImageView ppppic;
    Handler naviupdater;
    private String username;
    private String phoneno;
    private FileCacher<String> stringcacher;
    ImageLoaderConfiguration config;
    File cacheDir;
    DisplayImageOptions options;
    final String Tag=this.getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leaseholder");
        naviupdater=new Handler();
        preff=getSharedPreferences("login.conf",MODE_PRIVATE);
         phoneno=preff.getString("phone","");
        Toast.makeText(this, phoneno, Toast.LENGTH_SHORT).show();

        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .build();
        cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .defaultDisplayImageOptions(options) // default
                .writeDebugLogs()
                .build();

   /*     ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                .build();*/
               ImageLoader.getInstance().init(config);
         stringcacher=new FileCacher<>(getApplicationContext(),"nav.txt");












        updatepppic();
        android.support.v4.app.Fragment fragment=new Houses();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigationView=(BottomNavigationView) findViewById(R.id.btmnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navlistner);

        View NavHeader=navigationView.getHeaderView(0);
        pnamee=(TextView)NavHeader.findViewById(R.id.pname);
        ppppic=(ImageView)NavHeader.findViewById(R.id.pppic);



    }
    private  BottomNavigationView.OnNavigationItemSelectedListener navlistner=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.support.v4.app.Fragment selectedfragment=null;
            switch (item.getItemId()){
                case R.id.hoome:
                    selectedfragment =new Houses();
                    break;
                case R.id.search:
                    selectedfragment =new Search();
                    break;
                case R.id.more:
                    selectedfragment=new More();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,selectedfragment).addToBackStack(null).commit();
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            editor = preff.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(index.this, Login.class));
            index.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_landlord) {
            // Handle the camera action
            android.support.v4.app.Fragment fragment=new LandlordL();
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).commit();
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
// Always use string resources for UI text.
// This says something like "Share this photo with"
            String title = getResources().getString(R.string.share);
// Create intent to show chooser
            Intent chooser = Intent.createChooser(intent, title);

// Verify the intent will resolve to at least one activity
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            }

        } else if (id == R.id.nav_about) {
            android.support.v4.app.Fragment fragment=new About_app();
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).commit();
        } else if (id == R.id.nav_dev) {
            android.support.v4.app.Fragment fragment=new developer();
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void updatepppic(){
        Thread nav=new Thread(){
            @Override
            public void run() {
                naviupdater.post(new Runnable() {
                    @Override
                    public void run() {
                        String url="http://10.0.2.2/Lease/nav_task.php";
                        final HashMap postdata=new HashMap();
                        postdata.put("phone",phoneno);

                        PostResponseAsyncTask navtassk=new PostResponseAsyncTask(index.this, postdata,"loading", new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                if(!s.isEmpty()) {
                                   /* Toast.makeText(index.this, s, Toast.LENGTH_LONG).show();*/
                                    Log.d(Tag, s);
                                    ArrayList<post> navdetails = new JsonConverter<post>().toArrayList(s, post.class);
                                    ArrayList<String> title = new ArrayList<String>();
                                    for (post value : navdetails) {
                                        title.add(value.username);
                                        title.add(value.profile_picture);
                                        title.add(String.valueOf(value.id));
                                    }
                                    Log.d(Tag,title.get(2) );
                                    pnamee.setText(title.get(0));
                                    ImageLoader.getInstance().displayImage("http://10.0.2.2/Lease/" + title.get(1), ppppic);
                                    SharedPreferences.Editor editor=preff.edit();
                                    editor.putString("customerid",title.get(2));
                                    editor.apply();
                                    try{
                                        stringcacher.writeCache(s);
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                }else{
                                    Toast.makeText(index.this, "no internet", Toast.LENGTH_SHORT).show();
                                if(stringcacher.hasCache()){
                                    try {
                                        String t=stringcacher.readCache();
                                        ArrayList<post> navlist=new JsonConverter<post>().toArrayList(t,post.class);
                                        ArrayList<String> title=new ArrayList<String>();
                                        for (post value : navlist) {
                                            title.add(value.username);
                                            title.add(value.profile_picture);
                                            title.add(String.valueOf(value.id));
                                        }
                                        pnamee.setText(title.get(0));
                                        String encodedstring=title.get(1);
                                        SharedPreferences.Editor editor=preff.edit();
                                        editor.putString("customerid",title.get(2));
                                        editor.apply();
                                        Log.d(Tag, encodedstring);
                                        ImageLoader.getInstance().displayImage("http://10.0.2.2/Lease/" + title.get(1), ppppic);
                                      /*  Bitmap bitmap= ImageBase64.decode(encodedstring);
                                        ppppic.setImageBitmap(bitmap);*/


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }



                                }
                            }
                        });
                        navtassk.execute(url);
                        navtassk.setEachExceptionsHandler(new EachExceptionsHandler() {
                            @Override
                            public void handleIOException(IOException e) {
                                Toast.makeText(index.this, "no internet", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void handleMalformedURLException(MalformedURLException e) {
                                Toast.makeText(index.this, "url error ", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleProtocolException(ProtocolException e) {
                                Toast.makeText(index.this, "protocol error", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                                Toast.makeText(index.this, "encoding error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        };
        nav.start();

    }
}
