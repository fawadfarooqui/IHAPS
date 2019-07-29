package com.waseem.intelligentheartattackpreventionsystem.registeration;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class HttpMethodsCall extends Activity {
	TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_methods_call);
		tv=(TextView)findViewById(R.id.textfor);
		GetMethod test=new GetMethod();
		String returned;
		try {
			returned=test.GetInternetData();
			tv.setText(returned);
		} catch (Exception e) {
			Toast.makeText(HttpMethodsCall.this,e.getMessage(),Toast.LENGTH_LONG).show();
		}
    }


}
