package com.example.liuyueyue.diary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * Created by liuyueyue on 2017/6/9.
 */
public class Firstpage extends Activity{
    private Button writediary;
    private Button set;
    private Button deletediary;
    private Button scandiary;
    private Button firstfinish;
    private ListView diarylistview;

    private PopupWindow popupwindow;
    private DBAdapter dbAdapter;


    String result = "";
    String itemdata,itemdata1;
    private ArrayAdapter<String> Diaryadapter;
     protected void onCreate(Bundle saveInstanceState){
         super.onCreate(saveInstanceState);
         setContentView(R.layout.firstpage);
         requestWindowFeature(Window.FEATURE_NO_TITLE);
         dbAdapter = new DBAdapter(this);
         dbAdapter.open();
         initUI();
         showdeleteDialog();
     }

    private void initUI() {
// 初使化设备存储数组
        Diaryadapter = new ArrayAdapter<String>(this, R.layout.listview_format);
        diarylistview = (ListView)findViewById(R.id.diaryshow);
        diarylistview.setOnItemClickListener(new diarylistviewItemListener());
        diarylistview.setAdapter(Diaryadapter);

        firstfinish = (Button)findViewById(R.id.firstfinish);
        firstfinish.setOnClickListener(new firstfinishListener());

        set = (Button)findViewById(R.id.set);
        set.setOnClickListener(new setListener());

        writediary = (Button)findViewById(R.id.writediary);
        writediary.setOnClickListener(new writeListener());

        deletediary = (Button)findViewById(R.id.deletediary);
        deletediary.setOnClickListener(new deletediaryListener());

        scandiary = (Button)findViewById(R.id.scandiary);
        scandiary.setOnClickListener(new scandiaryListener());
        displaydiary();//登录进去后显示所有日记
        photodialog();

    }
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            displaydiary();
            handler.postDelayed(this,1000);
        }
    };
//图片对话框
    private void photodialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
   builder.setIcon(R.drawable.welcome);
        builder.setTitle("welcome");
        builder.setPositiveButton("确认",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }
});
        builder.create().show();

    }
// 更新listview数据并拆分数据得到id和时间后面的数据
    private void displaydiary() {
        Diaryadapter.clear();
        People[] people = dbAdapter.queryAllData();
        if(people == null){
            return;
        }
        for (People aPeople : people) {
            result = aPeople.toString() + "\n";
            String spStrone[] = result.split(",时间:");//匹配字符串
            Diaryadapter.add(spStrone[1]);
        }
        //添加完后将result清空，防止下次点击会累加
        result="";
    }

//listview item事件监听器，并根据listview id获取到数据库数据，再赋值给itemdata1
    private class diarylistviewItemListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            for(int i = 0;i<1000;i++){
                if(arg2 == i){
                    itemdata = Diaryadapter.getItem(arg2);
                    Diaryadapter.clear();
                    String result1="";
                    String c = String.valueOf(arg3);
                    int cc = Integer.parseInt(c);
                    People[] people = dbAdapter.queryAllData();
                    for(int j=0;j<people.length;j++){
                        result1 = people[cc].toString()+"\n";
                    }
                    itemdata1 = result1;
                    //点击listviewitem后弹出item对话框
                    showItemDialog();
                }
            }
        }
    }
//弹出item事件处理对话框
    private void showItemDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.item_dialog, null);
        builder.setView(layout);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(R.string.itemdialogtitle);
        Button deleteitem  = (Button)layout.findViewById(R.id.deleteitem);
        //删除该天记事监听器
        deleteitem.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("static-access")
            public void onClick(View arg0) {
                String spStr1[] = itemdata1.split(",时间");//匹配字符串
                int  id = Integer.parseInt(spStr1[0]);
                long result = dbAdapter.deleteOneData(id);
                Log.i(dbAdapter.DB_ACTION, "delete long :"+result);
                // String msg = "删除ID为"+id+"的数据" + (result>0?"成功":"失败");
                String msg = "数据删除" + (result>0?"成功":"失败");
                Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show();
                displaydiary();
            }
        });
// 编辑该天事件监听器
        Button editordialog  = (Button)layout.findViewById(R.id.editordialog);
        editordialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(Firstpage.this,WriteDiaryActivity.class);
                String spStr[] = itemdata1.split("日记内容:");//匹配字符串
                String spStr1[] = itemdata1.split(",时间");//匹配字符串
                intent.putExtra("diary", spStr[1]);  //获取内容
                intent.putExtra("timeid", spStr1[0]);//获取id
                startActivity(intent);
            }
        });
//cancel
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        final AlertDialog dlg = builder.create();
        dlg.show();
    }
    //关闭定时器
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
        handler.removeCallbacks(runnable);
    }
    //停止定时器
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
    //重启时启动定时器，定时器一直放在初始化中一直执行太耗内存，但会出现少许延迟
    @Override
    protected void onRestart() {
        super.onRestart();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,200);
    }
    //停止定时器


    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    //退出
    private class firstfinishListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
           finish();
        }
    }
//打开列表监听器
    private class setListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.set:
                    set.setText("设置");
                    if(popupwindow != null && popupwindow.isShowing()){
                        popupwindow.dismiss();
                        return;
                    }else{
                        set.setText("关闭");
                        initmPopupWindowView();
                        popupwindow.showAsDropDown(v,0,5);
                    }
                    break;
                default:
                    break;
            }
        }
    }
//弹出列表method
    private void initmPopupWindowView() {
// 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.view_item,null,false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupwindow = new PopupWindow(customView, 300, 300);
        // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
        popupwindow.setAnimationStyle(R.style.AnimationFade);
        // 自定义view添加触摸事件
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(popupwindow != null && popupwindow.isShowing()){
                    popupwindow.dismiss();
                    popupwindow = null;
                }
                return false;
            }
        });
        //初始化view_item UI
        Button grid = (Button) customView.findViewById(R.id.grid);
        Button list = (Button) customView.findViewById(R.id.more);
        final CheckBox gridCB = (CheckBox)customView.findViewById(R.id.gridCB);
        final CheckBox listCB = (CheckBox)customView.findViewById(R.id.listCB);
        //宫格显示监听
        gridCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gridCB.setChecked(true);
                Toast.makeText(getApplication(),"gridCB",Toast.LENGTH_LONG).show();
            }
        });
        //列表显示监听
        listCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listCB.setChecked(true);
                Toast.makeText(getApplication(),"listCB",Toast.LENGTH_LONG).show();
            }
        });
        //设置
        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Firstpage.this,Setting.class);
                startActivity(intent);
                set.setText("设置");
                popupwindow.dismiss();
            }
        });
        //list按钮监听器
        list.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Toast.makeText(getApplicationContext(), "没有更多选项了", Toast.LENGTH_SHORT).show();
                set.setText("设置");
                popupwindow.dismiss();
            }
        });
    }
//写笔记监听器
    private class writeListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Firstpage.this,WriteDiaryActivity.class);
            startActivity(intent);
        }
    }
    // 删除所有记事对话框
    public void showdeleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog, null);
        builder.setView(layout);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(R.string.firstdialogtitle);
        builder.setMessage("请问您是否要删除所有记事？");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                dbAdapter.deleteAllData();
                Diaryadapter.clear(); //删除数据后清空listview显示
                Toast.makeText(getApplication(), "日志已删除", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        final AlertDialog dlg = builder.create();
        dlg.show();
    }
    //监听器
    private class deletediaryListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //弹出是否删除全部数据对话框
            showItemDialog();
        }
    }
//监听器
    private class scandiaryListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            displaydiary();
        }
    }
}
