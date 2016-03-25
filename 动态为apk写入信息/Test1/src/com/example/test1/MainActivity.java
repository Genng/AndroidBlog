package com.example.test1;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView ex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ex=(TextView) findViewById(R.id.tex_1);
		String path=Tools.getPackagePath(getApplicationContext());
		if(path!=null){
			String msg=Tools.readApk(new File(path));
			ex.setText("内容："+msg);
		}
		
	}
	
	
	
	
}
