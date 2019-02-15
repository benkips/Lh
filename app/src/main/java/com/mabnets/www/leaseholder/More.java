package com.mabnets.www.leaseholder;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class More extends Fragment {
    private ListView lvvmore;


    public More() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View m= inflater.inflate(R.layout.fragment_more, container, false);
        ((AppCompatActivity)getContext()).getSupportActionBar().setTitle("LeaseHolder");
        lvvmore=(ListView)m.findViewById(R.id.lvmore);
        String [] more=getResources().getStringArray(R.array.Moremenu);

        Simpleadapter2 adapter=new Simpleadapter2(getContext(),more);
        lvvmore.setAdapter(adapter);
        lvvmore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:{
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://www.stackoverflow.com"));

                        String titlez= String.valueOf(getResources().getText(R.string.cmpactiom));
                        Intent chooser=Intent.createChooser(intent,titlez);
                        startActivity(chooser);

                    }
                    case 1:{
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://www.stackoverflow.com"));

                        String titlez= String.valueOf(getResources().getText(R.string.cmpactiom));
                        Intent chooser=Intent.createChooser(intent,titlez);
                        startActivity(chooser);
                    }
                    case 2:{
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://www.stackoverflow.com"));

                        String titlez= String.valueOf(getResources().getText(R.string.cmpactiom));
                        Intent chooser=Intent.createChooser(intent,titlez);
                        startActivity(chooser);
                    }
                    case 3 :{
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://www.stackoverflow.com"));

                        String titlez= String.valueOf(getResources().getText(R.string.cmpactiom));
                        Intent chooser=Intent.createChooser(intent,titlez);
                        startActivity(chooser);
                    }
                    case 4 :{
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://www.stackoverflow.com"));

                        String titlez= String.valueOf(getResources().getText(R.string.cmpactiom));
                        Intent chooser=Intent.createChooser(intent,titlez);
                        startActivity(chooser);
                    }
                }

            }
        });

        return m;
    }
    public class Simpleadapter2 extends BaseAdapter{
        private Context mcontext;
        private LayoutInflater LayoutInflator;
        private TextView moretitles;
        private  String [] moremenuzarray;
        private ImageView  ivmore;
        public Simpleadapter2( Context context,String [] mrtitles) {
           mcontext=context;
           moremenuzarray=mrtitles;
           LayoutInflator=LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            return moremenuzarray.length;

        }

        @Override
        public Object getItem(int i) {
            return moremenuzarray[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null){
                view=LayoutInflator.inflate(R.layout.moremenuu,null);

            }
            moretitles=(TextView)view.findViewById(R.id.tvmore);
            ivmore=(ImageView)view.findViewById(R.id.ivmore);

            moretitles.setText(moremenuzarray[i]);

            if(moremenuzarray[i].equalsIgnoreCase("Faq")){
                ivmore.setImageResource(R.drawable.ic_question_answer_black_24dp);
            }else if(moremenuzarray[i].equalsIgnoreCase("Help")){
                ivmore.setImageResource(R.drawable.ic_help_outline_black_24dp);
            }else if(moremenuzarray[i].equalsIgnoreCase("Rate")){
                ivmore.setImageResource(R.drawable.ic_stars_black_24dp);
            }else if(moremenuzarray[i].equalsIgnoreCase("Feedback")){
                ivmore.setImageResource(R.drawable.ic_feedback_black_24dp);
            }else{
                ivmore.setImageResource(R.drawable.ic_get_app_black_24dp);
            }






            return view;
        }
    }

}
