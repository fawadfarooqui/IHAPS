package com.waseem.intelligentheartattackpreventionsystem.registeration;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;


public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
	TextToSpeech tts;
	Button signup, login;
	Boolean flag = true;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		setContentView(R.layout.activity_main);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		Toast.makeText(this, mngr.getDeviceId() + "\n" + mngr.getSimOperatorName() + " \n", Toast.LENGTH_LONG).show();
		//if(mngr.getDeviceId().equals(mngr.getDeviceId())) {
		//	Intent Registration = new Intent("android.intent.action.Login");
		//	startActivity(Registration);
		//}
		tts = new TextToSpeech(this, this);
		signup = (Button) findViewById(R.id.button);
		login = (Button) findViewById(R.id.button2);
		signup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				flag = false;
				Thread t = new Thread() {
					public void run() {
						speeking(signup.getText().toString());
						Intent Registration = new Intent("android.intent.action.STARTINGPOINT");
						startActivity(Registration);
					}
				};
				t.start();
			}
		});
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				flag = false;
				Thread t = new Thread() {
					public void run() {
						speeking(login.getText().toString());
						Intent Registration = new Intent("android.intent.action.Login");
						startActivity(Registration);
					}
				};
				t.start();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		flag=true;
//		speeking("Welcome to Intelligent heart attack prevention system");
		Log.d("OnStart", "On start Activity has been called");

	}
		@Override
		public void onInit(int i) {
			if (i == TextToSpeech.SUCCESS) {

				int result = tts.setLanguage(Locale.getDefault());
				tts.setPitch(1.1f);
				//tts.setSpeechRate(1.1f);

				if (result == TextToSpeech.LANG_MISSING_DATA
						|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
					Log.e("TTS", "This Language is not supported");
				} else {
					Log.d("problem", "not speeking");
				}

			} else {
				Log.e("TTS", "Initilization Failed!");
			}
		}


	public void speeking(String message) {
		if (flag == false) {
			tts.speak(message + " Selected", TextToSpeech.QUEUE_FLUSH, null);
		} else
			tts.speak(message , TextToSpeech.QUEUE_FLUSH, null);
	}


	@Override
	public void onDestroy() {
		// Don't forget to shutdown tts!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}



}