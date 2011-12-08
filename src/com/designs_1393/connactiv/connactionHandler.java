package com.designs_1393.connactiv;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.util.Log;
import android.content.Context;

public class connactionHandler extends DefaultHandler{

	// ===========================================================
	// Fields
	// ===========================================================
	private boolean in_connaction = false;
	private boolean in_post_time = false;
	private boolean in_user_id = false;
	private boolean in_location = false;
	private boolean in_start_time = false;
	private boolean in_message = false;
	private boolean in_end_time = false;
	private boolean in_unique = false;
	private boolean in_private = false;

	private String cid = "";
	private String post_time = "";
	private String uid = "";
	private String location = "";
	private String start_time = "";
	private String message = "";
	private String end_time = "";
	private String unique = "";
	private String is_private = "";

	private dbAdapter mDbHelper;
	private Context ctx;

	private parsedDataSet myparsedDataSet = new parsedDataSet();

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public parsedDataSet getParsedData() {
		return this.myparsedDataSet;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	@Override
	public void startDocument() throws SAXException {
		this.myparsedDataSet = new parsedDataSet();
	}

	@Override
	public void endDocument() throws SAXException {
		// Nothing to do
	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if (localName.equals("CONNACTION_ID")) {
			this.in_connaction = true;
		}else if (localName.equals("POST_TIME")) {
			this.in_post_time = true;
		}else if (localName.equals("USER_ID")) {
			this.in_user_id = true;
		}else if (localName.equals("LOCATION")) {
			this.in_location = true;
		}else if (localName.equals("START_TIME")) {
			this.in_start_time = true;
		}else if (localName.equals("MESSAGE")) {
			this.in_message = true;
		}else if (localName.equals("END_TIME")) {
			this.in_end_time = true;
		}else if (localName.equals("UNIQUE_NETWORK_ID")) {
			this.in_unique = true;
		}else if (localName.equals("IS_PRIVATE")) {
			this.in_private = true;
		}
	}
	
	/** Gets be called on closing tags like: 
	 * </tag> */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("CONNACTION_ID")) {
			this.in_connaction = false;
		}else if (localName.equals("POST_TIME")) {
			this.in_post_time = false;
		}else if (localName.equals("USER_ID")) {
			this.in_user_id = false;
		}else if (localName.equals("LOCATION")) {
			this.in_location = false;
		}else if (localName.equals("START_TIME")) {
			this.in_start_time = false;
		}else if (localName.equals("MESSAGE")) {
			this.in_message = false;
		}else if (localName.equals("END_TIME")) {
			this.in_end_time = false;
		}else if (localName.equals("UNIQUE_NETWORK_ID")) {
			this.in_unique = false;
		}else if (localName.equals("IS_PRIVATE")) {
			this.in_private = false;
		}
	}
	
	/** Gets be called on the following structure: 
	 * <tag>characters</tag> */
	@Override
	public void characters(char ch[], int start, int length) {
		if(this.in_connaction){
			myparsedDataSet.setExtString(new String(ch, start, length));
			cid = myparsedDataSet.toString();
		}
		else if(this.in_post_time){
			myparsedDataSet.setExtString(new String(ch, start, length));
			post_time = myparsedDataSet.toString();
		}
		else if(this.in_user_id){
			myparsedDataSet.setExtString(new String(ch, start, length));
			uid = myparsedDataSet.toString();
		}
		else if(this.in_location){
			myparsedDataSet.setExtString(new String(ch, start, length));
			location = myparsedDataSet.toString();
		}
		else if(this.in_start_time){
			myparsedDataSet.setExtString(new String(ch, start, length));
			start_time = myparsedDataSet.toString();
		}
		else if(this.in_message){
			myparsedDataSet.setExtString(new String(ch, start, length));
			message = myparsedDataSet.toString();
		}
		else if(this.in_end_time){
			myparsedDataSet.setExtString(new String(ch, start, length));
			end_time = myparsedDataSet.toString();
		}
		else if(this.in_unique){
			myparsedDataSet.setExtString(new String(ch, start, length));
			unique = myparsedDataSet.toString();
		}
		else if(this.in_private){
			mDbHelper = new dbAdapter(ctx);
			mDbHelper.open();

			myparsedDataSet.setExtString(new String(ch, start, length));
			is_private = myparsedDataSet.toString();

			mDbHelper.createConnaction(cid, post_time, uid, location, start_time, message, 
				end_time, unique, is_private);

			mDbHelper.close();
		}
	}

	public void setContext(Context ctx){
		this.ctx = ctx;
	}
}
