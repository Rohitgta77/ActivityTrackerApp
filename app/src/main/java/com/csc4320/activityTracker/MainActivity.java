package com.csc4320.activityTracker;

import android.content.Intent;
import android.opengl.EGLExt;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private LinearLayout rowsRegion;
    private FloatingActionButton addActivityBtn;
    private ArrayList<ActivityWorkout> activityList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rowsRegion = findViewById(R.id.rowsRegion);

        findViewById(R.id.startWorkOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWorkout();
            }
        });

        addActivityBtn = findViewById(R.id.addActivity);
        addActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWorkOutView();
            }
        });
    }

    private void startWorkout() {
        activityList.clear();

        LinearLayout rowsRegion = (LinearLayout) findViewById(R.id.rowsRegion);

        boolean submitForm = true;
        if (rowsRegion.getChildCount() == 0) {
            Toast.makeText(MainActivity.this, "Please Add an activity", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < rowsRegion.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) rowsRegion.getChildAt(i);
            EditText e1 = (EditText) linearLayout.getChildAt(0);
            EditText e2 = (EditText) linearLayout.getChildAt(1);
            String workOut = e1.getText().toString();
            String seconds = e2.getText().toString();
            ActivityWorkout workoutRecord = new ActivityWorkout();

            if (TextUtils.isEmpty(workOut)) {
                e1.requestFocus();
                e1.setError("Enter WorkOut Name");
                submitForm = false;
            } else {
                workoutRecord.setWorkOutName(workOut);
            }

            if (TextUtils.isEmpty(seconds)) {
                e2.requestFocus();
                e2.setError("Enter Time in Seconds");
                submitForm = false;
            } else {
                workoutRecord.setSeconds(Integer.parseInt(seconds));
            }

            if (submitForm) {
                activityList.add(workoutRecord);
            }
        }

        if (!submitForm) {
            Toast.makeText(MainActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        } else {
            startTimerActivity(activityList);
        }
    }

    private void startTimerActivity(ArrayList<ActivityWorkout> arrayList) {
        Intent intent = new Intent(  this, TimerActivity.class);
        intent.putExtra("data", arrayList);
        startActivity(intent);
    }

    private void addWorkOutView() {
        final View workOutView = getLayoutInflater().inflate(R.layout.row_add_workout, null, false);
        rowsRegion = (LinearLayout) findViewById(R.id.rowsRegion);
        rowsRegion.addView(workOutView);
        ImageView deleteIcon = (ImageView) workOutView.findViewById(R.id.del_icon);
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rowsRegion.removeView(workOutView);
            }
        });
        //AutoTextComplete
        final String[] workoutName = getResources().getStringArray(R.array.workout_array);

        for (int i = 0; i < rowsRegion.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) rowsRegion.getChildAt(i);
            AutoCompleteTextView e1 = (AutoCompleteTextView) linearLayout.getChildAt(0);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,workoutName);
            e1.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}