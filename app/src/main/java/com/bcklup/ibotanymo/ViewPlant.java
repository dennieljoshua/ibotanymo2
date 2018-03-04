package com.bcklup.ibotanymo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.HashMap;
import java.util.Map;

public class ViewPlant extends AppCompatActivity {
    String plantnamey = "";
    Bitmap mybmp;



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
        byte[] plantImg = intent.getExtras().getByteArray(MainActivity.PLANT_IMAGE);
        Bitmap bmp = BitmapFactory.decodeByteArray(plantImg,0,plantImg.length);
        mybmp = bmp;
        plantImage.setImageBitmap(bmp);
        switch(intent.getExtras().getInt(MainActivity.PLANT_KIND)){
            case 1: growthMap.setImageResource(R.drawable.shrubgrowthmap);
                break;
            case 2: growthMap.setImageResource(R.drawable.vinegrowthmap);
                break;
            case 3: growthMap.setImageResource(R.drawable.vinegrowthmap);
                break;
            case 4: growthMap.setImageResource(R.drawable.treegrowthmap);
                break;
        }
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
        img.setImageBitmap(mybmp);

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
}
