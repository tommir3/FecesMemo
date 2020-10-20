package com.fecesmemo.tom.fecesmemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Fragment[] _viewFragments;      //子页面数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    //初始化UI界面
    private void initUI(){
        initFragments();
        Button btnChart = findViewById(R.id.btnChart);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnList = findViewById(R.id.btnList);
        btnAdd.setOnClickListener(buttonOnClickListener);
        btnChart.setOnClickListener(buttonOnClickListener);
        btnList.setOnClickListener(buttonOnClickListener);
    }
    //初始化子页面
    private void initFragments(){
        _viewFragments = new Fragment[]{
                new Chart_Layout(),new Feces_Information(),new List_Layout()
        };
        FragmentManager fraManager = getSupportFragmentManager();
        FragmentTransaction fraTrans = fraManager.beginTransaction();
        fraTrans.add(R.id.viewContent,new Chart_Layout());
        fraTrans.commit();
    }
    //按钮点击事件
    View.OnClickListener buttonOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Fragment curFragment = null;
            switch(v.getId()){
                case R.id.btnChart:
                    curFragment = _viewFragments[0];
                    break;
                case R.id.btnAdd:
                    curFragment = _viewFragments[1];
                    break;
                case R.id.btnList:
                    curFragment = _viewFragments[2];
                    break;
                default:
                    break;
            }
            if(null != curFragment){
                FragmentManager fraManager = getSupportFragmentManager();
                FragmentTransaction fraTrans = fraManager.beginTransaction();
                fraTrans.replace(R.id.viewContent,curFragment);
                fraTrans.commit();
            }
        }
    };
}
