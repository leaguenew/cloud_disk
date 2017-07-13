package com.example.sambaservertestproject;

import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {
	
	private JLANServerServiceImpl service = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					InputStream in = MainActivity.this.getResources().getAssets().open("jlanConfig.xml");
					service = new JLANServerServiceImpl();
					service.start(new InputStreamReader(in));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (service != null) {
			service.stop();
		}
	}

}
