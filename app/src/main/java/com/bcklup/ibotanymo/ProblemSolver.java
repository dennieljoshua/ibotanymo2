package com.bcklup.ibotanymo;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ProblemSolver extends AppCompatActivity {

    String solutionString = "";

    Map<String, Boolean> sol= new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_solver);
        initSol();
    }
    private void initSol(){
        sol.put("blight", false);
        sol.put("youngwilt", false);
        sol.put("maturewilt", false);
        sol.put("weakplant", false);
        sol.put("stuntedyellow", false);
        sol.put("yellowwilt", false);
        sol.put("brownspots", false);
        sol.put("burntedges", false);
        sol.put("burntfall", false);
        sol.put("curled", false);
        sol.put("powderywhite", false);
        sol.put("whitespots", false);
        sol.put("parasites", false);
    }
    public void showSolutionDialog(View view){
        buildSolution();
        showProblemFullDialog(ProblemSolver.this);
    }

    public void setSolutionParams(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()){
            case R.id.blight:
                if(checked) sol.put("blight",true);
                break;
            case R.id.youngwilt:
                if(checked) sol.put("youngwild",true);
                break;
            case R.id.maturewilt:
                if(checked) sol.put("maturewilt",true);
                break;
            case R.id.weakplant:
                if(checked) sol.put("weakplant",true);
                break;
            case R.id.stuntedyellow:
                if(checked) sol.put("stuntedyellow",true);
                break;
            case R.id.yellowwilt:
                if(checked) sol.put("yellowwilt",true);
                break;
            case R.id.brownspots:
                if(checked) sol.put("brownspots",true);
                break;
            case R.id.burntedges:
                if(checked) sol.put("burntedges",true);
                break;
            case R.id.burntfall:
                if(checked) sol.put("burntfall",true);
                break;
            case R.id.curled:
                if(checked) sol.put("curled",true);
                break;
            case R.id.powderywhite:
                if(checked) sol.put("powderywhite",true);
                break;
            case R.id.whitespots:
                if(checked) sol.put("whitespots",true);
                break;
            case R.id.parasites1:
            case R.id.parasites2:
            case R.id.parasites3:
            case R.id.parasites4:
                if(checked) sol.put("parasites",true);
                break;
        }
    }
    private void buildSolution(){
//        for(int i=0; i < sol.size(); i++){
//            if(sol.forEach();)
//        }
        solutionString ="";
        for(Map.Entry<String, Boolean> entry : sol.entrySet()) {
            String key = entry.getKey();
            Boolean value = entry.getValue();

            if(value){
                switch(key){
                    case "blight":
                        solutionString+="• Prune or Stake plants to improve air circulation and counter fungus. Disinfect pruning sheers against fungi.\n\n";
                        break;
                    case "youngwilt":
                        solutionString+="• Keep the garden free of rotting plant matter and weeds. Try treating fungicide and avoit overwatering.\n\n";
                        break;
                    case "maturewilt":
                        solutionString+="• Regulate water. Check for rotting roots and try letting the soil dry up if too watered out.\n\n";
                        break;
                    case "weakplant":
                        solutionString+="• Try to space out your plants and give sufficient sunlight. Also avoid watering down and giving too much notrogen.\n\n";
                        break;
                    case "stuntedyellow":
                        solutionString+="• Plant is suffering from insufficient nutrients. Rebase your fertilizer scheme.\n\n";
                        break;
                    case "yellowwilt":
                        solutionString+="• Test the soil for lack of spcific nutriets and ensure sufficient sunlight.\n\n";
                        break;
                    case "brownspots":
                        solutionString+="• Lessen fertilizers/pesticides. Also check for approriate temperature in the plant area.\n\n";
                        break;
                    case "burntedges":
                        solutionString+="• Halt all chemical substances applied to the plant. This causes the chemical burn.\n\n";
                        break;
                    case "burntfall":
                        solutionString+="• There are signs of salt damage, too low temperatures and dry/over fertilized soil.\n\n";
                        break;
                    case "curled":
                        solutionString+="• Balance watering schedule with room humidity. Hold out on herbicides.\n\n";
                        break;
                    case "powderywhite":
                        solutionString+="• Try to take the plant outside and space it out against other plants.\n\n";
                        break;
                    case "whitespots":
                        solutionString+="• Use insecticidal sprays.\n\n";
                        break;
                    case "parasites":
                        solutionString+="• Barrier your plants with epson salt sprinkled around the soil.\n\n";
                        break;

                }
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
}
