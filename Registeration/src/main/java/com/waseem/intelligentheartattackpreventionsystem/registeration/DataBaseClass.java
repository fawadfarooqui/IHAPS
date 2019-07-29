package com.waseem.intelligentheartattackpreventionsystem.registeration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.sql.SQLException;

/**
 * Created by Waseem ud din on 5/12/2014.
 */
public class DataBaseClass {
	public static final String ROW_ID ="_id";
	public static final String PATIENT_NAME ="patient_name";
	public static final String PATIENT_PHONE_NO="patient_phone_no";
	public static final String RELATED_DOCTOR ="doctor_name";
	public static final String DOCTOR_PHONE_NO="doctor_phone_no";
	public static final String RELATED_PERSON_NAME ="related_person_name";
	public static final String RELATED_PERSON_PHONE_NO ="related_person_phone_no";
	public static final String PATIENT_PASSWORD ="patient_password";

	public static final String PATIENT_SIGNAL ="patient_signal";
	public static final String SIGNAL_STATUS ="signal_status";

	private static final String DATABASE_NAME="patient_db";
	private static final String DATABASE_TABLE="patient_information";
	private static final String DATABASE_SIGNAL_TABLE="patient_signal";
	private static final int DATABASE_VERSION=1;

	private DbHelper our_Helper;
	private final Context our_Context;
	private SQLiteDatabase our_Database;

	private static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {

			super(context, DATABASE_NAME, null,DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE "+DATABASE_TABLE+" ("+
							ROW_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
							PATIENT_NAME +" TEXT NOT NULL,"+
							PATIENT_PHONE_NO +" TEXT NOT NULL,"+
							RELATED_DOCTOR +" TEXT NOT NULL,"+
							DOCTOR_PHONE_NO +" TEXT NOT NULL,"+
							RELATED_PERSON_NAME +" TEXT NOT NULL," +
							RELATED_PERSON_PHONE_NO +" TEXT NOT NULL,"+
							PATIENT_PASSWORD +" INTEGER NOT NULL);"

			);
			db.execSQL("CREATE TABLE "+DATABASE_SIGNAL_TABLE+" ("+
							PATIENT_SIGNAL +" TEXT,"+
							SIGNAL_STATUS +" TEXT);"

			);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
			onCreate(db);
		}
	}
	public DataBaseClass(Context c){
		our_Context=c;
	}

	public DataBaseClass open() throws SQLException{
		our_Helper=new DbHelper(our_Context);
		our_Database=our_Helper.getWritableDatabase();
		return this;
	}
	public long Create_Entry(String patient_name,String patient_number,String doctor_name,String doctor_number,String belong_name,String belong_number,String password){
		ContentValues cv=new ContentValues();
		cv.put(PATIENT_NAME,patient_name);
		cv.put(PATIENT_PHONE_NO,patient_number);
		cv.put(RELATED_DOCTOR,doctor_name);
		cv.put(DOCTOR_PHONE_NO,doctor_number);
		cv.put(RELATED_PERSON_NAME,belong_name);
		cv.put(RELATED_PERSON_PHONE_NO,belong_number);
		cv.put(PATIENT_PASSWORD,password);
		return our_Database.insert(DATABASE_TABLE,null,cv);
	}
	public long Signal_Learning(String signal,String status){
		ContentValues cv=new ContentValues();
		cv.put(PATIENT_SIGNAL,signal);
		cv.put(SIGNAL_STATUS,status);
		return our_Database.insert(DATABASE_SIGNAL_TABLE,null,cv);
	}
	public String Get_Status(String signal){
		String column[]={PATIENT_SIGNAL,SIGNAL_STATUS};
		String status="";
		Cursor mCursor =
				our_Database.query(true, DATABASE_SIGNAL_TABLE, column ,null , null,
						null, null, null, null);
		if (mCursor.moveToFirst())
		{
			do {
				if(mCursor.getString(0).equals(signal))
					status=mCursor.getString(1);
			} while (mCursor.moveToNext());
		}

		return status;
	}
	public Cursor GetAllData(){
		return our_Database.query(DATABASE_SIGNAL_TABLE, new String[] {PATIENT_SIGNAL, SIGNAL_STATUS}, null, null, null, null, null);
	}
	public Boolean validate(String phone_no,String password)throws SQLException{
		String column[]={PATIENT_NAME,PATIENT_PHONE_NO, RELATED_DOCTOR,DOCTOR_PHONE_NO,RELATED_PERSON_NAME, RELATED_PERSON_PHONE_NO ,PATIENT_PASSWORD };
		Boolean flag=false;
		Cursor mCursor =
				our_Database.query(true, DATABASE_TABLE, column ,null , null,
						null, null, null, null);
		if (mCursor.moveToFirst())
		{
			do {
				if((mCursor.getString(1).equals(phone_no))&&(mCursor.getString(6).equals(password)))
					flag=true;
			} while (mCursor.moveToNext());
		}
		return flag;
	}
	public String get_doctor_no(){
		String column[]={PATIENT_NAME,PATIENT_PHONE_NO, RELATED_DOCTOR,DOCTOR_PHONE_NO,RELATED_PERSON_NAME, RELATED_PERSON_PHONE_NO ,PATIENT_PASSWORD };
		Cursor mCursor =
				our_Database.query(true, DATABASE_TABLE, column ,null , null,
						null, null, null, null);
		if (mCursor.moveToFirst())
		{
			do {
				return mCursor.getString(3);
			} while (mCursor.moveToNext());
		}
		return mCursor.getString(3);
	}
	public String get_belonging_no(){
		String column[]={PATIENT_NAME,PATIENT_PHONE_NO, RELATED_DOCTOR,DOCTOR_PHONE_NO,RELATED_PERSON_NAME, RELATED_PERSON_PHONE_NO ,PATIENT_PASSWORD };
		Cursor mCursor =
				our_Database.query(true, DATABASE_TABLE, column ,null , null,
						null, null, null, null);
		if (mCursor.moveToFirst())
		{
			do {
				return mCursor.getString(5);
			} while (mCursor.moveToNext());
		}
		return mCursor.getString(5);
	}

	//---updates a contact---
	public boolean updateContact(String p_number,String heart_signal)
	{
		ContentValues args = new ContentValues();
		args.put(PATIENT_SIGNAL, heart_signal);
		return our_Database.update(DATABASE_TABLE, args, RELATED_PERSON_PHONE_NO + "=" + p_number, null) > 0;
	}

	public void Close(){

		our_Helper.close();
	}

}
