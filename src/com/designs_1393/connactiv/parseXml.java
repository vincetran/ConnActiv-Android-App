package com.designs_1393.connactiv;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;
import java.lang.Exception;
import android.os.AsyncTask;
import java.lang.String;

//Actionbar
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

//HTTP Stuff
import android.widget.ProgressBar;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

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

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parse);

		tv = new TextView(this);

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Parsing XML...");

		genXml gen = new genXml();
		gen.execute(generate);

		genXml pullXml = new genXml();
		pullXml.execute(pull);

		tv.setText(messageXml);
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
