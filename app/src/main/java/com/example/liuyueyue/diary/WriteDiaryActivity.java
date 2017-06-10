package com.example.liuyueyue.diary;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
/**
 * Created by liuyueyue on 2017/6/9.
 */

public class WriteDiaryActivity extends Activity {
    private TextView time;
    private Button takephoto;
    private Button finish;
    private Button delete;
    private Button change;
    private EditText diarycontent;
    private Button scanphoto;
    private Button save;
    private Button autocam;
    private ImageView imageview4, imageview1, imageview2, imageview3;
    private SoundPool sp;//声明一个SoundPool
    private int music;//定义一个整型用load（）；
    private DBAdapter dbAdapter;

    int count = 0;
    String diary;
    String data;
    String aa;
    String timeid;
    int num = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writediarycome);
        initUI();

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initUI() {
        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        music = sp.load(this, R.raw.jie, 1);
        autocam = (Button) findViewById(R.id.autocam);
        autocam.setOnClickListener(new autocamListener());
        imageview3 = (ImageView) findViewById(R.id.imageview3);
        imageview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentFocus();
                sp.play(music, 1, 1, 0, 0, 1);//拍照声音
            }
        });
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new saveListener());

        change = (Button) findViewById(R.id.change);
        change.setOnClickListener(new changeListener());

        diarycontent = (EditText) findViewById(R.id.diarycontent);
        diary = diarycontent.getText().toString();

        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new deleteListener());

        finish = (Button) findViewById(R.id.finish);
        finish.setOnClickListener(new finishListener());
        imageview4 = (ImageView) findViewById(R.id.imageview4);
        imageview4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                finish();
            }
        });
        takephoto = (Button) findViewById(R.id.takephoto);
        takephoto.setOnClickListener(new takephotoListener());
        imageview1 = (ImageView) findViewById(R.id.imageview1);
        imageview1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        scanphoto = (Button) findViewById(R.id.sanphoto);
        scanphoto.setOnClickListener(new scanphotoListener());
        imageview2 = (ImageView) findViewById(R.id.imageview2);
        imageview2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(WriteDiaryActivity.this, Scanphoto.class);
                startActivity(intent);
            }
        });
        time = (TextView) findViewById(R.id.time);
        //获取手机当前时间
        time();
        time1();//获取手机日期，年月日
        receiveaadata();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    //获取手机当前年月日
    private void time1() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        data = sDateFormat.format(new java.util.Date());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    //获取时间
    private void time() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String date = sDateFormat.format(new java.util.Date());
        time.setText(date);
    }
// 接收显示编辑diary
    private void receiveaadata() {
        Intent intent = getIntent();
        aa = intent.getStringExtra("diary");
        diarycontent.setText(aa);
        if (aa != null) {
            save.setEnabled(false);//设置保存按钮为不可操作状态
        }
    }

    //监听器
    private class autocamListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            getCurrentScreen();

        }
    }

    //退出监听器
    private class finishListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    //删除监听器
    private class deleteListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showDialog();

        }
    }

    //截屏
    public void getCurrentScreen() {
        num++;

        //构建Bitmap
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();
        Bitmap imageBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //获取屏幕
        View decorview = this.getWindow().getDecorView();//decor意思是装饰布置
        decorview.setDrawingCacheEnabled(true);
        imageBitmap = decorview.getDrawingCache();
        String SaveImageFilePath = getSDCardPath() + "/data";
        //保存Bitmap
        try {
            File path = new File(SaveImageFilePath);
            String imagepath = SaveImageFilePath + "/data" + num + ".png";//保存图片的路径
            File file = new File(imagepath);
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            if (null != fos) {
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
                Toast.makeText(this, "图片已经已保存在手机卡data文件目录下", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//获取SDCard的目录路径功能
    private String getSDCardPath() {
        String SDCardPath = null;
        boolean IsSDcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (IsSDcardExist) {
            SDCardPath = Environment.getExternalStorageDirectory().toString();//SD卡的路径为: /mnt/sdcard
        }
        return SDCardPath;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();//关闭数据库
    }

    //delete自定义对话框
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog, null);
        builder.setView(layout);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(R.string.dialogtitle);
        builder.setMessage("请问您是否要删除该天记事？");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

                diarycontent.setText("");
                Toast.makeText(getBaseContext(), "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("cancel",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        final AlertDialog dlg = builder.create();
        dlg.show();
    }

    //修改内容监听器
    private class changeListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            People people = new People();
            people.Diary = diarycontent.getText().toString();
            people.Time = data;
            Intent intent = getIntent();
            timeid = intent.getStringExtra("timeid");
            if(timeid!=null) {
                int id = Integer.parseInt(timeid);
                long count = dbAdapter.updateOneData(id, people);

                if (count == 1) {
                    Toast.makeText(getApplication(), "更新错误", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "更新成功", Toast.LENGTH_SHORT).show();
                }
                finish();
            }else{
                Toast.makeText(getApplication(), "请点击保存", Toast.LENGTH_SHORT).show();
            }
            hintKb();
        }

    }

//打开与关闭软件键盘
    private void hintKb(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    if(imm.isActive()){
        //if开启
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);
    }
    }
//保存数据监听器
    private class saveListener implements View.OnClickListener {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onClick(View v) {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            data = sDateFormat.format(new Date());
            People people = new People();
            people.Time = data;
            people.Diary = diarycontent.getText().toString();
            long colunm = dbAdapter.insert(people);
            if(colunm  == -1){
                Toast.makeText(getApplication(),"添加错误",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"成功添加数据",Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
//浏览拍好的照片
    private class scanphotoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(WriteDiaryActivity.this,Scanphoto.class);
            startActivity(intent);
        }
    }
//执行拍照
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(resultCode == Activity.RESULT_OK){
           count++;
           String sdstatus = Environment.getExternalStorageState();
           if(!sdstatus.equals(Environment.MEDIA_MOUNTED)){
               Log.i("TestFile","SD card is not avaiable/writeable right now.");
               return;
           }
           Bundle bundle = data.getExtras();
           Bitmap bitmap = (Bitmap) bundle.get("data");
           FileOutputStream b = null;
           File file = new File("/sdcard/data/");
           file.mkdirs();// 创建文件夹
           String s = String.valueOf(count)+".jpg";
           String fileName = "/sdcard/data/"+s;
           try{
               b = new FileOutputStream(fileName);
               bitmap.compress(Bitmap.CompressFormat.JPEG,100,b);
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           }finally {
               try{
                   b.flush();
                   b.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
        super.onActivityResult(requestCode, resultCode, data);
    }
//takepho监听器
    private class takephotoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,1);
        }
    }

}

