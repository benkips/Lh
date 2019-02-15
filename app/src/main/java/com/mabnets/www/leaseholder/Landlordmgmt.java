package com.mabnets.www.leaseholder;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class Landlordmgmt extends Fragment {
    private RecyclerView rv;
    private CardView cv;
    private Mycommand mycommand;
    private SharedPreferences predd;
    final String Tag=this.getClass().getName();
    private ProgressDialog pd;
    public Landlordmgmt() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mgm= inflater.inflate(R.layout.fragment_landlordmgmt, container, false);
        rv=(RecyclerView)mgm.findViewById(R.id.rvmgmt);
        predd=getActivity().getSharedPreferences("Landlord.conf", Context.MODE_PRIVATE);
        final String emm=predd.getString("landlordemail","");
        final String pasx=predd.getString("landlordPassword","");


        pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        rv.setHasFixedSize(true);
        mycommand=new Mycommand(getContext());
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        rv.setLayoutManager(manager);
        String url="http://10.0.2.2/Lease/Landlordmgmt.php";
        StringRequest strrq=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("no houses registered")){
                    pd.dismiss();
                    AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                    alert.setMessage("please register your house so as to access this section");
                    alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Fragment fragment=new LandlordMenu();
                            getFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).addToBackStack("null").commit();
                        }
                    });
                    alert.show();

                }else {
                Log.d(Tag,response);
                    pd.dismiss();
                final ArrayList<mgmtt> mgmtlist=new JsonConverter<mgmtt>().toArrayList(response,mgmtt.class);
                mgmtAdapter mgmtadapter=new mgmtAdapter(getContext(),mgmtlist);
                rv.setAdapter(mgmtadapter);

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

        return mgm;
    }

}
