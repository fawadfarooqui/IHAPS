package com.waseem.intelligentheartattackpreventionsystem.registeration;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;


public class Dummy_activity extends AppCompatActivity {

	EditText signal,status;
	Button submit;

	DataBaseClass db=new DataBaseClass(Dummy_activity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_activity);
		try {
			db.open();
		} catch (SQLException e) {
			Toast.makeText(Dummy_activity.this,e.getMessage(),Toast.LENGTH_LONG);
		}

		signal=(EditText)findViewById(R.id.sig);
		status=(EditText)findViewById(R.id.stat);
		submit=(Button)findViewById(R.id.signal_submit);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

					db.Signal_Learning(signal.getText().toString(), status.getText().toString());
					Data_validation("djdfkjsdsasd::"+signal.getText().toString());

				//	Cursor c = db.GetAllData();
				/*	if (c.moveToFirst())
					{
						do {
							DisplayContact(c);
						} while (c.moveToNext());
					}*/
					Toast.makeText(Dummy_activity.this,db.Get_Status("68,63,87,55,74,9,4,5,9,8"),Toast.LENGTH_LONG).show();
					if(db.Get_Status("68,63,87,55,74,9,4,5,9,8").equalsIgnoreCase("sleeping")){

					}



			}
		});
    }

	@Override
	public void onBackPressed() {
		onPause();
	}

	@Override
	protected void onPause() {
		db.Close();
		super.onPause();
	}

	void Data_validation(String message) {
		String signal_message = "";
		String compare="";
		Toast.makeText(Dummy_activity.this, "here is it " + message, Toast.LENGTH_LONG).show();
		try {
			for (int i = 0; i < message.length(); i++) {
				if ((message.charAt(i) == '0') || (message.charAt(i) == '1') || (message.charAt(i) == '2')
						|| (message.charAt(i) == '3') || (message.charAt(i) == '4') || (message.charAt(i) == '5')
						|| (message.charAt(i) == '6') || (message.charAt(i) == '7') || (message.charAt(i) == '8')
						|| (message.charAt(i) == '9') || (message.charAt(i) == ','))
					signal_message = signal_message + message.charAt(i);

			}
		} catch (Exception e) {
			Toast.makeText(Dummy_activity.this, "here is it " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		for(int i=0;i<signal_message.length();i++){
			if(signal_message.charAt(i)!=','){
				compare=compare+ signal_message.charAt(i);
			}
		}
		try {
			String status = db.Get_Status(signal_message);
			if (status.equalsIgnoreCase("Sleeping"))
				Toast.makeText(Dummy_activity.this, "sendtodatabase();", Toast.LENGTH_LONG).show();
			else if (status.equalsIgnoreCase("Normal"))

				Toast.makeText(Dummy_activity.this, "sendtodatabase();", Toast.LENGTH_LONG).show();
			else if (status.equalsIgnoreCase("Running"))

				Toast.makeText(Dummy_activity.this, "sendtodatabase();", Toast.LENGTH_LONG).show();
			else {
				sendMessage();
				//speeking("Please please please Leave this Activity you are moving to the heart attack please leave this function");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void sendMessage() {
		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(db.get_belonging_no(), null, "Dear " + ", " +
					" Please Help your patient you patient is moving towards the heart attack !!! ", null, null);
			Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
			smsManager.sendTextMessage(db.get_doctor_no(), null, "Respected Doctor " + ", " +
					" Please contact your patient or visit the website your patient is moving towards the heart attack !!! ", null, null);
			Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"SMS failed, please try again later!",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	public void DisplayContact(Cursor c)
	{
		Toast.makeText(this,
				"id: " + c.getString(0) + "\n" +
						"Name: " + c.getString(1) ,
				Toast.LENGTH_LONG).show();
	}
}
