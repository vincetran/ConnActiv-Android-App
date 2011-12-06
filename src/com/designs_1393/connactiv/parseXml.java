package com.designs_1393.connactiv;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;
import java.lang.Exception;
import android.os.AsyncTask;
import java.lang.String;

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
	private ProgressBar pb;
	private dbAdapter mDbHelper;
	private TextView tv;
	private static final String generate = "generate";
	private static final String pull = "pull";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stream);
		pb = (ProgressBar)findViewById(R.id.login_progress);
		genXml gen = new genXml();
		gen.execute(generate);

		mDbHelper = new dbAdapter(this);
		mDbHelper.open();

		pb.setVisibility(View.VISIBLE);
		genXml pullXml = new genXml();
		pullXml.execute(pull);

		tv = new TextView(this);

		tv.setText("Hello, Android");
		setContentView(tv);

	}

	private class genXml extends AsyncTask<String, Void, Integer>
	{
		protected Integer doInBackground(String... params)
		{
			if(params[0].compareTo("generate") == 0)
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
			}
			else if(params[0].compareTo("pull")==0){
				
			}
			return 0;
		}
	}
}
