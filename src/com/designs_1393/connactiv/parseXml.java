package com.designs_1393.connactiv;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

//HTTP Stuff
import android.widget.ProgressBar;
import java.net.HttpURLConnection;
import java.net.URL;

//XML Stuff
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.database.Cursor;
import android.content.Intent;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class parseXml extends Activity
{
    private Element input;
    private NodeList message;

    ProgressBar pb;
    TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream);
        pb = (ProgressBar)findViewById(R.id.login_progress);
        tv = new TextView(this);

        try{
            URL url = new URL("http://connactiv.nfshost.com/test/android/genXML.php"); //This section isn't working
            //genXML.php isn't being called. Perhaps using HTTP POST will work?
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            pb.setVisibility(View.VISIBLE);
            URL xurl = new URL("http://connactiv.nfshost.com/test/android/database.xml");
        
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
            Document doc = builder.parse(new InputSource(xurl.openStream()));
            input = doc.getDocumentElement();
            message = input.getElementsByTagName("MESSAGE");

        }catch(Exception e){}
        
        String output = "Empty";

        // if (message.item(0).getFirstChild().getNodeValue() != "") {
        //     output = message.item(0).getFirstChild().getNodeValue();
        // }

        //For whatever reason, message is being null right now.

        tv.setText("XML: \n");
    }
}
