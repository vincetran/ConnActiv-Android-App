package com.designs_1393.connactiv;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.database.Cursor;
import android.content.Intent;
import android.content.Context;
import java.lang.Exception;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import java.lang.String;
import java.util.concurrent.locks.ReentrantLock;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.widget.Toast;
import android.widget.ListView;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.content.DialogInterface;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.util.Log;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

public class stream extends ListActivity
{
	private dbAdapter dbHelper;
	private Cursor connCursor;
	private SharedPreferences prefs;
	private DateFormat oldDF = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
	private DateFormat newDF = new SimpleDateFormat("MM'/'dd'/'yy' @ 'HH:mm");

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stream);

		parseXml();

		// create database
		dbHelper = new dbAdapter(this);

		// open database
		dbHelper.open();
		connCursor = dbHelper.fetchAllConnactions();

		fillData();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		Cursor c = connCursor;
		c.moveToPosition(position);

		AlertDialog.Builder builder;
		LayoutInflater inflater = (LayoutInflater) (this).getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.connaction_dialog,
			(ViewGroup) findViewById(R.id.layout_root));

		TextView message = (TextView) layout.findViewById(R.id.message);
		message.setText(c.getString(c.getColumnIndexOrThrow(dbAdapter.KEY_MESSAGE)));
		TextView date = (TextView) layout.findViewById(R.id.date);
		try{
			date.setText(newDF.format(oldDF.parse(c.getString(c.getColumnIndexOrThrow(dbAdapter.KEY_START_DATE))) ) +
				 " until " + newDF.format(oldDF.parse( c.getString(c.getColumnIndexOrThrow(dbAdapter.KEY_END_DATE)))));
		}catch(Exception e) {Log.i("ConnActiv", "Error with parsing date: "+ e);}
		TextView location = (TextView) layout.findViewById(R.id.location);
		location.setText(c.getString(c.getColumnIndexOrThrow(dbAdapter.KEY_LOCATION)));
		TextView levels = (TextView) layout.findViewById(R.id.levels);

		String[] allLevels = c.getString(c.getColumnIndexOrThrow(dbAdapter.KEY_LEVELS)).split("|");
		levels.setText("Level "+allLevels[7]+", seeking level "+allLevels[5]+", but I'm cool with levels "+allLevels[1]+"-"+allLevels[3]+".");

		builder = new AlertDialog.Builder(this);
		builder.setView(layout);
		builder.setIcon(R.drawable.icon);
		builder.setTitle("ConnAction with "+ c.getString(c.getColumnIndexOrThrow(dbAdapter.KEY_UID)));
		builder.setPositiveButton("Back", 
			new android.content.DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int arg1) {
				}
			});
		builder.show();
	}


	@Override
	protected void onStop()
	{
		super.onStop();
		dbHelper.close();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		dbHelper.close();
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		dbHelper.open();
		fillData();
	}

	private void fillData()
	{
		//Will get the information from the subs database and display it onto the main View
		connCursor = dbHelper.fetchAllConnactions();
		startManagingCursor(connCursor);

		streamAdapter sAdapter = new streamAdapter(getApplicationContext(), connCursor);
		setListAdapter(sAdapter);
	}

	private ProgressBar pb;
	private static final String generate = "generate";
	private static final String pull = "pull";
	private String messageXml = "";

	public void parseXml()
	{
		genXml gen = new genXml();
		gen.execute(generate);

		genXml pullXml = new genXml();
		pullXml.execute(pull);

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

					httpclient.execute(hp); 

				}catch(Exception e){
					Log.e("Connactiv", "Error: "+e);
				}
			}
			else if(params[0].compareTo("pull")==0){
				try{
					prefs = getSharedPreferences("connactivPrefs", Activity.MODE_PRIVATE);
					URL url = new URL("http://connactiv.com/test/android/"+prefs.getString("userId","error")+"connactionDb.xml");

					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp = spf.newSAXParser();
					XMLReader xr = sp.getXMLReader();
					connactionHandler caHandler = new connactionHandler();
					caHandler.setContext(getApplicationContext());

					xr.setContentHandler(caHandler);
					xr.parse(new InputSource(url.openStream()));
					/*
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost hp = new HttpPost("http://connactiv.com/test/android/deleteXML.php");
					List<NameValuePair> postParams = new ArrayList<NameValuePair>(1);
					postParams.add(new BasicNameValuePair("userId", prefs.getString("userId","error")));
					hp.setEntity( new UrlEncodedFormEntity(postParams) );
					httpclient.execute(hp); 
					*/
				}catch(Exception e){
					Log.i("ConnActiv", "STREAM ERRRRROR: " + e);
				}
			}
			return 0;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.stream_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menu_refresh:
				parseXml();
				fillData();
				return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
