package com.designs_1393.connactiv;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.graphics.Typeface;
import android.widget.Button;
import android.view.View;
import android.widget.ProgressBar;

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

// cookies
import android.webkit.CookieSyncManager;
import org.apache.http.cookie.Cookie;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.protocol.BasicHttpContext;

import android.content.Intent;
import android.content.Context;

import android.widget.Toast;

// threading
import android.os.AsyncTask;
import java.lang.Void;
import java.util.concurrent.locks.ReentrantLock;

// DBG
import android.util.Log;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class connactiv extends Activity
{
	EditText email, pw;
	Button login;
	ProgressBar pb;

	final String TAG = "ConnActiv: connactiv";
	Context ctx;
	ReentrantLock loginLock = new ReentrantLock();

	String httpResponse = "";
	CookieStore cs = new BasicCookieStore();
	BasicHttpContext httpCtx = new BasicHttpContext();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		ctx = getApplicationContext();
		CookieSyncManager.createInstance(ctx);
		CookieSyncManager.getInstance().startSync();
		List<Cookie> cookieList = cs.getCookies();
		for( Cookie c : cookieList )
		Log.i(TAG, c.getName() );


		email = (EditText)findViewById(R.id.email);
		email.setTypeface(Typeface.DEFAULT);
		pw = (EditText)findViewById(R.id.password);
		pw.setTypeface(Typeface.DEFAULT);
		login = (Button)findViewById(R.id.login_button);
		pb = (ProgressBar)findViewById(R.id.login_progress);
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.i(TAG, "Login button pressed.");
				if( loginLock.tryLock() )
				{
					pb.setVisibility(View.VISIBLE); // show spinner
					Log.i(TAG, "Login lock acquired");
					loginTask task = new loginTask();
					task.execute(email.getText().toString(), pw.getText().toString());
					
					

					try {
						Log.i(TAG, "Login lock released");
						loginLock.unlock();
					} catch (Exception e){
						Log.i(TAG, "ERROR: " +e.toString());
					}
					/* NOTE: it hides so quickly that it never actually
					* appears, even on a 3G connection.  We protect against
					* ANR's, so this can remain to provide a user confirmation
					* that "the computer is thinking." */

				}
			}
		});
	}

		private class loginTask extends AsyncTask<String, Void, Integer>
		{
			protected Integer doInBackground(String... strings)
			{
				try {
					/* stackoverflow question 2999945 */
					HttpPost post = new HttpPost("http://connactiv.nfshost.com/test/index.php");
					post.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
					final DefaultHttpClient client = new DefaultHttpClient(post.getParams());
					client.setCookieStore(cs);

					List<NameValuePair> postParams = new ArrayList<NameValuePair>(3);
					postParams.add(new BasicNameValuePair("login", "1"));
					postParams.add(new BasicNameValuePair("username", strings[0]));
					postParams.add(new BasicNameValuePair("pass", strings[1]));

					post.setEntity( new UrlEncodedFormEntity(postParams) );

					BasicResponseHandler brh = new BasicResponseHandler();
					HttpResponse response = client.execute( post, httpCtx );
					final String resp = brh.handleResponse( response );

					runOnUiThread( new Runnable() {
						public void run() {
							pb.setVisibility(View.INVISIBLE); // hide spinner
							if( resp.contains("Incorrect password"))
								Toast.makeText(ctx, "Incorrect password.", Toast.LENGTH_SHORT).show();
							else if ( resp.startsWith("Sorry, that user does not exist in our database.") )
								Toast.makeText(ctx, "User does not exist.", Toast.LENGTH_SHORT).show();
							else if ( resp.startsWith("Oops.") )
								Toast.makeText(ctx, "All fields are required.", Toast.LENGTH_SHORT).show();
							else
							{
								CookieSyncManager.getInstance().sync();
								List<Cookie> cookieList = client.getCookieStore().getCookies();
								Log.i(TAG, "===== Cookie List =====");
								for( Cookie c : cookieList )
								Log.i(TAG, c.getName() );
								startActivity(new Intent(getApplicationContext(), parseXml.class));
								//startActivity(new Intent(this, parseXml.class));
								Toast.makeText(ctx, "Welcome to ConnActiv, " +email.getText().toString().split("@")[0] +"!", Toast.LENGTH_SHORT).show();
								finish();
							}
						}
						});

					} catch (Exception e) {
						Log.i(TAG, "ERROR: " +e.toString());
					}
					return 0;
				}
			}

		}
