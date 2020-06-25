package com.postman.costscalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Dict extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict);
    }

    public void OnClickTypeDict(View view)
    {
        Intent intent = new Intent(this, AddType.class);
        startActivity(intent);
    }

    public void OnClickSubTypeDict(View view)
    {
        Intent intent = new Intent(this, AddSubtype.class);
        startActivity(intent);
    }

    public void OnClickItemDict(View view)
    {
        Intent intent = new Intent(this, AddItem.class);
        startActivity(intent);
    }
}
