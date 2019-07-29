package com.waseem.intelligentheartattackpreventionsystem.registeration;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.UUID;


public class Login extends Activity implements TextToSpeech.OnInitListener {
	/**
	 * Default Serial-Port UUID
	 */
	private String defaultUUID = "00001101-0000-1000-8000-00805F9B34FB";

	/**
	 * Default bluetooth adapter on the device.
	 */
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	/**
	 * String used to identify this application in the log.
	 */
	private final String TAG = MainActivity.class.getName();

	/**
	 * The prefix to identify devices of interest.
	 */
	private final static String PREFIX = "BT_";

	/**
	 * The Server thread.
	 */
	private AcceptThread server;

	/**
	 * Magic number used in the bluetooth enabling request.
	 */
	private final int REQ = 111;

	private NotificationCenter mNotificationCenter;

	private static final String MESSAGE_RECEIVED_INTENT = "com.waseem.intent.MESSAGE_RECEIVED";
	EditText mob_number, password;
	Button sign;
	Boolean flag = false;
	String signal_message = "";
	TextToSpeech tts;
	final Handler myHandler=new Handler();
	final DataBaseClass db_class = new DataBaseClass(Login.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		try {
			db_class.open();
		} catch (SQLException e) {
			Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_LONG);
		}
		mob_number = (EditText) findViewById(R.id.mob_num);
		password = (EditText) findViewById(R.id.pass);
		tts = new TextToSpeech(this, this);
		mNotificationCenter = new NotificationCenter();

		if (mBluetoothAdapter == null) {
			Log.e(TAG, "No Bluetooth Adapter available. Exiting...");
			this.finish();
		}
		this.registerReceiver(mNotificationCenter, new IntentFilter(MESSAGE_RECEIVED_INTENT));

		sign = (Button) findViewById((R.id.Sign_in_btn));
		sign.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final TextView tv = new TextView(Login.this);
				final Dialog dil = new Dialog(Login.this);
				dil.setTitle("Problem in getting");
				final Runnable myUpdateResults = new Runnable() {
					@Override
					public void run() {

						if ((mob_number.getText().toString() != null) && (password.getText().toString() != null)) {
							try {
								if (db_class.validate(mob_number.getText().toString(), password.getText().toString())) {
									//	Intent getdata=new Intent("android.intent.action.DummyActivity");
									//	startActivity(getdata);
									if (!(mBluetoothAdapter.getName().startsWith(PREFIX)))
										mBluetoothAdapter.setName(PREFIX + mBluetoothAdapter.getName());

									if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
										requestBTDiscoverable();
									server = new AcceptThread();
									server.start();
									flag = true;
									sign.setEnabled(false);

								} else {
									Toast.makeText(Login.this, "Could not start activity", Toast.LENGTH_LONG).show();
									server.cancel();
									restoreBTDeviceName();

								}

							} catch (Exception e) {
								tv.setText("Sorry Conflicting with the database !!!" + e.getMessage());
								dil.setContentView(tv);
								dil.show();
							}
						} else {
							Toast.makeText(Login.this, "Please fill the fields first ", Toast.LENGTH_LONG).show();
						}
					}
				};
				new Thread(){
					public void run(){
						myHandler.post(myUpdateResults);
					}
				}.start();


			}

		});
	}

	void Data_validation(String message) {
		String signal_message = "";
		//Toast.makeText(Login.this, "here is it " + message, Toast.LENGTH_LONG).show();
		try {
			for (int i = 0; i < message.length(); i++) {
				if ((message.charAt(i) == '0') || (message.charAt(i) == '1') || (message.charAt(i) == '2')
						|| (message.charAt(i) == '3') || (message.charAt(i) == '4') || (message.charAt(i) == '5')
						|| (message.charAt(i) == '6') || (message.charAt(i) == '7') || (message.charAt(i) == '8')
						|| (message.charAt(i) == '9') || (message.charAt(i) == ','))
					signal_message = signal_message + message.charAt(i);


			}
			//Toast.makeText(Login.this, "your validated data is " + signal_message, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(Login.this, "here is it " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		try {
			String status = db_class.Get_Status(signal_message);
			if(status.equals("")){
				speeking("Please please please Leave this Activity you are moving to the heart attack please leave this function");
				sendMessage();

			}
			else {
				if (status.equalsIgnoreCase("Sleeping"))
					Toast.makeText(Login.this, "You are Sleeping", Toast.LENGTH_LONG).show();
				else if (status.equalsIgnoreCase("Normal"))

					Toast.makeText(Login.this, "You are in Normal State", Toast.LENGTH_LONG).show();
				else if (status.equalsIgnoreCase("Running"))

					Toast.makeText(Login.this, "You are Running ", Toast.LENGTH_LONG).show();
				else {
					sendMessage();
					speeking("Please please please Leave this Activity you are moving to the heart attack please leave this function");
				}
			}
		} catch (Exception e) {
			Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_LONG).show();
		}
	}
	void sendMessage() {
		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(db_class.get_belonging_no(), null, "Dear " + ", " +
					" Please Help your patient you patient is moving towards the heart attack !!! ", null, null);
			Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
			smsManager.sendTextMessage(db_class.get_doctor_no(), null, "Respected Doctor " + ", " +
					" Please contact your patient or visit the website your patient is moving towards the heart attack !!! ", null, null);
			Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"SMS failed, please try again later!",
					Toast.LENGTH_LONG).show();
		}
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
				tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
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

			@Override
			public void onBackPressed() {
				if (flag)
					onPause();
				super.onBackPressed();
			}

			@Override
			public void onPause() {
				//this.unregisterReceiver(mNotificationCenter);
				server.cancel();
				db_class.Close();
				restoreBTDeviceName();
				super.onPause();
			}

			/**
			 * Launches Discoverable Bluetooth Intent.
			 */
			public void requestBTDiscoverable() {
				Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 900);

				startActivityForResult(i, REQ);

				int result = 0;

				this.onActivityResult(REQ, result, i);
				Log.i(TAG, "Bluetooth discoverability enabled");
			}

			/**
			 * Obtains the Vibrator service.
			 *
			 * @return Vibrator Object.
			 */
			private Vibrator getVibrator() {
				return (Vibrator) getSystemService(VIBRATOR_SERVICE);
			}

			private String getData() {
				if (!signal_message.equals(""))
					return signal_message;
				else
					return null;
			}

			/**
			 * Removes the prefix from the device name if the prefix is present.
			 */
			private void restoreBTDeviceName() {
				if (mBluetoothAdapter.getName().startsWith(PREFIX))
					mBluetoothAdapter.setName(mBluetoothAdapter.getName().substring(PREFIX.length()));
			}


			/**
			 * Shows a information dialog.
			 *
			 * @param message  String resource used to define the message.
			 * @param duration Dialog's TTL.
			 */
			private void showInformation(String message, long duration) {
				final Dialog mDialog = new Dialog(this);

				TextView txt = new TextView(this);
				txt.setText(message);
				mDialog.setContentView(txt);
				mDialog.setTitle("Information");
				mDialog.show();
				(new Handler()).postDelayed(new Runnable() {
					public void run() {
						mDialog.dismiss();
					}
				}, duration); // Close dialog after dela
				//Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
				Data_validation(message);


			}


			/**
			 * Thread that handles an incoming connection.
			 * Adapted from http://developer.android.com/guide/topics/wireless/bluetooth.html
			 */
			class AcceptThread extends Thread {
				/**
				 * Tag that will appear in the log.
				 */
				private final String ACCEPT_TAG = AcceptThread.class.getName();

				/**
				 * The bluetooth server socket.
				 */
				private final BluetoothServerSocket mServerSocket;

				public AcceptThread() {
					BluetoothServerSocket tmp = null;
					try {
						tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(ACCEPT_TAG, UUID.fromString(defaultUUID));
					} catch (IOException e) {
						e.printStackTrace();
					}
					mServerSocket = tmp;
				}

				public void run() {
					BluetoothSocket socket = null;
					while (true) {
						try {
							Log.i(ACCEPT_TAG, "Listening for a connection...");

							socket = mServerSocket.accept();
							Log.i(ACCEPT_TAG, "Connected to " + socket.getRemoteDevice().getName());

						} catch (IOException e) {
							break;
						}
						// If a connection was accepted
						if (socket != null) {
							// Do work to manage the connection (in a separate thread)
							try {
								// Read the incoming string.
								String buffer;

								DataInputStream in = new DataInputStream(socket.getInputStream());

								buffer = in.readUTF();

								Intent i = new Intent(MESSAGE_RECEIVED_INTENT);
								i.putExtra("Message", String.format("%sn From: %s", buffer, socket.getRemoteDevice().getName()));

								getBaseContext().sendBroadcast(i);
							} catch (IOException e) {
								Log.e(ACCEPT_TAG, "Error obtaining InputStream from socket");
								e.printStackTrace();
							}
							try {
								mServerSocket.close();
							} catch (IOException e) {
							}
							break;
						}
					}
				}

				/**
				 * Will cancel the listening socket, and cause the thread to finish
				 */
				public void cancel() {
					try {
						mServerSocket.close();
					} catch (IOException e) {
						Toast.makeText(Login.this, "Server is cancelling at end", Toast.LENGTH_LONG).show();
					}
				}
			}

			class NotificationCenter extends BroadcastReceiver {

				@Override
				public void onReceive(Context context, Intent intent) {
					if (intent.getAction().equals(MESSAGE_RECEIVED_INTENT)) {
						showInformation(intent.getExtras().getString("Message"), 5000);
						getVibrator().vibrate(500);
					}
				}

			}
	/*public void DisplayContact(Cursor c)
	{
		Toast.makeText(this,
				"id: " + c.getString(0) + "\n" +
						"Name: " + c.getString(1) + "\n" +
						"Name: " + c.getString(2) + "\n" +
						"Name: " + c.getString(3) + "\n" +
						"Name: " + c.getString(4) + "\n" +
						"Name: " + c.getString(5) + "\n" +
						"Email:  " + c.getString(6),
				Toast.LENGTH_LONG).show();
	}*/
		}

