package com.fecesmemo.tom.fecesmemo;

import android.opengl.Visibility;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Fragment[] _viewFragments;      //子页面数组
    private LineChart _lineChart;        //折现图表控件
    private BarChart _barChart;         //柱状图表控件
    public enum ChartModel{
        Line, Bar
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    //初始化UI界面
    private void initUI(){
        _lineChart = findViewById(R.id.lineChart);
        _barChart = findViewById(R.id.barChart);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnList = findViewById(R.id.btnList);
        RadioButton rbtnHalfMonth = findViewById(R.id.rbtnHalfMonth);
        RadioButton rbtnMonth = findViewById(R.id.rbtnMonth);
        RadioButton rbtnHalfYear = findViewById(R.id.rbtnHalfYear);
        RadioButton rbtnYear = findViewById(R.id.rbtnYear);
        RadioButton rbtnAll = findViewById(R.id.rbtnAll);
        RadioButton rbtnInterval = findViewById(R.id.rbtnInterval);
        RadioButton rbtnCount = findViewById(R.id.rbtnCount);
        btnAdd.setOnClickListener(buttonOnClickListener);
        btnList.setOnClickListener(buttonOnClickListener);
        rbtnInterval.setOnCheckedChangeListener(radioButtonChartTab);
        rbtnCount.setOnCheckedChangeListener(radioButtonChartTab);
        rbtnHalfMonth.setOnCheckedChangeListener(radioButtonChartData);
        rbtnMonth.setOnCheckedChangeListener(radioButtonChartData);
        rbtnHalfYear.setOnCheckedChangeListener(radioButtonChartData);
        rbtnYear.setOnCheckedChangeListener(radioButtonChartData);
        rbtnAll.setOnCheckedChangeListener(radioButtonChartData);
        rbtnInterval.setChecked(true);
        rbtnAll.setChecked(true);
    }
    //图表切换事件
    private CompoundButton.OnCheckedChangeListener radioButtonChartTab = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getId()){
                case R.id.rbtnInterval:
                    if(isChecked){
                        setChartModel(ChartModel.Line);
                    }
                    break;
                case R.id.rbtnCount:
                    if(isChecked){
                        setChartModel(ChartModel.Bar);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    //图表数据切换的事件
    private CompoundButton.OnCheckedChangeListener radioButtonChartData = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getId()){
                case R.id.rbtnHalfMonth:
                    if(isChecked){
                        return;
                    }
                    break;
                case R.id.rbtnMonth:
                    if(isChecked){
                        return;
                    }
                    break;
                case R.id.rbtnHalfYear:
                    if(isChecked){
                        return;
                    }
                    break;
                case R.id.rbtnYear:
                    if(isChecked){
                        return;
                    }
                    break;
                case R.id.rbtnAll:
                    if(isChecked){
                        return;
                    }
                    break;
                default:
                    break;
            }
        }
    };
    //按钮点击事件
    private View.OnClickListener buttonOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Fragment curFragment = null;
            switch(v.getId()){
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

    private void setLineData(){
        //_lineChart = findViewById(R.id.chart);
        //_lineChart = new LineChart(this);
        _lineChart.getXAxis().setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        _lineChart.getAxisLeft().setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）
        //对于右下角一串字母的操作
        _lineChart.getDescription().setEnabled(false);                  //是否显示右下角描述
        //chart.getDescription().setText("这是修改那串英文的方法");    //修改右下角字母的显示
        _lineChart.getDescription().setTextSize(20);                    //字体大小
        //是否隐藏右边的Y轴（不设置的话有两条Y轴 同理可以隐藏左边的Y轴）
        _lineChart.getAxisRight().setEnabled(false);
        XAxis xAxis=_lineChart.getXAxis();
        //xAxis.setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        //xAxis.setAxisLineColor(Color.RED);   //X轴颜色
        //xAxis.setAxisLineWidth(2);           //X轴粗细
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);        //X轴所在位置   默认为上面
        List dataList = new ArrayList();
        dataList.add(new Entry(1,1));
        dataList.add(new Entry(2,3));
        dataList.add(new Entry(3,1));
        dataList.add(new Entry(4,2));
        dataList.add(new Entry(5,1));
        dataList.add(new Entry(6,3));
        dataList.add(new Entry(7,1));
        dataList.add(new Entry(8,2));
        LineDataSet dataSet=new LineDataSet(dataList,null);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(5f);
        LineData lineData=new LineData(dataSet);
        _lineChart.setData(lineData);
        _lineChart.setDragEnabled(true);
        _lineChart.setScaleEnabled(true);
        _lineChart.notifyDataSetChanged();
        _lineChart.invalidate();
    }

    private void setBarData(){
        //_barChart = findViewById(R.id.chart);
        //_barChart = new BarChart(this);
        _barChart.getXAxis().setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        _barChart.getAxisLeft().setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）
        //对于右下角一串字母的操作
        _barChart.getDescription().setEnabled(false);                  //是否显示右下角描述
        //chart.getDescription().setText("这是修改那串英文的方法");    //修改右下角字母的显示
        _barChart.getDescription().setTextSize(20);                    //字体大小
        //是否隐藏右边的Y轴（不设置的话有两条Y轴 同理可以隐藏左边的Y轴）
        _barChart.getAxisRight().setEnabled(false);
        XAxis xAxis=_barChart.getXAxis();
        //xAxis.setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        //xAxis.setAxisLineColor(Color.RED);   //X轴颜色
        //xAxis.setAxisLineWidth(2);           //X轴粗细
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);        //X轴所在位置   默认为上面
        List dataList = new ArrayList();
        dataList.add(new BarEntry(1,1));
        dataList.add(new BarEntry(2,3));
        dataList.add(new BarEntry(3,1));
        dataList.add(new BarEntry(4,2));
        dataList.add(new BarEntry(5,1));
        dataList.add(new BarEntry(6,3));
        dataList.add(new BarEntry(7,1));
        dataList.add(new BarEntry(8,2));
        BarDataSet dataSet=new BarDataSet(dataList,null);
        BarData barData=new BarData(dataSet);
        _barChart.setData(barData);
        _barChart.setDragEnabled(true);
        _barChart.setScaleEnabled(true);
        _barChart.notifyDataSetChanged();
        _barChart.invalidate();
    }

    private void setChartModel(ChartModel model){
        if(model == ChartModel.Line){
            _lineChart.setVisibility(View.VISIBLE);
            _barChart.setVisibility(View.GONE);
            setLineData();
        }else if(model == ChartModel.Bar){
            _lineChart.setVisibility(View.GONE);
            _barChart.setVisibility(View.VISIBLE);
            setBarData();
        }
    }
}
