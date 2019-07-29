package adapter_for_swipe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.waseem.intelligentheartattackpreventionsystem.registeration.DssModelFragment;
import com.waseem.intelligentheartattackpreventionsystem.registeration.PatientPortalFragment;
import com.waseem.intelligentheartattackpreventionsystem.registeration.SignalFragment;

/**
 * Created by Waseem ud din on 6/2/2014.
 */
public class AdapterSwipe extends FragmentPagerAdapter {


	public AdapterSwipe(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				// Top Rated fragment activity
				return new PatientPortalFragment();
			case 1:
				// Signal fragment activity
				return new SignalFragment();
			case 2:
				// DSS model fragment activity
				return new DssModelFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}
}
