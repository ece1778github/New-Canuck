package newcanuck.client.views.mission;

import java.util.List;

import newcanuck.client.localDatabase.datasource.MyMissionDatabaseDatasource;
import newcanuck.client.views.R;
import newcanuck.entity.Mission;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class MyMissionActivity extends Activity {

	//widgets
	private ListView listViewMissions;
	
	//data
	private List<Mission> myMissions = null;

	//datasource
	private MyMissionDatabaseDatasource myMissionDatabaseDatasource;

	private MyMissionLoaderAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_mission);
		
		setupListView();
		
		myMissionDatabaseDatasource = new MyMissionDatabaseDatasource(this);
		
		updateMissionsListUI();
		

	}

	private void setupListView() {
		listViewMissions = (ListView)findViewById(R.id.listViewMissions);
		adapter = new MyMissionLoaderAdapter(this);
		listViewMissions.setAdapter(adapter);
		listViewMissions.setOnScrollListener(mScrollListener);

		listViewMissions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Mission myMission = myMissions.get(position);

				Bundle b = new Bundle();
				if (myMission != null)
					b.putSerializable("mission", myMission);

				Intent i = new Intent(MyMissionActivity.this, MissionPageActivity.class);
				i.putExtras(b);

				startActivityForResult(i, 0);
			}

		});
	}
	

	OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_FLING:
				adapter.setFlagBusy(true);
				break;
			case OnScrollListener.SCROLL_STATE_IDLE:
				adapter.setFlagBusy(false);
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				adapter.setFlagBusy(false);
				break;
			default:
				break;
			}
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}
	};
	
	private void updateMissionsListUI() {
		myMissions = myMissionDatabaseDatasource.getAllMyMissions();
		if (myMissions != null) {
			adapter.setMissions(myMissions);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onResume() {
		updateMissionsListUI();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_my_mission, menu);
		return true;
	}
	

}
