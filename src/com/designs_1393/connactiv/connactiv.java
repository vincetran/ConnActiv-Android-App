package com.designs_1393.connactiv;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;

public class connactiv extends Activity
{
	EditText un, pw;
	Button login;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

		un = (EditText)findViewById(R.id.email);
		pw = (EditText)findViewById(R.id.password);
		login = (Button)findViewById(R.id.login_button);
    }
}
