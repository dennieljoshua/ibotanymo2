package com.bcklup.ibotanymo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by  gians on 11/02/2018.
 */


public class PlantList extends AppCompatActivity{
    ListView listView;
    ArrayList<Plant> list;
    PlantListAdapter adapter = null;

    public static SQLiteHelper sqLiteHelper;
    String filter ="1";
    String filter2 = " AND 1";
    String filter3 = " AND 1";
    int humlevel=0;
    int soillevel=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_items);

        setTitle("View Plants");
        final SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        try {
            sqLiteHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            sqLiteHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }
        listView = (ListView) findViewById(R.id.plantListView);
        list = new ArrayList<>();
        adapter = new PlantListAdapter(this, R.layout.customlistview,list);
        listView.setAdapter(adapter);


        Cursor cursor = sqLiteHelper.getData("SELECT * FROM plants WHERE "+filter+filter2+filter3);
        list.clear();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int type = cursor.getInt(2);
            int storeType = cursor.getInt(3);
            String guide = cursor.getString(4);
            int kind = cursor.getInt(5);
            list.add(new Plant(id, name, type, storeType, guide,kind));
        }
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            sqLiteHelper.insertPlanner((long)list.get(position).getId());
            Intent intent = new Intent(PlantList.this,HomeScreen.class);
            startActivity(intent);
            finish();
            }
        });

        final CharSequence[] humiditySelect = {"Watery", "Dry", "Less Water", "Warm & Airy"};
        final CharSequence[] soilSelect = {"Clay", "Loamy", "Sandy", "Silty", "Chalky"};

        //Prepare the list dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Set its title
        builder.setTitle("Select Humidity of Environment");
        //Set the list items and assign with the click listener
        builder.setItems(humiditySelect, new DialogInterface.OnClickListener() {
            // Click listener
            public void onClick(DialogInterface dialog, int item) {
                humlevel = item;
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        //Set its title
        builder2.setTitle("Select Soil Type");
        //Set the list items and assign with the click listener
        builder2.setItems(soilSelect, new DialogInterface.OnClickListener() {
            // Click listener
            public void onClick(DialogInterface dialog, int item) {
                soillevel = item;
                populateListView();
                dialog.dismiss();
            }
        });
        AlertDialog alert2 = builder2.create();

        //display dialog box
        alert2.show();
        alert.show();
    }

    private void populateListView(){
        filter3=" AND humidity = "+humlevel+" AND soil = "+soillevel;
        final SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        try {
            sqLiteHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            sqLiteHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }
        listView = (ListView) findViewById(R.id.plantListView);
        list = new ArrayList<>();
        adapter = new PlantListAdapter(this, R.layout.customlistview,list);
        listView.setAdapter(adapter);


        Cursor cursor = sqLiteHelper.getData("SELECT * FROM plants WHERE "+filter+filter2+filter3);
        list.clear();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int type = cursor.getInt(2);
            int storeType = cursor.getInt(3);
            String guide = cursor.getString(4);
            int kind = cursor.getInt(5);
            list.add(new Plant(id, name, type, storeType, guide, kind));
        }
        adapter.notifyDataSetChanged();
    }
    public void onRadioButtonClicked2(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_lawn:
                if (checked){
                    filter2=" AND 1";
                    filter2=filter2+" AND storeType=1";
                    populateListView();
                }
                break;
            case R.id.radio_potted:
                if (checked){
                    filter2=" AND 1";
                    filter2=filter2+" AND storeType=2";
                    populateListView();
                }
                break;
            case R.id.radio_hanging:
                if (checked){
                    filter2=" AND 1";
                    filter2=filter2+" AND storeType=3";
                    populateListView();
                }
                break;
        }
    }
    public void onRadioButtonClicked1(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_indoor:
                if (checked){
                    filter="1";
                    filter=filter+" AND type=1";
                    populateListView();
                }
                break;
            case R.id.radio_outdoor:
                if (checked){
                    filter="1";
                    filter=filter+" AND type=2";
                    populateListView();
                }
                break;
        }
    }
}
