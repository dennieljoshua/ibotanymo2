package com.bcklup.ibotanymo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //INITIATE
    GridView gridView;
    ArrayList<Plant> list;
    ArrayList<Planner> planners;
    PlannerListAdapter adapter = null;

    private static final String TAG = "MyActivity";
    public static final String PLANT_ID="com.bcklup.ibotanymo.PLANT_ID";
    public static final String PLANNER_ID="com.bcklup.ibotanymo.PLANNER_ID";
    public static final String PLANT_NAME="com.bcklup.ibotanymo.PLANT_NAME";
    public static final String PLANT_TYPE="com.bcklup.ibotanymo.PLANT_TYPE";
    public static final String PLANT_STORETYPE="com.bcklup.ibotanymo.PLANT_STORETYPE";
    public static final String PLANT_GUIDE="com.bcklup.ibotanymo.PLANT_GUIDE";
    public static final String PLANT_KIND="com.bcklup.ibotanymo.PLANT_KIND";
    public static final String PLANT_DATE="com.bcklup.ibotanymo.PLANT_DATE";



    class Planner{
        int id;
        int soil;
        String date;

        Planner(int id, String date) {
            this.id = id;
            this.date = date;
        }
    }


    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INITIATE DATABASE
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

        //POPULATE CRIDVIEW
        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        planners = new ArrayList<>();
        adapter = new PlannerListAdapter(this, R.layout.planner_items,list);
        gridView.setAdapter(adapter);

        Cursor cursor = sqLiteHelper.getData("SELECT b._id, b.datestarted, a.* FROM plants as A JOIN planner as B ON a._id=b.plant_id WHERE 1");
        list.clear();
        planners.clear();
        while(cursor.moveToNext()){

            int planner_id = cursor.getInt(0);
            String date = cursor.getString(1);
            int id = cursor.getInt(2);
            String name = cursor.getString(3);
            int type = cursor.getInt(4);
            int storeType = cursor.getInt(5);
            String guide = cursor.getString(6);
            int kind = cursor.getInt(7);

            planners.add(new Planner(planner_id, date));
            list.add(new Plant(id, name, type, storeType, guide,kind));

        }
        adapter.notifyDataSetChanged();
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogDelete(position);
                return true;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ViewPlant.class);
                intent.putExtra(PLANT_ID,list.get(position).getId());
                intent.putExtra(PLANT_NAME,list.get(position).getName());
                intent.putExtra(PLANT_TYPE,list.get(position).getType());
                intent.putExtra(PLANT_STORETYPE,list.get(position).getStoreType());
                intent.putExtra(PLANT_GUIDE,list.get(position).getGuide());
                intent.putExtra(PLANT_KIND,list.get(position).getKind());
                intent.putExtra(PLANT_GUIDE,list.get(position).getGuide());
                intent.putExtra(PLANT_KIND,list.get(position).getKind());
                intent.putExtra(PLANT_DATE,planners.get(position).date);
                startActivity(intent);

            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlantList.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

    }
    private void showDialogDelete(final long id){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity.this);

        dialogDelete.setTitle("Warning!");
        dialogDelete.setMessage("Are you sure you want to delete this item?");

        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity.this, id+"", Toast.LENGTH_SHORT).show();

                int pos = (int) id;
                SQLiteHelper dbx = new SQLiteHelper(getApplicationContext());
                dbx.deletePlanner((long)planners.get(pos).id);
                adapter.notifyDataSetChanged();
                recreate();
                Toast.makeText(getApplicationContext(),"Deleted Successfully",Toast.LENGTH_SHORT).show();
            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }
    public void onSecretButtonClicked(View view){
        Intent intent = new Intent(this, AddPlant.class);
        startActivity(intent);
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
