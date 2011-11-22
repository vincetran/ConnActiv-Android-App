package com.designs_1393.connactiv;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

// authentication
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.lang.String;

// DBG
import android.util.Log;

public class connactiv extends Activity
{
	EditText email, pw;
	Button login;

	final String TAG = "ConnActiv: connactiv";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

		email = (EditText)findViewById(R.id.email);
		pw = (EditText)findViewById(R.id.password);
		login = (Button)findViewById(R.id.login_button);
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.i(TAG, "Login button pressed.");

				/* create array list to hold HTTP post parameters */
				ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
				postParams.add(new BasicNameValuePair("username", email.getText().toString()));
				postParams.add(new BasicNameValuePair("pass", pw.getText().toString()));
				postParams.add(new BasicNameValuePair("#login", "true"));

				String response = null;
				try {
					response = CustomHttpClient.executeHttpPost("http://www.1393designs.com/ConnActiv/views/config.php", postParams);
					response = response.trim();
					response = response.replaceAll("\\s+","");
					if( response.equals("") )
						Log.i(TAG, "HTTP response: EMPTYYYY");
					else
						Log.i(TAG, "HTTP response: " +response.toString());
				} catch (Exception e) {
					Log.i(TAG, "ERROR: " +e.toString());
				}
			}
		});
    }
}
