package com.waseem.intelligentheartattackpreventionsystem.registeration;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class ReceiveData extends Service {
    public ReceiveData() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
	@Override
	public void onCreate() {
		Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// For time consuming an long tasks you can launch a new thread here..
		Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
		return 0;

	}
	@Override
	public void onDestroy() {
		Toast.makeText(this, " Service Destroyed", Toast.LENGTH_LONG).show();

	}
}
