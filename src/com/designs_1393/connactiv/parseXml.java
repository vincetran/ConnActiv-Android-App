package com.designs_1393.connactiv;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;
import java.lang.Exception;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import java.lang.String;
import android.content.Context;
import android.content.Intent;
import java.util.concurrent.locks.ReentrantLock;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.util.Log;

//HTTP Stuff
import android.widget.ProgressBar;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.HttpEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import java.util.ArrayList;

//XML Stuff
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import java.net.URL;

public class parseXml extends Activity
{
	private ProgressBar pb;
	private TextView tv;
	private static final String generate = "generate";
	private static final String pull = "pull";
	private String messageXml = "";
	private ReentrantLock xmlLock = new ReentrantLock();
	private SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parse);
		

		tv = new TextView(this);

		genXml gen = new genXml();
		gen.execute(generate);

		/*
		while(gen.getStatus() == AsyncTask.Status.RUNNING)
		{
			//Run some code
		}
		*/
		genXml pullXml = new genXml();
		pullXml.execute(pull);
		/*
		while(pullXml.getStatus() == AsyncTask.Status.RUNNING)
		{
			//Run some code
		}
		*/
		startActivity(new Intent(getApplicationContext(), stream.class));
		finish();
	}

	private class genXml extends AsyncTask<String, Void, Integer>
	{
		protected Integer doInBackground(String... params)
		{
			if(params[0].compareTo("generate") == 0)
			{
				String resp = "";
				try{
					prefs = getSharedPreferences("connactivPrefs", Activity.MODE_PRIVATE);

					HttpClient httpclient = new DefaultHttpClient();
					HttpPost hp = new HttpPost("http://connactiv.com/test/android/genXML.php");

					List<NameValuePair> postParams = new ArrayList<NameValuePair>(1);
					postParams.add(new BasicNameValuePair("userId", prefs.getString("userId","error")));
					hp.setEntity( new UrlEncodedFormEntity(postParams) );

					HttpResponse response = httpclient.execute(hp); 
					BasicResponseHandler brh = new BasicResponseHandler();
					resp = brh.handleResponse( response );

					//WE REALLY SHOULDN'T USE THIS BECAUSE IT'S SO BAD
					//while(resp.compareTo("Success") != 0)
					//{}
					//LIKE SERIOUSLY. BAD.
				}catch(Exception e){
					Log.e("Connactiv", "Error: "+e);
				}
			}
			else if(params[0].compareTo("pull")==0){
				try{
					URL url = new URL("http://connactiv.com/test/android/connactionDb.xml");
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp = spf.newSAXParser();
					XMLReader xr = sp.getXMLReader();
					connactionHandler caHandler = new connactionHandler();
					caHandler.setContext(getApplicationContext());

					xr.setContentHandler(caHandler);
					xr.parse(new InputSource(url.openStream()));
				}catch(Exception e){
					Log.i("ConnActiv", "ERRRRROR: " + e);
				}
			}
			return 0;
		}

	}
}
