package com.example.liuyueyue.diary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by liuyueyue on 2017/6/9.
 */

public class Login extends Activity implements DialogInterface.OnClickListener, View.OnClickListener {
    Boolean falg;
    private TextView display;
    private Button c, clear, cancel, helplogin;
    private Button[] btnNum = new Button[10];//数值按钮
    private EditText[] editNum = new EditText[5];//数值按钮
    int number = 0;
    int number1 = 0;
    int editnumber = 0;
    String num = "", num1 = "", num2 = "", num3 = "", num4 = "";//定义5个输入密码

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initUI();

    }

    private void initUI() {
        helplogin = (Button) findViewById(R.id.helplogin);
        helplogin.setOnClickListener(new helploginListener());

        c = (Button) findViewById(R.id.c);
        c.setOnClickListener(new cListener());

        clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(new clearListener());

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new cancelListener());

        display = (TextView) findViewById(R.id.display);
        editNum[0] = (EditText) findViewById(R.id.editone);
        editNum[1] = (EditText) findViewById(R.id.edittwo);
        editNum[2] = (EditText) findViewById(R.id.editthree);
        editNum[3] = (EditText) findViewById(R.id.editfour);
        editNum[4] = (EditText) findViewById(R.id.editfive);

        btnNum[0] = (Button) findViewById(R.id.one);
        btnNum[1] = (Button) findViewById(R.id.two);
        btnNum[2] = (Button) findViewById(R.id.three);
        btnNum[3] = (Button) findViewById(R.id.four);
        btnNum[4] = (Button) findViewById(R.id.five);
        btnNum[5] = (Button) findViewById(R.id.six);
        btnNum[6] = (Button) findViewById(R.id.seven);
        btnNum[7] = (Button) findViewById(R.id.eight);
        btnNum[8] = (Button) findViewById(R.id.nine);
        btnNum[9] = (Button) findViewById(R.id.zero);
        read();
        for (int i = 0; i < 10; i++) {
            btnNum[i].setOnClickListener(this);
        }
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            read();
            handler.postDelayed(this, 1000);
        }
    };

    private void read() {
        SharedPreferences ferences = getSharedPreferences("softinfo",0);
        num=ferences.getString("editNum[0]", "");
        num1=ferences.getString("editNum[1]", "");
        num2=ferences.getString("editNum[2]", "");
        num3=ferences.getString("editNum[3]", "");
        num4=ferences.getString("editNum[4]", "");
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {


    }

    @Override
    public void onClick(View v) {
        if ((num.equals("")) || (num.equals("")) || (num.equals("")) || (num.equals("")) || (num.equals(""))) {
            SharedPreferences preferences = getSharedPreferences("softinfo", Context.MODE_WORLD_READABLE);
            SharedPreferences.Editor edit = preferences.edit();
            Button btn1 = (Button) v;
            String input1 = btn1.getText().toString();
            editNum[number1].setText(input1);
            number1++;
            if (number1 > 4) {
                number1 = 0;
                for (int i = 0; i < 10; i++) {
                    btnNum[i].setEnabled(false);
                }
            }
            edit.putString("editNum[0]", editNum[0].getText().toString());
            edit.putString("editNum[1]", editNum[1].getText().toString());
            edit.putString("editNum[2]", editNum[2].getText().toString());
            edit.putString("editNum[3]", editNum[3].getText().toString());
            edit.putString("editNum[4]", editNum[4].getText().toString());
            edit.commit();
            if (number1 == 3) {
                Toast.makeText(getApplication(), "输完后请再输入一次密码", Toast.LENGTH_SHORT).show();
            }
        }
        if (isNumeric(num) && isNumeric(num1) && isNumeric(num2) && isNumeric(num3) && isNumeric(num4)) {
            Button btn = (Button) v;
            String input = btn.getText().toString();
            editNum[number].setText(input);
            if ((num.equals(editNum[0].getText().toString())) && (num1.equals(editNum[1].getText().toString())) && (num2.equals(editNum[2].getText().toString()))
                    && (num3.equals(editNum[3].getText().toString())) && (num4.equals(editNum[4].getText().toString()))) {

                Intent intent = new Intent(Login.this, Firstpage.class);
                startActivity(intent);
            }
            editnumber++;
            if (editnumber > 4) {
                editnumber = 4;

            }
            number++;
            if (number > 4) {
                number = 0;
                //输完密码后设置控件为不可操作状态
                for (int i = 0; i < 10; i++) {
                    btnNum[i].setEnabled(false);
                }
            }

        }
    }

    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]");
        return pattern.matcher(str).matches();
    }

    private class helploginListener implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            dialog();

        }

    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.helpcontent);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("温馨提示");
        builder.setPositiveButton("确认",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    private class cListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            btnsetEnabaled();
            number--;
            if(number<0){
                number=0;
            }
            editNum[editnumber].setText("");
            editnumber--;
            if(editnumber<0){
                editnumber=0;
            }
        }
    }
    protected void onDestroy(){
        for(int i = 0;i<10;i++){
            btnNum[i].setEnabled(true);
        }
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    private void btnsetEnabaled() {
        for(int i = 0;i<10;i++){
            btnNum[i].setEnabled(true);
        }
    }
protected void onRestart(){
    super.onRestart();
    btnsetEnabaled();
    for(int i=0;i<5;i++){
        editNum[i].setText("");
    }
}
    private class clearListener implements View.OnClickListener {
        @Override
        public void onClick(View arg0) {
            for(int i = 0;i<5;i++){
                editNum[i].setText("");
            }
            btnsetEnabaled();
            read();
        }
    }

    private class cancelListener implements View.OnClickListener {
        @Override
        public void onClick(View arg0) {
            finish();
        }
    }
}
