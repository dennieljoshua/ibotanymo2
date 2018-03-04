package com.bcklup.ibotanymo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }
    public void showPlanner(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void showSolver(View view){
        Intent intent = new Intent(this, ProblemSolver.class);
        startActivity(intent);
    }

    public void showGuide(View view){
        Intent intent = new Intent(this, ProblemSolver.class);
        startActivity(intent);
    }
}
