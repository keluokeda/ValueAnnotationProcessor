package com.keluokeda.valueannotationprocessor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ke.annotation.Price;
import com.keluokeda.api.KPrice;

public class MainActivity extends AppCompatActivity {


    @Price(10)
    int value;

    @Price(200)
    int price =100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KPrice.injectPrice(this);
        TextView textView = (TextView) findViewById(R.id.tv_content);

        textView.setText(String.format("value = %d",value));


    }
}
