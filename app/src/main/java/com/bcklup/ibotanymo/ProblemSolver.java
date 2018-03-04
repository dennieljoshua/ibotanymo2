package com.bcklup.ibotanymo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProblemSolver extends AppCompatActivity {


    class Problem{
        public int id;
        public String problem;
        public int solution;
        public Problem(int id, String problem, int solution){
            this.id = id;
            this.problem = problem;
            this.solution = solution;
        }
    }
    SQLiteHelper dbhelper;

    String solutionString = "";

    LinearLayout listCheckBox;
    CheckBox checkbox;

    ArrayList<Problem> list;
    HashMap<Integer, Boolean> sols;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_solver);
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
        list= new ArrayList<>();
        sols= new HashMap<>();
        listCheckBox = (LinearLayout) findViewById(R.id.listCheckbox);
        initProbs();

        initSols();
        initChecklist();
    }
    private void initSols(){
        sols.clear();
        Cursor cursor = dbhelper.getData("SELECT _id FROM solutions");
        while(cursor.moveToNext()){
            sols.put(cursor.getInt(0),false);
        }
    }
    private void initProbs(){
        Cursor cursor = dbhelper.getData("SELECT * FROM problems");
        list.clear();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String problem = cursor.getString(1);
            int solution = cursor.getInt(2);
            list.add(new Problem(id,problem,solution));
        }
    }
    private void initChecklist(){
        for (Problem object: list) {
            checkbox = new CheckBox(this);
            checkbox.setId(list.indexOf(object));
            checkbox.setText(object.problem);
            checkbox.setOnClickListener(getOnClickDoSomething(checkbox));
            listCheckBox.addView(checkbox);
        }
    }
    View.OnClickListener getOnClickDoSomething(final CheckBox button){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sols.put(list.get(button.getId()).solution,button.isChecked());
            }
        };
    }
    public void showSolutionDialog(View view){
        buildSolution();
        showProblemFullDialog(ProblemSolver.this);
    }
    private void buildSolution(){
//        for(int i=0; i < sol.size(); i++){
//            if(sol.forEach();)
//        }
        solutionString ="";
        Cursor cursor = dbhelper.getData("SELECT * FROM solutions");
        while(cursor.moveToNext()){
            if(sols.get(cursor.getInt(0))){
                solutionString += "• "+cursor.getString(1)+"\n";
            }
        }

    }
    public void showProblemFullDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.view_solution);
        dialog.setTitle("Results:");
        TextView txtSolution = (TextView) dialog.findViewById(R.id.solutionText);
        txtSolution.setText(solutionString);
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels *0.95);
        int height= (int) (activity.getResources().getDisplayMetrics().heightPixels *0.7);
        Button btnClose = (Button) dialog.findViewById(R.id.btnCloseX);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(width, height);
        dialog.show();
    }
    public void showAddProblem(View view){
        Intent intent = new Intent(this,AddProblem.class);
        startActivity(intent);
    }
}