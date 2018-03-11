package com.bcklup.ibotanymo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by gians on 11/02/2018.
 */

public class PlannerListAdapter extends BaseAdapter{
    private Context context;
    private int layout;
    private ArrayList<Plant> plantList;

    public PlannerListAdapter(Context context, int layout, ArrayList<Plant> plantList) {
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


    private class ViewHolder {
        ImageView imageView;
        TextView txtName;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);

            holder.txtName = (TextView) row.findViewById(R.id.plantName);
            holder.imageView = (ImageView) row.findViewById(R.id.plantImage);
            row.setTag(holder);
        }
        else{
            holder = (ViewHolder) row.getTag();
        }

        Plant plant = plantList.get(position);

        holder.txtName.setText(plant.getName());
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
