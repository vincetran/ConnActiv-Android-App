package com.designs_1393.connactiv;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.graphics.Typeface;
import android.widget.Button;
import android.view.View;
import android.widget.ProgressBar;
import android.util.Log;
import android.preference.PreferenceManager;
import android.util.Log;
import android.content.SharedPreferences;

// authentication
// TODO: prune
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.CoreProtocolPNames;
import android.net.http.AndroidHttpClient;
import java.io.ByteArrayOutputStream;

// threading
import android.os.AsyncTask;
import java.lang.Void;
import java.util.concurrent.locks.ReentrantLock;

public class registerPart2 extends Activity
{
	
	final String TAG = "ConnActiv";
	Context ctx;
	ReentrantLock regLock = new ReentrantLock();

	String httpResponse = "";
	BasicHttpContext httpCtx = new BasicHttpContext();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerPart2);
		ctx = getApplicationContext();

	}

	private class regTask extends AsyncTask<String, Void, Integer>
	{
		protected Integer doInBackground(String... strings)
		{
			//BRAOIHFOAH WAT?
			HttpPost getNetworks = new HttpPost("http://connactiv.com/test/android/getNetworks.php");
			final DefaultHttpClient client2 = new DefaultHttpClient(getNetworks.getParams());
			BasicResponseHandler brh2 = new BasicResponseHandler();
			HttpResponse response2 = client2.execute( getNetworks, httpCtx );
			final String resp2 = brh2.handleResponse( response2 );

		}	
	}
}