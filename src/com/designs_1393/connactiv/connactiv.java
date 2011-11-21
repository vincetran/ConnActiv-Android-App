package com.designs_1393.connactiv;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListActivity;

// actionbar
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class connactiv extends ListActivity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream);

		// ----- actionbar -----
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("ConnActiv Stream");
    }
}
