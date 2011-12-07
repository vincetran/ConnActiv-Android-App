package com.designatum_1393.textspansion;

import android.database.Cursor;
import android.widget.TextView;
import android.content.Context;
import android.view.ViewGroup;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;

public class privitized_adapter extends CursorAdapter
{
	public static final String KEY_CID = "connaction_id";
	public static final String KEY_POST_TIME = "post_time";
	public static final String KEY_UID = "user_id";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_START_DATE = "start_date";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_END_DATE = "end_date";
	public static final String KEY_UNID = "unique_network_id";
	public static final String KEY_PRIVATE = "is_private";

	public privitized_adapter(Context context, Cursor c)
	{
		super(context, c);
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup viewGroup)
	{
		return LayoutInflater.from(context).inflate(layout, viewGroup, false);
	}

	@Override
	public void bindView(View v, Context context, Cursor c)
	{
		TextView vTime = (TextView) v.findViewById( R.id.time );
		TextView vUser = (TextView) v.findViewById( R.id.userID );
		TextView vLoc  = (TextView) v.findViewById( R.id.location );
		TextView vStart= (TextView) v.findViewById( R.id.startDate );
		TextView vEnd  = (TextView) v.findViewById( R.id.endDate );
		TextView vMess = (TextView) v.findViewById( R.id.Message );
		TextView vNet  = (TextView) v.findViewById( R.id.networkID );

		vTime.setText(  c.getString( c.getColumnIndexOrThrow( KEY_POST_TIME ) ) );
		vUser.setText(  c.getString( c.getColumnIndexOrThrow( KEY_UID ) ) );
		vLoc.setText(   c.getString( c.getColumnIndexOrThrow( KEY_LOCATION ) ) );
		vStart.setText( c.getString( c.getColumnIndexOrThrow( KEY_START_DATE ) ) );
		vEnd.setText(   c.getString( c.getColumnIndexOrThrow( KEY_END_DATE ) ) );
		vMess.setText(  c.getString( c.getColumnIndexOrThrow( KEY_MESSAGE ) ) );
		vNet.setText(   c.getString( c.getColumnIndexOrThrow( KEY_UNID ) ) );
	}
}