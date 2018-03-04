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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //INITIATE
    GridView gridView;
    ArrayList<Plant> list;
    PlannerListAdapter adapter = null;

    private static final String TAG = "MyActivity";
    public static final String PLANT_NAME="com.bcklup.ibotanymo.PLANT_NAME";
    public static final String PLANT_TYPE="com.bcklup.ibotanymo.PLANT_TYPE";
    public static final String PLANT_STORETYPE="com.bcklup.ibotanymo.PLANT_STORETYPE";
    public static final String PLANT_GUIDE="com.bcklup.ibotanymo.PLANT_GUIDE";
    public static final String PLANT_IMAGE="com.bcklup.ibotanymo.PLANT_IMAGE";
    public static final String PLANT_KIND="com.bcklup.ibotanymo.PLANT_KIND";


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
        adapter = new PlannerListAdapter(this, R.layout.planner_items,list);
        gridView.setAdapter(adapter);

        Cursor cursor = sqLiteHelper.getData("SELECT * FROM plants JOIN planner ON plants._id=planner.plant_id WHERE 1");
        list.clear();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int type = cursor.getInt(2);
            int storeType = cursor.getInt(3);
            byte[] image = cursor.getBlob(4);
            String guide = cursor.getString(5);
            int kind = cursor.getInt(6);

            list.add(new Plant(id, name, type, storeType, image, guide,kind));
        }
        adapter.notifyDataSetChanged();
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogDelete((long)list.get(position).getId());
                return true;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ViewPlant.class);
                intent.putExtra(PLANT_NAME,list.get(position).getName());
                intent.putExtra(PLANT_TYPE,list.get(position).getType());
                intent.putExtra(PLANT_STORETYPE,list.get(position).getStoreType());
                intent.putExtra(PLANT_IMAGE,list.get(position).getImage());
                intent.putExtra(PLANT_GUIDE,list.get(position).getGuide());
                intent.putExtra(PLANT_KIND,list.get(position).getKind());

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

                Log.e(TAG,id+"");
                Long idx = (long) id;
                MainActivity.sqLiteHelper.deletePlanner(idx);
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
}
