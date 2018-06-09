
package com.bcklup.ibotanymo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProblemSolver extends AppCompatActivity {


    class Problem{
        public int id;
        public String problem;
        public ArrayList<Integer> solution;
        public Problem(int id, String problem, ArrayList<Integer> solution){
            this.id = id;
            this.problem = problem;
            this.solution = solution;
        }
    }
    SQLiteHelper dbhelper;

    String solutionString = "";
    String filterText="1";
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_problem);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty()){
                    filterText = "1 AND problem LIKE \"%"+newText+"%\"";
                    resetCheckbox();
                    return false;
                }
                else{
                    filterText="1";
                    resetCheckbox();
                    return false;
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    private void initSols(){
        sols.clear();
        Cursor cursor = dbhelper.getData("SELECT _id FROM solutions");
        while(cursor.moveToNext()){
            sols.put(cursor.getInt(0),false);
        }
    }

    void resetCheckbox(){
        initProbs();
        initSols();
        listCheckBox.removeAllViews();
        initChecklist();
    }
    private void initProbs(){
        Cursor cursor = dbhelper.getData("SELECT * FROM problems WHERE "+filterText);
        list.clear();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String problem = cursor.getString(1);
            Cursor cursor2 = dbhelper.getData("SELECT solution_id FROM problems_solutions WHERE problem_id ="+id);
            ArrayList<Integer> sols = new ArrayList<>();
            while(cursor2.moveToNext()){
                sols.add(cursor2.getInt(0));
            }
            list.add(new Problem(id,problem,sols));
        }
    }
    private void initChecklist(){
        for (Problem object: list) {
            checkbox = new CheckBox(this);
            checkbox.setId(list.indexOf(object));
            checkbox.setText(object.problem);
            checkbox.setOnClickListener(getOnClickDoSomething(checkbox));
            listCheckBox.addView(checkbox);
            
            Button btn = new Button(this);
            btn.setText("DELETE");
            btn.setMaxWidth(250);
            btn.setOnClickListener((View v) -> {
                dbhelper.deleteProblemAndSolution((long) object.id);
                resetCheckbox();
                Toast.makeText(this, "Problem Deleted!", Toast.LENGTH_SHORT).show();
            });
            listCheckBox.addView(btn);

            Button editBtn = new Button(this);
            editBtn.setText("EDIT");
            editBtn.setMaxWidth(250);
            listCheckBox.addView(editBtn);
        }
    }

    View.OnClickListener getOnClickDoSomething(final CheckBox button){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Integer xad: list.get(button.getId()).solution){
                    sols.put(xad,button.isChecked());
                }
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