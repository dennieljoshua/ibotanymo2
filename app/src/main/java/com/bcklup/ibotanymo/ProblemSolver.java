
package com.bcklup.ibotanymo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcklup.ibotanymo.problems.EditProblemAndSolution;
import com.bcklup.ibotanymo.problems.Problem;
import com.bcklup.ibotanymo.problems.ProblemListAdapter;
import com.bcklup.ibotanymo.problems.ProblemsAdapter;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ProblemSolver extends AppCompatActivity {


    SQLiteHelper dbhelper;

    String solutionString = "";
    String filterText="1";
//    LinearLayout listCheckBox;
    CheckBox checkbox;

    ArrayList<Problem> problems;
    HashMap<Integer, Boolean> sols;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_solver);
        dbhelper = new SQLiteHelper(this);

        try {
            dbhelper.createDataBase();
            dbhelper.openDataBase();
        } catch (Exception ioe) {
            throw new Error("Unable to create database");
        }

        problems= new ArrayList<>();
        sols= new HashMap<>();
//        listCheckBox = (LinearLayout) findViewById(R.id.);
        initProbs();

        initSols();
        initChecklist();

        FloatingActionButton fab = findViewById(R.id.fabProblems);

        RecyclerView rvProblems = findViewById(R.id.rvProblems);

        ProblemsAdapter adapter = new ProblemsAdapter(problems);
        rvProblems.setAdapter(adapter);
        rvProblems.setLayoutManager(new LinearLayoutManager(this));

        rvProblems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    fab.hide();
                } else if (dy < 0) {
                    fab.show();
                }
            }
        });
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
//        listCheckBox.removeAllViews();
        initChecklist();
    }
    private void initProbs(){
        Cursor cursor = dbhelper.getData("SELECT * FROM problems WHERE "+filterText);
        problems.clear();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String problem = cursor.getString(1);
            Cursor cursor2 = dbhelper.getData("SELECT solution_id FROM problems_solutions WHERE problem_id ="+id);
            ArrayList<Integer> sols = new ArrayList<>();
            while(cursor2.moveToNext()){
                sols.add(cursor2.getInt(0));
            }
            problems.add(new Problem(id,problem,sols));
        }
    }
    private void initChecklist(){
        for (Problem object: problems) {
            checkbox = new CheckBox(this);
            checkbox.setId(problems.indexOf(object));
            checkbox.setText(object.problem);
            checkbox.setOnClickListener(getOnClickDoSomething(checkbox));
//            listCheckBox.addView(checkbox);
            
            Button btn = new Button(this);
            btn.setText("DELETE");
            btn.setMaxWidth(250);
            btn.setOnClickListener((View v) -> {
                dbhelper.deleteProblemAndSolution((long) object.id);
                resetCheckbox();
                Toast.makeText(this, "Problem Deleted!", Toast.LENGTH_SHORT).show();
            });
//            listCheckBox.addView(btn);

            Button editBtn = new Button(this);
            editBtn.setText("EDIT");
            editBtn.setMaxWidth(250);
            editBtn.setOnClickListener((View v) -> {
                FragmentManager manager = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putSerializable("problem", object);
                EditProblemAndSolution newFragment = new EditProblemAndSolution();
                newFragment.setArguments(bundle);
                FragmentTransaction t = manager.beginTransaction();
                t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                t.add(android.R.id.content, newFragment).addToBackStack(null).commit();
            });
//            listCheckBox.addView(editBtn);
        }
    }

    View.OnClickListener getOnClickDoSomething(final CheckBox button){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Integer xad: problems.get(button.getId()).solution){
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