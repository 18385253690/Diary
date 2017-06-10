package com.example.liuyueyue.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

public class LogoActivity extends Activity{
	private ProgressBar progressBar;  
    private Button backButton;  
    int i = 0; 
    boolean cancel = true;
    
  private Handler handler = new Handler();
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        // ȥ������  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.logo);  
  
        progressBar = (ProgressBar) findViewById(R.id.pgBar);  
        backButton = (Button) findViewById(R.id.btn_back);  
          
        handler.removeCallbacks(runnable);
		handler.postDelayed(runnable,500);

        backButton.setOnClickListener(new OnClickListener() {  
  
            public void onClick(View v) {  
               finish();  
               
            }  
        });  
  
    }  
    private Runnable runnable = new Runnable() {
        public void run () {
        progressBar.setMax(100); 
        
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(i);     
        i += 30;
        
        if(i>100 && (cancel ==true)){
        	Intent intent = new Intent(LogoActivity.this, Firstpage.class);  
            LogoActivity.this.startActivity(intent);
                      
        }
        handler.postDelayed(this,1000); 
     }
   };

	protected void onDestroy() {
		super.onDestroy();
		cancel = false;
	}

	protected void onPause() {
		super.onPause();
		cancel = false;
	}
	   
}
