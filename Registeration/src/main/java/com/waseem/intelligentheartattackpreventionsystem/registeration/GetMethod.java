package com.waseem.intelligentheartattackpreventionsystem.registeration;

import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by Waseem ud din on 5/31/2014.
 */
public class GetMethod {

	public String GetInternetData() throws Exception{
		BufferedReader in=null;
		String data=null;
		try{
			/**
			 * The Application and the web communication
			 */
			DefaultHttpClient client=new DefaultHttpClient();
			/**
			 * Website from where we will fetch the data
			 */
			URI website=new URI("http://www.google.com");
			/**
			 * Http method to get the data using Http protocols
			 */
			HttpGet request=new HttpGet();
			/**
			 * We will get the set method with the set uri
			 */
			request.setURI(website);
			/**
			 * Setting the network for getting the data from the website in the form of response
			 */
			HttpResponse response=client.execute(request);
			/**
			 * To make the data readable we neeed to convert it in to a string that is why we have created a Buffer Object "in"
			 * the inputstream will convert the data which is in the form response from the website
			 * we are getting the content
			 */
			in=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer buffer=new StringBuffer(" ");
			String line="";
			/**
			 * Creating new line to move  the data on the new line
			 */
			String nline=System.getProperty("line.separator");
			/**
			 * To read all the data we use While loop
			 */
			while((line=in.readLine())!=null){
				buffer.append(line+nline);

			}
			in.close();
			data=buffer.toString();
			return data;

		}finally {
			if(in!=null){
				try{
					in.close();
					return data;
				}catch (Exception e){
					Log.e("Problem in close",e.toString());
				}

			}
		}
	}
}
