package com.bcklup.ibotanymo;

import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class AddProblem extends AppCompatActivity {

    EditText txtProblem, txtSolution;
    Button saveBtn;
    SQLiteHelper dbHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);
        setTitle("Add Problem & Solution");


        txtProblem = (EditText) findViewById(R.id.txtProblem);
        txtSolution= (EditText) findViewById(R.id.txtSolution);
        saveBtn = (Button) findViewById(R.id.btnAddProblem);

         dbHelp= new SQLiteHelper(this);
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

    }

    public void saveProblem(View view){
        try {
            dbHelp.insertProblem(
                    txtProblem.getText().toString().trim(),
                    txtSolution.getText().toString().trim()
            );
            Toast.makeText(getApplicationContext(), "Added Successfully!",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
