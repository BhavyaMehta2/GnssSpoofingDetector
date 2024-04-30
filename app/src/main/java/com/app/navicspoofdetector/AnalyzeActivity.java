package com.app.navicspoofdetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Arrays;

public class AnalyzeActivity extends AppCompatActivity {

    RecyclerView simpleGrid;
    TextView meanTV;
    TextView hypothesisTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        double[][] coefficients;
        coefficients =(double[][]) getIntent().getSerializableExtra("coeffs");

        hypothesisTV = findViewById(R.id.hypothesisTV);
        meanTV = findViewById(R.id.meanTV);
        simpleGrid = findViewById(R.id.simpleGridView);

        HeatMapAdapter adapter = new HeatMapAdapter(coefficients, this);
        assert coefficients != null;
        GridLayoutManager layoutManager=new GridLayoutManager(this,coefficients.length);

        double mean = 0.0;
        int count = 0;
        for(int i =0; i<coefficients.length;i++)
        {
            for(int j = 0; j<=i; j++)
            {
                count++;
                if(coefficients[i][j]!=-1)
                    mean += coefficients[i][j];
            }
        }

        mean /= count;

        meanTV.setText("Coeffecient Mean: "+Math.round(mean*1000.0)/1000.0);

        hypothesisTV.setText("Hypothesis: "+((mean>0.5)?"Spoofing Detected":"Nominal Conditions"));

        simpleGrid.setLayoutManager(layoutManager);
        simpleGrid.setAdapter(adapter);
    }
}