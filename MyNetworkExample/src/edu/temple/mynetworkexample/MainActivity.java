package edu.temple.mynetworkexample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	
	EditText urlTextView;
	WebView display;
	Button goButton;

	Handler showContent = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			display.loadData((String) msg.obj, "text/html", "UTF-8");
			return false;
		}
	});
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		display = (WebView) findViewById(R.id.display);
		urlTextView = (EditText) findViewById(R.id.url_textfield);
		goButton = (Button) findViewById(R.id.go_button);
		
		
		
		goButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				Thread loadContent = new Thread(){
					@Override
					public void run(){
				
						if (isNetworkActive()){
							
							URL url = null;
							
							try {
								url = new URL(urlTextView.getText().toString());
								BufferedReader reader = new BufferedReader(
										new InputStreamReader(
												url.openStream()));
								
								String response = "", tmpResponse = "";
								
								tmpResponse = reader.readLine();
								while (tmpResponse != null){
									response = response + tmpResponse;
									tmpResponse = reader.readLine();
								}
								
								Message msg = Message.obtain();
								
								msg.obj = response;
								
								showContent.sendMessage(msg);
								
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					}
				};
				
				loadContent.start();

			}
		});
		
		
	}
	
	
	public boolean isNetworkActive(){
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}
}
