package com.example.liuyueyue.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button test;
    private Button logon;
    private EditText zhanghao,mima;
    private CheckBox rememberpassword,autologon;
    private SharedPreferences sp;
    String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initUI();
    }

    private void initUI() {
        test = (Button)findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }
        });

        //���ʵ������
        sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);

        logon = (Button)findViewById(R.id.logon);
        logon.setOnClickListener(new logonListener());

        zhanghao = (EditText)findViewById(R.id.zhanghao);
        mima = (EditText)findViewById(R.id.mima);

        rememberpassword = (CheckBox)findViewById(R.id.rememberpassword);
        rememberpassword.setChecked(true);
        autologon = (CheckBox)findViewById(R.id.autologon);

        remembepassword();
    }

    public class logonListener implements View.OnClickListener {
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            username = zhanghao.getText().toString();
            password = mima.getText().toString();
            if(username.equals("liu")&&password.equals("123"))
            {
                Toast.makeText(MainActivity.this,"��¼�ɹ�", Toast.LENGTH_SHORT).show();
                if(rememberpassword.isChecked())
                {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("USER_NAME", username);
                    editor.putString("PASSWORD",password);
                    editor.commit();
                }
                //��ת����
                Intent intent = new Intent(MainActivity.this,LogoActivity.class);
                MainActivity.this.startActivity(intent);

            }else{

                Toast.makeText(MainActivity.this,"", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void remembepassword(){
        if(sp.getBoolean("ISCHECK", false))
        {
            rememberpassword.setChecked(true);
            zhanghao.setText(sp.getString("USER_NAME", ""));
            mima.setText(sp.getString("PASSWORD", ""));
            if(sp.getBoolean("AUTO_ISCHECK", false))
            {
                rememberpassword.setChecked(true);
                Intent intent = new Intent(MainActivity.this,LogoActivity.class);
                MainActivity.this.startActivity(intent);

            }
        }
    }

    public void autologon(){
        if (autologon.isChecked()) {
            System.out.println("");

        } else {
            System.out.println("");

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    }
