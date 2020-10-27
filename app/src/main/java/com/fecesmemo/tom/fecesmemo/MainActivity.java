package com.fecesmemo.tom.fecesmemo;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DBHelper _db;       //数据库辅助类
    private ChartModel _curChartModel = ChartModel.Line;//当前图表模式
    private LineChart _lineChart;        //折现图表控件
    private BarChart _barChart;         //柱状图表控件
    public enum ChartModel{
        Line, Bar
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _db = new DBHelper(this);
        _db.AddTestDatas();
        initUI();
    }

    //初始化UI界面
    private void initUI(){
        _lineChart = findViewById(R.id.lineChart);
        _barChart = findViewById(R.id.barChart);
        initLineChart();
        initBarChart();
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
        //setChartModel(_curChartModel);
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
            if(!isChecked){
                return;
            }
            FecesInfo[] infos = null;
            Date lowDate = null;
            switch(buttonView.getId()){
                case R.id.rbtnHalfMonth:
                    lowDate = DateHelper.GetBeforeDayDate(15);
                    break;
                case R.id.rbtnMonth:
                    lowDate = DateHelper.GetBeforeMonthDate(1);
                    break;
                case R.id.rbtnHalfYear:
                    lowDate = DateHelper.GetBeforeMonthDate(6);
                    break;
                case R.id.rbtnYear:
                    lowDate = DateHelper.GetBeforeMonthDate(12);
                    break;
                case R.id.rbtnAll:
                    lowDate = null;
                    break;
                default:
                    lowDate = null;
                    break;
            }
            infos = _db.GetFecesInfosByDate(lowDate,null);
            if(_curChartModel == ChartModel.Line){
                setLineData(infos);
            }else if(_curChartModel == ChartModel.Bar){
                setBarData(infos);
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
                    curFragment = new Feces_Information();
                    break;
                case R.id.btnList:
                    curFragment = new List_Layout();
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
    //初始化线性图表
    private void initLineChart(){
        //_lineChart = findViewById(R.id.chart);
        //_lineChart = new LineChart(this);
        _lineChart.getAxisLeft().setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）
        //对于右下角一串字母的操作
        _lineChart.getDescription().setEnabled(false);                  //是否显示右下角描述
        //chart.getDescription().setText("这是修改那串英文的方法");    //修改右下角字母的显示
        _lineChart.getDescription().setTextSize(20);                    //字体大小
        //是否隐藏右边的Y轴（不设置的话有两条Y轴 同理可以隐藏左边的Y轴）
        _lineChart.getAxisRight().setEnabled(false);
        _lineChart.getLegend().setEnabled(false);
        XAxis xAxis=_lineChart.getXAxis();
        xAxis.setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        //xAxis.setAxisLineColor(Color.RED);   //X轴颜色
        xAxis.setAxisLineWidth(2);           //X轴粗细
        xAxis.setLabelRotationAngle(-30);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return DateHelper.DateToString(DateHelper.LongToDate((long)v),"YYYY MM-dd HH:mm");
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);        //X轴所在位置   默认为上面
        YAxis yAxis = _lineChart.getAxisLeft();
        yAxis.setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的竖线）
        yAxis.setZeroLineWidth(2);
//        yAxis.setAxisMinimum(0);
//        yAxis.setAxisMaximum(100);
        yAxis.setGranularity(1);
    }
    //初始化柱状图表
    private void initBarChart(){
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
    }
    //设置线性图表数据
    private void setLineData(FecesInfo[] infos){
        List dataList = new ArrayList();
        Date minDate, maxDate;
        int intervals = 0;
        for(int i = 0; i < infos.length; ++i){
            maxDate = DateHelper.StringToDate(infos[i].dateStr);
            if(i > 0){
                minDate = DateHelper.StringToDate(infos[i - 1].dateStr);
                intervals = DateHelper.GetDaysBetweenDate(minDate,maxDate);
            }else{
                intervals = 0;
            }
            dataList.add(new Entry(Float.valueOf(DateHelper.DateToLong(DateHelper.StringToDate(infos[i].dateStr))),Float.valueOf(intervals)));
        }
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
    //设置柱状图表数据
    private void setBarData(FecesInfo[] infos){
        Map<Long,Integer> mapData = new HashMap<Long,Integer>();
        long key = -1;
        for(int i = 0; i < infos.length; ++i){
            key = DateHelper.DateToLong(DateHelper.StringToDate(infos[i].dateStr));
            if(!mapData.containsKey(key)){
                mapData.put(key,1);
            }else{
                mapData.put(key,mapData.get(key) + 1);
            }
        }
        List dataList = new ArrayList();
        Iterator<Long> iter = mapData.keySet().iterator();
        while(iter.hasNext()){
            Long k=iter.next();
            Integer value = mapData.get(key);
            dataList.add(new BarEntry(k,value));
        }
        BarDataSet dataSet=new BarDataSet(dataList,null);
        BarData barData=new BarData(dataSet);
        _barChart.setData(barData);
        _barChart.setDragEnabled(true);
        _barChart.setScaleEnabled(true);
        _barChart.notifyDataSetChanged();
        _barChart.invalidate();
    }

    private void setChartModel(ChartModel model){
        RadioGroup rg = findViewById(R.id.rGropChartData);
        _curChartModel = model;
        if(model == ChartModel.Line){
            _lineChart.setVisibility(View.VISIBLE);
            _barChart.setVisibility(View.GONE);
            rg.check(rg.getCheckedRadioButtonId());
        }else if(model == ChartModel.Bar){
            _lineChart.setVisibility(View.GONE);
            _barChart.setVisibility(View.VISIBLE);
            rg.check(rg.getCheckedRadioButtonId());
        }
    }
}
