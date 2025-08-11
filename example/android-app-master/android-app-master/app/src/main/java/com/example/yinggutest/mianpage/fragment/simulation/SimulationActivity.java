package com.example.yinggutest.mianpage.fragment.simulation;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.yinggutest.R;
import com.example.yinggutest.SearchActivity;
import com.example.yinggutest.mianpage.fragment.testdata.ConstantData;

import java.util.List;

public class SimulationActivity extends AppCompatActivity {


    private TextView tvback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);

        tvback = findViewById(R.id.tv_back);
        ListView testsListView = findViewById(R.id.lv_tests_list);
        List<SimulationEntity> entityList = ConstantData.toEntityList();
        SimulationAdapter adapter = new SimulationAdapter(this, entityList);
        testsListView.setAdapter(adapter);

        initEvent();


        // requestData();

    }

    private void initEvent() {
        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimulationActivity.this.finish();
            }
        });
    }


   /* private void requestData() {
        AnSwerInfo.addAll(testsList);
    }*/
}