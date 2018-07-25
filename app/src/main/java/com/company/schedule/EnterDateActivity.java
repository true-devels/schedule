package com.company.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class EnterDateActivity extends AppCompatActivity implements View.OnClickListener{

    final private String TAG = "myLog EnterDateActivity";  // tag for log

    Button btnDatePickerOK, btnDatePickerCancel;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker);

        datePicker = (DatePicker) findViewById(R.id.datePicker);

        //TODO try delete this
        Calendar calendar = Calendar.getInstance();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                Toast.makeText(getApplicationContext(), datePicker.getDayOfMonth()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getYear(), Toast.LENGTH_SHORT).show();
            }
        });
        //--------------

        btnDatePickerOK = (Button) findViewById(R.id.btnDatePickerOK);
        btnDatePickerOK.setOnClickListener(this);

        btnDatePickerCancel = (Button) findViewById(R.id.btnDatePickerCancel);
        btnDatePickerCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
        case R.id.btnDatePickerOK:
            Intent intentReturnDate = new Intent();
            intentReturnDate.putExtra("day_of_month", datePicker.getDayOfMonth());
            intentReturnDate.putExtra("month", datePicker.getMonth() + 1);  // we must add 1 because by default computer start count month from 0
            intentReturnDate.putExtra("year", datePicker.getYear());
            Toast.makeText(getApplicationContext(), String.valueOf(datePicker.getDayOfMonth()), Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, intentReturnDate);
            finish();
            break;  // so calmer
        case R.id.btnDatePickerCancel:
            // TODO handle button cancel
            break;
        }
    }
}
