package newcanuck.client.views.record;

import newcanuck.client.views.R;
import newcanuck.client.views.R.layout;
import newcanuck.client.views.R.menu;
import newcanuck.client.views.mission.AllMissionActivity;
import newcanuck.client.views.mission.CreateMissionActivity;
import newcanuck.client.views.mission.MyMissionActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TabHost;

public class RecordActivity extends TabActivity {

	// widgets
	// tabWidget
	TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		
		tabHost.addTab(tabHost
				.newTabSpec("analysis")
				.setIndicator("Analysis",
						getResources().getDrawable(R.drawable.icon2))
				.setContent(new Intent(this, AnalysisActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("growth_paths")
				.setIndicator("Growth Paths",
						getResources().getDrawable(R.drawable.icon2))
				.setContent(new Intent(this, GrowthPathActivity.class)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record, menu);
		return true;
	}

}
