package com.bcklup.ibotanymo;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class AddProblem extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText txtProblem, txtSolution;
    Button saveBtn;
    SQLiteHelper dbHelp;
    ArrayAdapter<String> spnAdapter;
    Spinner spnSolution;
    int selectedSol;
    ArrayList<Integer> solids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);
        setTitle("Add Problem & Solution");
        selectedSol = 1337;
        solids = new ArrayList<>();
        txtProblem = (EditText) findViewById(R.id.txtProblem);
        txtSolution= (EditText) findViewById(R.id.txtAddSolution);
        saveBtn = (Button) findViewById(R.id.btnAddProblem);
        spnSolution = (Spinner) findViewById(R.id.txtSolution);
        dbHelp = new SQLiteHelper(this);
        try {
            dbHelp.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            dbHelp.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }
//        spnAdapter = ArrayAdapter.
        Cursor cursor = dbHelp.getData("SELECT * FROM solutions");
        ArrayList<String> contents = new ArrayList<>();
        solids.clear();
        while(cursor.moveToNext()){
            contents.add(cursor.getString(1));
            solids.add(cursor.getInt(0));
        }
        spnAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,contents);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSolution.setAdapter(spnAdapter);
        dbHelp= new SQLiteHelper(this);
        spnSolution.setOnItemSelectedListener(this);

        txtProblem.requestFocus();
    }
    public void addNewSolution(View view){
        txtSolution.setVisibility(View.VISIBLE);
    }
    public void saveProblem(View view){
        if(!txtProblem.getText().toString().isEmpty()){
            if(txtSolution.getText().toString().isEmpty()) {
                if (selectedSol != 1337) {
                    try {
                        dbHelp.insertProblemExisting(
                                txtProblem.getText().toString().trim(),
                                selectedSol
                        );
                        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "Added Successfully!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Choose solution first!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                try {
                    dbHelp.insertProblem(
                            txtProblem.getText().toString().trim(),
                            txtSolution.getText().toString().trim()
                    );
                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Added Successfully!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(this, "Add Problem Text first!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        selectedSol = solids.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        selectedSol = 1337;
        // TODO Auto-generated method stub

    }

}
