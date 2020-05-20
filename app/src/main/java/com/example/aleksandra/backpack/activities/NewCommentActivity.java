package com.example.aleksandra.backpack.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.aleksandra.backpack.Models.CommentModel;
import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.R;

public class NewCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_comment);

        Button add = findViewById(R.id.bt_new_add);
        Button cancel = findViewById(R.id.bt_new_cancel);
        final EditText et = findViewById(R.id.ed_new_comment);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("name","Anne Smith");
                data.putExtra("state","England");
                data.putExtra("comment",et.getText().toString());
                data.putExtra("points", "10");

                setResult(RESULT_OK,data);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(5);
                finish();
            }
        });
    }
}
