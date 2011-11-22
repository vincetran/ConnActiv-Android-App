package com.designs_1393.connactiv;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

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

import android.content.Intent;
import android.content.Context;

import android.widget.Toast;

// threading
import android.os.AsyncTask;
import java.lang.Void;

// DBG
import android.util.Log;



public class connactiv extends Activity
{
	EditText email, pw;
	Button login;

	final String TAG = "ConnActiv: connactiv";
	Context ctx;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
		ctx = getApplicationContext();


		email = (EditText)findViewById(R.id.email);
		pw = (EditText)findViewById(R.id.password);
		login = (Button)findViewById(R.id.login_button);
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.i(TAG, "Login button pressed.");

				new loginTask().execute(email.getText().toString(), pw.getText().toString());
			}
		});
    }

	private class loginTask extends AsyncTask<String, Void, Long>
	{
		protected Long doInBackground(String... strings)
		{
			try {
				/* stackoverflow question 2999945 */
				HttpPost post = new HttpPost("http://1393designs.com/ConnActiv/views/welcome.php");
				post.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
				DefaultHttpClient client = new DefaultHttpClient(post.getParams());

				List<NameValuePair> postParams = new ArrayList<NameValuePair>(3);
				postParams.add(new BasicNameValuePair("login", "1"));
				postParams.add(new BasicNameValuePair("username", strings[0]));
				postParams.add(new BasicNameValuePair("pass", strings[1]));

				post.setEntity( new UrlEncodedFormEntity(postParams) );

				BasicResponseHandler brh = new BasicResponseHandler();
				HttpResponse response = client.execute( post );
				String resp = brh.handleResponse( response );

				if( resp.startsWith("1") )
					Toast.makeText(ctx, "Incorrect password.", Toast.LENGTH_SHORT).show();
				else if ( resp.startsWith("2") )
					Toast.makeText(ctx, "User does not exist.", Toast.LENGTH_SHORT).show();
				else if ( resp.startsWith("3") )
					Toast.makeText(ctx, "All fields are required.", Toast.LENGTH_SHORT).show();
				else if (resp.startsWith("0"))
				{
					startActivity(new Intent(getApplicationContext(), stream.class));
					Toast.makeText(ctx, "Welcome to ConnActiv, " +email.getText().toString().split("@")[0] +"!", Toast.LENGTH_SHORT).show();
					finish();
				}
			} catch (Exception e) {
				Log.i(TAG, "ERROR: " +e.toString());
			}
			return (long)0;
		}
	}

}
