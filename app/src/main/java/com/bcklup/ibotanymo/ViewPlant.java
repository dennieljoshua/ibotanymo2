package com.bcklup.ibotanymo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ViewPlant extends AppCompatActivity {
    String plantnamey = "";
    Drawable d;
    long plantDaysAge;

    SQLiteHelper dbhelper;
    String solutionString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewplant);

        Intent intent = getIntent();
        TextView plantName = findViewById(R.id.plantName);
        TextView plantType = findViewById(R.id.plantType);
        TextView plantStoreType = findViewById(R.id.plantStoreType);
        TextView plantGuide = findViewById(R.id.plantGuide);
        ImageView plantImage= findViewById(R.id.plantImage);
        ImageView growthMap = findViewById(R.id.growthMap);
        TextView plantKind = findViewById(R.id.plantKind);
        TextView plantAge = findViewById(R.id.plantAge);
        TextView plantDate = findViewById(R.id.plantDate);
        TextView plantTips = findViewById(R.id.growthTips);



        dbhelper = new SQLiteHelper(this);

        try {
            dbhelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            dbhelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }

        plantName.setText(intent.getExtras().getString(MainActivity.PLANT_NAME));
        plantnamey = intent.getExtras().getString(MainActivity.PLANT_NAME);
        switch(intent.getExtras().getInt(MainActivity.PLANT_TYPE)){
            case 1: plantType.setText("Indoor");
                break;
            case 2: plantType.setText("Outdoor");
                break;
        }
        switch(intent.getExtras().getInt(MainActivity.PLANT_STORETYPE)){
            case 1: plantStoreType.setText("Lawn");
                break;
            case 2: plantStoreType.setText("Potted");
                break;
            case 3: plantStoreType.setText("Hanging");
                break;
        }
        plantGuide.setText(intent.getExtras().getString(MainActivity.PLANT_GUIDE));
        InputStream ims = null;
        try {
            ims = getAssets().open("imgs/"+intent.getExtras().getInt(MainActivity.PLANT_ID)+".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        d = Drawable.createFromStream(ims, null);
        plantImage.setImageDrawable(d);


        switch(intent.getExtras().getInt(MainActivity.PLANT_KIND)){
            case 1: plantKind.setText("Shrub");
                break;
            case 2: plantKind.setText("Vine");
                break;
            case 3: plantKind.setText("Bulb");
                break;
            case 4: plantKind.setText("Tree");
                break;
        }
        Date plantedDate = parseDateString(intent.getExtras().getString(MainActivity.PLANT_DATE));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(plantedDate);
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MMM-yyyy");
        String plantedDateString = formatter.format(calendar.getTime());

        long diff = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();
        plantDaysAge = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        plantDate.setText(plantedDateString);
        if(plantDaysAge == 1)         plantAge.setText(plantDaysAge+" day");
        else plantAge.setText(plantDaysAge+" days");
        solutionString="";
        Cursor cursor = dbhelper.getData("SELECT tip FROM tips WHERE kind = "+intent.getExtras().getInt(MainActivity.PLANT_KIND)+" AND min < "+plantDaysAge+" AND max > "+plantDaysAge);
        while(cursor.moveToNext()){
            solutionString += "• "+cursor.getString(0)+"\n";
        }
        plantTips.setText(solutionString);

        int stage=0;
        switch(intent.getExtras().getInt(MainActivity.PLANT_KIND)){
            case 1:
                if(plantDaysAge > 0 && plantDaysAge < 30 ) stage = 1;
                else if(plantDaysAge > 29 && plantDaysAge < 100 ) stage = 2;
                else if(plantDaysAge > 99) stage = 3;
                break;
            case 2:
                if(plantDaysAge > 0 && plantDaysAge < 30 ) stage = 1;
                else if(plantDaysAge > 29 && plantDaysAge < 70 ) stage = 2;
                else if(plantDaysAge > 69 && plantDaysAge < 200) stage = 3;
                else if(plantDaysAge > 199) stage = 4;
                break;
            case 3:
                if(plantDaysAge > 0 && plantDaysAge < 30 ) stage = 1;
                else if(plantDaysAge > 29 && plantDaysAge < 70 ) stage = 2;
                else if(plantDaysAge > 69 && plantDaysAge < 200) stage = 3;
                else if(plantDaysAge > 199) stage = 4;
                break;
            case 4:
                if(plantDaysAge > 0 && plantDaysAge < 50 ) stage = 1;
                else if(plantDaysAge > 49 && plantDaysAge < 100 ) stage = 2;
                else if(plantDaysAge > 99 && plantDaysAge < 300) stage = 3;
                else if(plantDaysAge > 299 && plantDaysAge < 400) stage = 4;
                else if(plantDaysAge > 399) stage = 5;
                break;
        }

        InputStream instream = null;
        try {
            instream = getAssets().open("growthprogress/"+intent.getExtras().getInt(MainActivity.PLANT_KIND)+""+stage+".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable progress = Drawable.createFromStream(instream, null);
        growthMap.setImageDrawable(progress);
    }
    public void showImageFull(View view){

        showImageFullDialog(ViewPlant.this);
    }
    public void showImageFullDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.plant_fullview);
        dialog.setTitle(plantnamey);

        ImageView img = (ImageView) dialog.findViewById(R.id.fullImageView);
//        final ZoomControls zc = (ZoomControls) dialog.findViewById(R.id.zoom_controls);
        img.setImageDrawable(d);

//        zc.setOnZoomInClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                float currentZoom=1;
//                float oldZoom = currentZoom;
//                currentZoom = currentZoom * (float)1.25;
//                zc.setIsZoomOutEnabled(true);
//                if (3.0 < currentZoom) {
//                    zc.setIsZoomInEnabled(false);
//                }
//                ScaleAnimation scaleAnim = new ScaleAnimation(oldZoom, currentZoom, oldZoom, currentZoom, 0, 0);
//                scaleAnim.setFillAfter(true);
//                img.startAnimation(scaleAnim);
//            }
//        });
//        zc.setOnZoomOutClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                float currentZoom=1;
//                float oldZoom = currentZoom;
//                currentZoom = currentZoom / (float)1.25;
//                zc.setIsZoomInEnabled(true);
//                if (0.33 > currentZoom) {
//                    zc.setIsZoomOutEnabled(false);
//                }
//                ScaleAnimation scaleAnim = new ScaleAnimation(oldZoom, currentZoom, oldZoom, currentZoom, 0, 0);
//                scaleAnim.setFillAfter(true);
//                img.startAnimation(scaleAnim);
//            }
//        });
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels *0.95);
        int height= (int) (activity.getResources().getDisplayMetrics().heightPixels *0.7);
        Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(width, height);
        dialog.show();

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
