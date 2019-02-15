package com.mabnets.www.leaseholder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.searchHolder> implements Filterable {
    private Context context;
    private List<srchdata> searchlist;
    private List<srchdata> searchlistcpy;

    public searchAdapter (Context context,List<srchdata> searchlist){
        this.context=context;
        this.searchlist=searchlist;
        this.searchlistcpy=new ArrayList<>(searchlist);

    }
    @Override
    public searchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflator=LayoutInflater.from(parent.getContext());
        View view=inflator.inflate(R.layout.searchdata,parent,false);
        searchHolder srch=new searchHolder(view);
        return srch;
    }

    @Override
    public void onBindViewHolder(searchHolder holder, int position) {
    final srchdata srchdata=searchlist.get(position);
    holder.srchplace.setText(srchdata.getCounty());
    holder.srchname.setText(srchdata.getPremise());
    ImageLoader.getInstance().displayImage("http://10.0.2.2/Lease/"+srchdata.getImage(),holder.srchiv);
    holder.cvsrch.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(context, srchdata.getPremise(), Toast.LENGTH_SHORT).show();
        }
    });
    }

    @Override
    public int getItemCount() {
        if(searchlist!=null){
            return searchlist.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return  searchfilter;
    }
    private Filter searchfilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           List<srchdata> filteredlist=new ArrayList<>();
           if(constraint==null || constraint.length()==0){
               filteredlist.addAll(searchlistcpy);
           }else{
               String Filteredstring=constraint.toString().toLowerCase().trim();
               for(srchdata item: searchlistcpy){
                   if(item.getCounty().toLowerCase().contains(constraint)){
                       filteredlist.add(item);
                   }else if(item.getPremise().toLowerCase().contains(constraint)){
                       filteredlist.add(item);
                   }else {
                       Toast.makeText(context, "results not found", Toast.LENGTH_SHORT).show();
                   }
               }
           }
           FilterResults results=new FilterResults();
           results.values=filteredlist;
           return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchlist.clear();
            searchlist.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
    public static class searchHolder extends RecyclerView.ViewHolder{
        public TextView srchname;
        public TextView srchplace;
        public ImageView srchiv;
        public CardView cvsrch;
    public searchHolder(View itemView) {
        super(itemView);
        srchname=(TextView)itemView.findViewById(R.id.namesrch);
        srchplace=(TextView)itemView.findViewById(R.id.placesrch);
        srchiv=(ImageView) itemView.findViewById(R.id.srchiv);
        cvsrch=(CardView)itemView.findViewById(R.id.cvsrch);

    }
}
}
