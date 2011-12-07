package com.designs_1393.connactiv;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.database.Cursor;


public class stream extends ListActivity
{
	dbAdapter dbHelper;
	Cursor connCursor;

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
}
