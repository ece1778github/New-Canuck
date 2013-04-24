package newcanuck.client.views.mission;

import newcanuck.client.views.R;
import newcanuck.client.views.map.MissionsInMapActivity;
import newcanuck.client.views.map.MyMissionsInMapActivity;
import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

@SuppressWarnings("deprecation")
public class MissionActivity extends TabActivity {

	// widgets
	// tabWidget
	TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mission);

		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.addTab(tabHost
				.newTabSpec("all_missions")
				.setIndicator("All Missions",
						getResources().getDrawable(R.drawable.icon2))
				.setContent(new Intent(this, AllMissionActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("my_missions")
				.setIndicator("My Missions",
						getResources().getDrawable(R.drawable.icon2))
				.setContent(new Intent(this, MyMissionActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("create_mission")
				.setIndicator("New Mission",
						getResources().getDrawable(R.drawable.icon2))
				.setContent(new Intent(this, CreateMissionActivity.class)));

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				mCurrentTabName = tabId;
				onCreateOptionsMenu(mMenu);

				// TODO: check whether the user has completed more than 5
				// missions.
				/*
				 * if(tabId.equals("create_mission")){ tabHost.setCurrentTab(1);
				 * }
				 */
			}

		});

	}

	public void setCurrentTab(int index) {
		tabHost.setCurrentTab(index);
	}

	private String mCurrentTabName = "all_missions";
	private Menu mMenu;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.menu_update: {
			AllMissionActivity currentActivity = (AllMissionActivity) this
					.getCurrentActivity();
			currentActivity.updateMissionsList();
			break;
		}
		case R.id.menu_see_missions_in_map: {
			AllMissionActivity currentActivity = (AllMissionActivity) this
					.getCurrentActivity();
			startActivityForResult(new Intent(currentActivity, MissionsInMapActivity.class),0);
			break;
		}
		case R.id.menu_see_my_missions_in_map: {
			MyMissionActivity currentActivity = (MyMissionActivity) this
					.getCurrentActivity();
			startActivityForResult(new Intent(currentActivity, MyMissionsInMapActivity.class),0);
			break;
		}
		default:
			return super.onOptionsItemSelected(item);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		mMenu = menu;
		mMenu.clear();

		if (mCurrentTabName.equals("all_missions"))
			getMenuInflater().inflate(R.menu.activity_all_mission, menu);
		else if (mCurrentTabName.equals("my_missions"))
			getMenuInflater().inflate(R.menu.activity_my_mission, menu);
		else if (mCurrentTabName.equals("create_mission"))
			getMenuInflater().inflate(R.menu.activity_create_mission, menu);
		else
			getMenuInflater().inflate(R.menu.activity_mission, menu);

		return true;
	}

}
