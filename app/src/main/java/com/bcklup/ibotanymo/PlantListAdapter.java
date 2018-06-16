package com.bcklup.ibotanymo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by gians on 11/02/2018.
 */

public class PlantListAdapter extends BaseAdapter implements Filterable{
    private Context context;
    private int layout;
    private ArrayList<Plant> plantList;
    private ArrayList<Plant> filtered = null;
    private ItemFilter mFilter = new ItemFilter();

    public PlantListAdapter(Context context, int layout, ArrayList<Plant> plantList) {
        this.context = context;
        this.layout = layout;
        this.plantList = plantList;
    }

    @Override
    public int getCount() {
        return plantList.size();
    }

    @Override
    public Object getItem(int position) {
        return plantList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<Plant> list = plantList;

            int count = list.size();
            final ArrayList<Plant> nlist = new ArrayList<Plant>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
//                filterableString = list.get(i);
                Plant item = list.get(i);
                filterableString = list.get(i).getName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(item);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered = (ArrayList<Plant>) results.values;
            notifyDataSetChanged();
        }

    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtName;
        TextView txtType;
        TextView txtStoreType;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);

            holder.txtName = (TextView) row.findViewById(R.id.plantName);
            holder.txtType = (TextView) row.findViewById(R.id.plantType);
            holder.txtStoreType = (TextView) row.findViewById(R.id.plantStoreType);
            holder.imageView = (ImageView) row.findViewById(R.id.plantImage);
            row.setTag(holder);
        }
        else{
            holder = (ViewHolder) row.getTag();
        }

        Plant plant = plantList.get(position);

        holder.txtName.setText(plant.getName());

        switch(plant.getType()){
            case 1: holder.txtType.setText("Indoor");
                break;
            case 2: holder.txtType.setText("Outdoor");;
                break;
        }
        switch(plant.getStoreType()){
            case 1: holder.txtStoreType.setText("Lawn");
                break;
            case 2: holder.txtStoreType.setText("Potted");
                break;
            case 3: holder.txtStoreType.setText("Hanging");
                break;
        }
        InputStream ims = null;
        try {
            ims = context.getAssets().open("imgs/"+plant.getId()+".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable d = Drawable.createFromStream(ims, null);
        holder.imageView.setImageDrawable(d);

        return row;
    }
}
