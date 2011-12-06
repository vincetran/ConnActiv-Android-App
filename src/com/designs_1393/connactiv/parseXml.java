package com.designs_1393.connactiv;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;
import java.lang.Exception;
import android.os.AsyncTask;

//HTTP Stuff
import android.widget.ProgressBar;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

//XML Stuff


public class parseXml extends Activity
{
	ProgressBar pb;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stream);
		pb = (ProgressBar)findViewById(R.id.login_progress);
		genXml task = new genXml();
		task.execute();
		
	}

	private class genXml extends AsyncTask<Void, Void, String>
	{
		protected String doInBackground(Void... params)
		{
			String resp = "";
			try{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost hp = new HttpPost("http://connactiv.nfshost.com/test/android/genXML.php");
				HttpResponse response = httpclient.execute(hp); 
				BasicResponseHandler brh = new BasicResponseHandler();
				resp = brh.handleResponse( response );
			}catch(Exception e){
				Log.e("Connactiv - Parsing", "Error: "+e);
			}
			return resp;
		}
	}
}
