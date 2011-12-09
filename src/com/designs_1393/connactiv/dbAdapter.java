package com.designs_1393.connactiv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import android.util.Log;

public class dbAdapter
{

	public static final String KEY_CID = "connaction_id";
	public static final String KEY_POST_TIME = "post_time";
	public static final String KEY_UID = "user_id";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_START_DATE = "start_date";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_END_DATE = "end_date";
	public static final String KEY_UNID = "unique_network_id";
	public static final String KEY_LEVELS = "levels";
	public static final String KEY_PRIVATE = "is_private";

	private static final String TAG = "ConnActiv: dbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_CREATE =
		"create table connaction (_id integer primary key autoincrement, connaction_id text not null, post_time text not null,"
		+ " user_id text not null, location text not null, start_date text not null, "
		+ " message text not null,  end_date text not null,  unique_network_id text not null, "
		+ " levels text not null, is_private text not null);";

	private static final String DATABASE_NAME = "connactiv";
	private static final String CONNACTION_TABLE = "connaction";
	private static final int DATABASE_VERSION = 1;
	//Version 1.0 was Database_version 2
	//Version 1.1 is Database_version 3
	//Version 1.2 is Database_version 4
	private final Context mCtx;

	/**
	 * Class constructor.  Retains calling application's context, so that it
	 * can be used in additional functions.
	 *
	 * @param ctx	Calling application's context
	 */
	public dbAdapter(Context ctx)
	{
		this.mCtx = ctx;

	}

	/**
	 * Inner class providing a database upgrade process.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{

		}
	}

	/**
	 * Opens the database helper for writing and returns the database adapter.
	 *
	 * @return	Database adapter associated with the database.
	 */
	public dbAdapter open() throws SQLException
	{
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Closes the database helper.
	 */
	public void close()
	{
		mDbHelper.close();
	}

	public long createConnaction(String... params)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CID, params[0]);
		initialValues.put(KEY_POST_TIME, params[1]);
		initialValues.put(KEY_UID, params[2]);
		initialValues.put(KEY_LOCATION, params[3]);
		initialValues.put(KEY_START_DATE, params[4]);
		initialValues.put(KEY_MESSAGE, params[5]);
		initialValues.put(KEY_END_DATE, params[6]);
		initialValues.put(KEY_UNID, params[7]);
		initialValues.put(KEY_LEVELS, params[8]);
		initialValues.put(KEY_PRIVATE, params[9]);

		Log.i("ConnActiv", "IN DB LEVELS:::::: "+ params[8]);

		if (mDb.query(CONNACTION_TABLE, new String[] {KEY_CID}, KEY_CID +"=?", new String[] {params[0]}, null, null, KEY_CID).getCount() != 0)
			return -1;
		else
			return mDb.insert(CONNACTION_TABLE, null, initialValues);
	}

	public Cursor fetchAllConnactions()
	{
		return mDb.query(CONNACTION_TABLE, new String[] {KEY_POST_TIME, KEY_UID,
			KEY_LOCATION, KEY_START_DATE, KEY_MESSAGE, KEY_END_DATE, KEY_UNID, KEY_LEVELS, "_id"}, null, null, null, null, KEY_CID + " DESC");
	}

}
