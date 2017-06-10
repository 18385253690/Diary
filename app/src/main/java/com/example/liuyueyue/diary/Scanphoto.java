package com.example.liuyueyue.diary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
public class Scanphoto extends Activity {
	private Button last;
	private Button next;
	private Button exit;
	private ImageView imageview;
	
	int num = 0;
	  @Override  
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.scanphoto);
	        setResult(Activity.RESULT_CANCELED);
	        initUI();
	    }
	private void initUI() {
		// TODO Auto-generated method stub
		last = (Button)findViewById(R.id.last);
		last.setOnClickListener(new lastListener());
		
		next = (Button)findViewById(R.id.next);
		next.setOnClickListener(new nextListener());
		
		exit = (Button)findViewById(R.id.exit);
		exit.setOnClickListener(new exitListener());
		
		imageview = (ImageView)findViewById(R.id.imageview);
	}
	public class exitListener implements OnClickListener{

		public void onClick(View arg0) {
			finish();
		}
		
	}
	public class lastListener implements OnClickListener{


		public void onClick(View arg0) {
			num--;
			if(num>=0){
				 String fileName = "storage/sdcard0/data/"+num+".jpg";
				 File f = new File(fileName);
				 if (f.exists()) {
				    Bitmap bm = BitmapFactory.decodeFile(fileName);
				    imageview.setImageBitmap(bm);
				}	
			}
		}
		
	}
	public class nextListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {

			num++;
		     String fileName = "storage/sdcard0/data/"+num+".jpg";
			File f = new File(fileName);
			if (f.exists()) {
			    Bitmap bm = BitmapFactory.decodeFile(fileName);
			    imageview.setImageBitmap(bm);

			}
		}
		
	}
	
}
