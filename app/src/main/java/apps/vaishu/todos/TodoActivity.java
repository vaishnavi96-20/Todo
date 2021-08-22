package apps.vaishu.todos;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TodoActivity extends AppCompatActivity  {

    private EditText txtTitle, txtNotes, txtDueDate;
    private RadioGroup priorityGroup;
    private RadioButton highBtn;
    private RadioButton lowBtn;
    private RadioButton mediumBtn;
    private Button saveBtn, btnDueDate;
    private Button markasCompleteBtn;
    private DatePicker datePicker;
    private ImageButton btnCalendar;

    private Integer selectedPriority =  new Integer(0);
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_todo);
        // initialize and add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize fields
        txtTitle = (EditText) findViewById(R.id.titleId);
        txtNotes = (EditText) findViewById(R.id.notesId);
        saveBtn = (Button) findViewById(R.id.btnSave);
        markasCompleteBtn = (Button) findViewById(R.id.btnMarkCompleted);
        priorityGroup = (RadioGroup) findViewById(R.id.priorityGroupId);
        highBtn = (RadioButton) findViewById(R.id.highId);
        mediumBtn = (RadioButton) findViewById(R.id.mediumId);
        lowBtn = (RadioButton) findViewById(R.id.lowId);
        btnDueDate = (Button) findViewById(R.id.btnDueDate);
        txtDueDate = (EditText) findViewById(R.id.txtDueDate);
        btnCalendar = (ImageButton) findViewById(R.id.btnCalendar);
        // Delete on clicking delete image button
        ImageButton deleteBtn = (ImageButton) findViewById(R.id.deleteBtn);

        // int checkedRadioId = priorityGroup.getCheckedRadioButtonId();
        priorityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                if (null != radioButton && checkedId != -1) {
                    switch (checkedId) {
                        case R.id.highId:
                            selectedPriority = 3;
                            break;
                        case R.id.mediumId:
                            selectedPriority = 2;
                            break;
                        case R.id.lowId:
                            selectedPriority = 1;
                            break;
                        default:
                            selectedPriority = 0;
                            break;
                    }
                }
            }
        });


        // Fetch data passed from activity 1
        Intent intent = getIntent();
        id = intent.getStringExtra("TODOID");
        String title = intent.getStringExtra("TODOTITLE");
        String notes = intent.getStringExtra("TODONOTES");
        String pri = intent.getStringExtra("TODOPRI");
        Boolean done = intent.getBooleanExtra("TODODONE", false);
        String dueDate = intent.getStringExtra("DUEDATE");
        if( id == null){
            //System.out.println("Add mode");
            markasCompleteBtn.setVisibility(View.INVISIBLE);
            deleteBtn.setVisibility(View.INVISIBLE);
        }else if(Integer.parseInt(id) > 0){
            //System.out.println("id " + id);
            markasCompleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);
            if(title != null){
                txtTitle.setText(title);
            }
            if(notes != null){
                txtNotes.setText(notes);
            }
            if(pri != null){
                if(pri.equals("3")){
                    highBtn.setChecked(true);
                }else if(pri.equals("2")){
                    mediumBtn.setChecked(true);
                }if(pri.equals("1")){
                    lowBtn.setChecked(true);
                }
            }
            if(done){
                // if a to do is complete, hide save and mark as complete buttons
                saveBtn.setVisibility(View.INVISIBLE);
                markasCompleteBtn.setVisibility(View.INVISIBLE);
            }
            if(dueDate !=  null){
                txtDueDate.setText(dueDate);
            }
        }

        saveBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(validate()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("TODOID", id);
                    returnIntent.putExtra("TODOTITLE", txtTitle.getText().toString());
                    returnIntent.putExtra("TODONOTES", txtNotes.getText().toString());
                    returnIntent.putExtra("TODOPRI", selectedPriority.toString());
                    returnIntent.putExtra("DUEDATE", txtDueDate.getText().toString());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

        markasCompleteBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent returnIntent =  new Intent();
                returnIntent.putExtra("TODODONE", true);
                returnIntent.putExtra("TODOID", id);
                returnIntent.putExtra("TODOTITLE", txtTitle.getText().toString());
                returnIntent.putExtra("TODONOTES", txtNotes.getText().toString());
                returnIntent.putExtra("TODOPRI", selectedPriority.toString());
                returnIntent.putExtra("DUEDATE", txtDueDate.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent returnIntent =  new Intent();
                returnIntent.putExtra("TODOID", id);
                returnIntent.putExtra("ISDELETE", true);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });



        btnDueDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // Get Current Date
                int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                final DatePickerDialog datePickerDialog = new DatePickerDialog(TodoActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDueDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dueDate = null;
                    if(!validate()){
                        return;
                    }
                else {
                    try {
                        dueDate = new SimpleDateFormat("dd-MM-yyyy").parse(txtDueDate.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar calendarEvent = Calendar.getInstance();
                    calendarEvent.setTimeInMillis(dueDate.getTime());
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setType("vnd.android.cursor.item/event");
                    intent.putExtra("beginTime", dueDate.getTime());
                    intent.putExtra("allDay", true);
                    intent.putExtra("rule", "FREQ=YEARLY");
                    intent.putExtra("endTime", dueDate.getTime() + 60 * 60 * 1000);
                    intent.putExtra("title", txtTitle.getText().toString());
                    intent.putExtra("description", txtNotes.getText().toString());
                    startActivity(intent);
                }
            }
        });

   }

    private Boolean validate() {
        if (txtDueDate.getText().toString().equals(null) || txtDueDate.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a due date", Toast.LENGTH_LONG).show();
            return false;
        } else if (txtTitle.getText().toString().equals(null) || txtTitle.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Please enter a title", Toast.LENGTH_LONG).show();
            return false;
        }else if (selectedPriority.equals(0)){
            Toast.makeText(getApplicationContext(), "Please enter a priority", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
