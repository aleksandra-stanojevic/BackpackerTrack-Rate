package com.example.aleksandra.backpack.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.aleksandra.backpack.R;

public class FiltersActivity extends AppCompatActivity   {

    boolean sw1_enabled=false;
    boolean sw2_enabled=false;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Switch sw1=(Switch)findViewById(R.id.switch1);
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditText ed=(EditText) findViewById(R.id.ed_rad_num);


                if(isChecked)
                {
                    sw1_enabled=true;
                    ed.setEnabled(true);
                }
                else
                {
                    sw1_enabled=false;
                    ed.setEnabled(false);
                }

            }

        });
        final Switch sw2=(Switch)findViewById(R.id.switch2);
        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Button sb=(Button) findViewById(R.id.btn_ok);
                if(isChecked)
                {
                    sw2_enabled=false;
                    sb.setEnabled(true);
                }
                else
                {
                    sw2_enabled=true;
                    sb.setEnabled(false);
                }

            }

        });
        Button btn_search=(Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FiltersActivity.this, GoogleMapActivity.class);
                EditText ed=(EditText)findViewById(R.id.ed_rad_num);
                TextView tags=(TextView)findViewById(R.id.tags);
                if(sw1_enabled)
                {
                    i.putExtra("sw1",ed.getText().toString());

                }
                if(sw2_enabled)
                {
                    i.putExtra("sw2",tags.getText().toString());
                }
                startActivity(i);
            }
        });

    }

    public void onOkClick(View v) {
        EditText ed=(EditText) findViewById(R.id.et_add_tags);
        TextView tags=(TextView) findViewById(R.id.tags);
        String text=ed.getText().toString();
        if(!text.isEmpty())
        {
            tags.setText(tags.getText()+" "+text);
            ed.setText("");
        }
    }

    public void onCleanClick(View view)
    {
        TextView tags=(TextView) findViewById(R.id.tags);
        tags.setText("tags");

    }
}
