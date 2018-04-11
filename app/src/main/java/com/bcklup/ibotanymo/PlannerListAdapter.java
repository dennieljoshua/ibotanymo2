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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by gians on 11/02/2018.
 */

public class PlannerListAdapter extends BaseAdapter{


    private Context context;
    private int layout;
    private ArrayList<Plant> plantList;
    private ArrayList<Planner> plannerList;

    public PlannerListAdapter(Context context, int layout, ArrayList<Plant> plantList, ArrayList<Planner> plannerList) {
        this.context = context;
        this.layout = layout;
        this.plantList = plantList;
        this.plannerList = plannerList;
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
        TextView txtDays;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);

            holder.txtName = (TextView) row.findViewById(R.id.plantName);
            holder.txtDays = (TextView) row.findViewById(R.id.plannerAge);
            holder.imageView = (ImageView) row.findViewById(R.id.plantImage);

            row.setTag(holder);
        }
        else{
            holder = (ViewHolder) row.getTag();
        }

        Plant plant = plantList.get(position);
        Planner plan = plannerList.get(position);

        Date plantedDate = parseDateString(plan.getDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(plantedDate);
        long diff = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();
        long plantAge = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        holder.txtDays.setText(plantAge+"");
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
    Date parseDateString(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date response = new Date();
        try {
            response =  dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }
}
