package com.fecesmemo.tom.fecesmemo;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Fragment[] _viewFragments;      //子页面数组
    private LineChart chart;        //图表控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    //初始化UI界面
    private void initUI(){
        Button btnChart = findViewById(R.id.btnChart);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnList = findViewById(R.id.btnList);
        btnAdd.setOnClickListener(buttonOnClickListener);
        btnChart.setOnClickListener(buttonOnClickListener);
        btnList.setOnClickListener(buttonOnClickListener);
        initChart();
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
//                FragmentManager fraManager = getSupportFragmentManager();
//                FragmentTransaction fraTrans = fraManager.beginTransaction();
//                fraTrans.replace(R.id.viewContent,curFragment);
//                fraTrans.commit();
            }
        }
    };

    private void initChart(){
        chart = findViewById(R.id.chart);
        List dataList = new ArrayList();
        dataList.add(new Entry(20201005,1));
        dataList.add(new Entry(20201009,3));
        dataList.add(new Entry(20201010,1));
        dataList.add(new Entry(20201011,2));
        dataList.add(new Entry(20201015,1));
        dataList.add(new Entry(20201016,3));
        dataList.add(new Entry(20201017,1));
        dataList.add(new Entry(20201018,2));
        LineDataSet lineDataSet=new LineDataSet(dataList,null);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(5f);
        LineData lineData=new LineData(lineDataSet);
        chart.setData(lineData);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
    }
}
