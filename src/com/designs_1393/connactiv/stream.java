package com.designs_1393.connactiv;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.database.Cursor;
import android.content.Intent;
import android.content.Context;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;

public class stream extends ListActivity
{
	private dbAdapter dbHelper;
	private Cursor connCursor;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stream);

		// create database
		dbHelper = new dbAdapter(this);

		// open database
		dbHelper.open();
		connCursor = dbHelper.fetchAllConnactions();

		fillData();
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
	/**
	 * Updates the contents of the ListView.  Technically, requerys the
	 * database, uses that cursor in a new {@link privitized_adapter}, and then
	 * sets the List Adapter to said adapter.
	 */
	private void fillData()
	{
		//Will get the information from the subs database and display it onto the main View
		connCursor = dbHelper.fetchAllConnactions();
		startManagingCursor(connCursor);

		streamAdapter sAdapter = new streamAdapter(getApplicationContext(), connCursor);
		setListAdapter(sAdapter);
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
				startActivity(new Intent(getApplicationContext(), parseXml.class));
				finish();
				fillData();
				return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
