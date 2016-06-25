package com.example.wutao.graphguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String COMMOND = "COMMOND";
    public static final String PARAMS = "PARAMS";
    public static final int DISTANCE_OF_TRPI = 0;
    public static final int TRIPS_WITH_MAX_STOPS = 1;
    public static final int TRIPS_WIYH_EXACTLY_STOPS = 2;
    public static final int SHORTEST_LENGTH_OF_TWO_STOP = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        findViewById(R.id.distance_submit_button).setOnClickListener(this);
        findViewById(R.id.limit_step_solution_submit).setOnClickListener(this);
        findViewById(R.id.fixt_step_solution_submit).setOnClickListener(this);
        findViewById(R.id.short_distance_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.distance_submit_button:
                EditText editText = (EditText) findViewById(R.id.route_input_edit);
                if(checkTripInputIlegal(editText.getText())){
                    goGraphActivity(DISTANCE_OF_TRPI, editText.getText().toString().trim());
                }else {
                    showToast();
                }
                break;
            case R.id.limit_step_solution_submit: {
                Editable start = ((EditText) findViewById(R.id.limit_step_start_stop_edit)).getText();
                Editable end = ((EditText) findViewById(R.id.limit_step_end_stop_edit)).getText();
                Editable limitStep = ((EditText) findViewById(R.id.limit_max_step_num_input_edit)).getText();
                if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(start.toString().trim()) &&
                        !TextUtils.isEmpty(end) && !TextUtils.isEmpty(end.toString().trim())
                        && !TextUtils.isEmpty(limitStep) && !TextUtils.isEmpty(limitStep.toString().trim())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(start.toString().trim()).append(end.toString().trim()).append(limitStep.toString().trim());
                    goGraphActivity(TRIPS_WITH_MAX_STOPS, sb.toString().toUpperCase());
                } else {
                    showToast();
                }
            }
                break;
            case R.id.fixt_step_solution_submit: {
                Editable start = ((EditText) findViewById(R.id.fixt_step_start_stop_edit)).getText();
                Editable end = ((EditText) findViewById(R.id.fixt_step_end_stop_edit)).getText();
                Editable limitStep = ((EditText) findViewById(R.id.fixt_max_step_num_input_edit)).getText();
                if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(start.toString().trim()) &&
                        !TextUtils.isEmpty(end) && !TextUtils.isEmpty(end.toString().trim())
                        && !TextUtils.isEmpty(limitStep) && !TextUtils.isEmpty(limitStep.toString().trim())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(start.toString().trim()).append(end.toString().trim()).append(limitStep.toString().trim());
                    goGraphActivity(TRIPS_WIYH_EXACTLY_STOPS, sb.toString().toUpperCase());
                } else {
                    showToast();
                }
            }
                break;
            case R.id.short_distance_submit:
            {
                Editable start = ((EditText) findViewById(R.id.short_distance_start_edit)).getText();
                Editable end = ((EditText) findViewById(R.id.short_distance_end_edit)).getText();
                if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(start.toString().trim()) &&
                        !TextUtils.isEmpty(end) && !TextUtils.isEmpty(end.toString().trim())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(start.toString().trim()).append(end.toString().trim());
                    goGraphActivity(SHORTEST_LENGTH_OF_TWO_STOP, sb.toString().toUpperCase());
                } else {
                    showToast();
                }
            }
                break;
        }
    }

    private void goGraphActivity(int commond, String params){
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra(COMMOND, commond);
        intent.putExtra(PARAMS, params.toUpperCase());
        startActivity(intent);
    }

    private boolean checkTripInputIlegal(Editable input){
        if(TextUtils.isEmpty(input) || TextUtils.isEmpty(input.toString().trim())){
            return false;
        }
        String params = input.toString();
        params = params.trim();
        if(Pattern.matches("[a-zA-Z]{2,}", params)){
            String upParam = params.toUpperCase();
            for (int i = 0; i < upParam.length() - 1; i++){
                if(upParam.charAt(i) == upParam.charAt(i+1)){
                    return false;
                }
            }
        }
        return true;
    }

    private void showToast() {
        Toast.makeText(this, R.string.invalid_input, Toast.LENGTH_SHORT).show();
    }

}
