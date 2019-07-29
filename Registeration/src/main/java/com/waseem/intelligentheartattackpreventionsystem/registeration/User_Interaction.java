package com.waseem.intelligentheartattackpreventionsystem.registeration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;


public class User_Interaction extends Activity {
	TextView tvinternet;
	HttpClient client;
	JSONObject json;

	final static String URL="http://www.google.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__interaction);
		final Button GetServerData = (Button) findViewById(R.id.GetServerData);

				GetServerData.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {

						// Server Request URL
						String serverURL = "http://www.google.com";

						// Create Object and call AsyncTask execute Method
						new LongOperation().execute(serverURL);

					}
				});

			}


			// Class with extends AsyncTask class
			private class LongOperation  extends AsyncTask<String, Void, Void> {

				private final HttpClient Client = new DefaultHttpClient();
				private String Content;
				private String Error = null;
				private ProgressDialog Dialog = new ProgressDialog(User_Interaction.this);

				TextView uiUpdate = (TextView) findViewById(R.id.tv_http);

				protected void onPreExecute() {
					// NOTE: You can call UI Element here.

					//UI Element
					uiUpdate.setText("Output : ");
					Dialog.setMessage("Downloading source..");
					Dialog.show();
				}

				// Call after onPreExecute method
				protected Void doInBackground(String... urls) {
					try {

						// Call long running operations here (perform background computation)
						// NOTE: Don't call UI Element here.

						// Server url call by GET method
						HttpGet httpget = new HttpGet(urls[0]);
						ResponseHandler<String> responseHandler = new BasicResponseHandler();
						Content = Client.execute(httpget, responseHandler);

					} catch (ClientProtocolException e) {
						Error = e.getMessage();
						cancel(true);
					} catch (IOException e) {
						Error = e.getMessage();
						cancel(true);
					}

					return null;
				}

				protected void onPostExecute(Void unused) {
					// NOTE: You can call UI Element here.

					// Close progress dialog
					Dialog.dismiss();

					if (Error != null) {

						uiUpdate.setText("Output : "+Error);

					} else {

						uiUpdate.setText("Output : "+Content);

					}
				}

			}
		}




	/*	if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		tvinternet=(TextView)findViewById(R.id.tv_http);
		client =new DefaultHttpClient();
		String test;
		try{
			test=new GetMethod().GetInternetData();
			tvinternet.setText(test);
		}catch(Exception e){
			Toast.makeText(User_Interaction.this,e.toString(),Toast.LENGTH_LONG).show();
		}
		//new Read().execute("text");
	}
	public JSONObject lasttweet(String username)throws ClientProtocolException,IOException,JSONException{
		StringBuilder url=new StringBuilder(URL);
		url.append(username);
		HttpGet get=new HttpGet((url.toString()));
		HttpResponse response=client.execute(get);
		int status=response.getStatusLine().getStatusCode();

		if(status==200){
			HttpEntity entity=response.getEntity();
			String data= EntityUtils.toString(entity);
			JSONArray timeline =new JSONArray(data);
			JSONObject last=timeline.getJSONObject(0);
			return last;

		}else{
			Toast.makeText(User_Interaction.this,"error",Toast.LENGTH_LONG).show();
			return null;
		}
	}
	public class Read extends AsyncTask<String,Integer,String>{

		@Override
		protected String doInBackground(String... params) {
			try {
				json=lasttweet("google ");
				return json.getString(params[0]);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			tvinternet.setText(s);

		}
	}*/
