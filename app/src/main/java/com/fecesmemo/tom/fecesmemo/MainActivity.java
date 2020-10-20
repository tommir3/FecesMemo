package com.fecesmemo.tom.fecesmemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI(){
        Button btnChart = findViewById(R.id.btnChart);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnList = findViewById(R.id.btnList);
        btnAdd.setOnClickListener(buttonOnClickListener);
        btnChart.setOnClickListener(buttonOnClickListener);
        btnList.setOnClickListener(buttonOnClickListener);
    }

    View.OnClickListener buttonOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Fragment curFragment = null;
            switch(v.getId()){
                case R.id.btnChart:
                    curFragment = new Chart_Layout();
                    break;
                case R.id.btnAdd:
                    curFragment = new Feces_Information();
                    break;
                case R.id.btnList:
                    curFragment = new List_Layout();
                    break;
                default:
                    break;
            }
            if(null != curFragment){
                FragmentManager fraManager = getSupportFragmentManager();
                FragmentTransaction fraTrans = fraManager.beginTransaction();
                fraTrans.replace(R.id.fragment,curFragment);
                fraTrans.commit();
            }
        }
    };
}
