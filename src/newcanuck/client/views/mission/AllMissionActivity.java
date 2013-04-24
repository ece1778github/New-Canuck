package newcanuck.client.views.mission;

import java.util.List;
import java.util.Map;

import newcanuck.client.localDatabase.datasource.MissionDatabaseDatasource;
import newcanuck.client.localDatabase.datasource.MyMissionDatabaseDatasource;
import newcanuck.client.network.datasource.MissionNetworkDatasource;
import newcanuck.client.views.R;
import newcanuck.entity.Mission;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class AllMissionActivity extends Activity {

	//widgets
	private Button btnUpdate;
	private ListView listViewMissions;
	private ProgressBar progressBar;
	
	//datasource
	private MissionNetworkDatasource missionNetworkDatasource;
	private MissionDatabaseDatasource missionDatabaseDatasource;
	private MyMissionDatabaseDatasource myMissionDatabaseDatasource;
	
	private AllMissionLoaderAdapter adapter;
	
	//data
	private List<Mission> missions = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_mission);
		
		//load widgets
		btnUpdate = (Button)findViewById(R.id.btnUpdateMissions);
		
		setupListView();
		
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		progressBar.setVisibility(View.INVISIBLE);
		
		btnUpdate.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				updateMissionsList();
			}
		
		});
		
		//init datasource
		missionNetworkDatasource = new MissionNetworkDatasource();
		missionDatabaseDatasource = new MissionDatabaseDatasource(this);
		myMissionDatabaseDatasource = new MyMissionDatabaseDatasource(this);
		
		missions = missionDatabaseDatasource.getAllMissions();
		
		
		if(missions == null){
			updateMissionsList();
		}
		else{
			updateMissionsListUI();
		}
	
	}

	private void setupListView() {
		listViewMissions = (ListView)findViewById(R.id.listViewMissions);
		adapter = new AllMissionLoaderAdapter(this);
		listViewMissions.setAdapter(adapter);
		listViewMissions.setOnScrollListener(mScrollListener);
		
		listViewMissions.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Mission mission = missions.get(position);
				Mission myMission = myMissionDatabaseDatasource.getMyMission(mission.getId());
				
				Bundle b= new Bundle();
				if(myMission != null)
					b.putSerializable("mission", myMission);
				else
					b.putSerializable("mission", mission);
				
				Intent i = new Intent(AllMissionActivity.this, MissionPageActivity.class);
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
			if(firstVisibleItem > 0){
				btnUpdate.setHeight(0);
				Log.v("button","0");
			}else{
				btnUpdate.setHeight(50);
				Log.v("button","50");
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_all_mission, menu);
		return true;
	}
	
	//update the data, listView ui.
	public void updateMissionsList(){
		progressBar.setVisibility(View.VISIBLE);
		listViewMissions.setVisibility(View.INVISIBLE);
		
		//create a thread to get data and store them into database.
		UpdateMissionsASyncTask updateTask = new UpdateMissionsASyncTask();
		updateTask.execute();
	}
	

	private void updateMissionsListUI() {
		adapter.setMissions(missions);
		adapter.notifyDataSetChanged();
		listViewMissions.setVisibility(View.VISIBLE);
	}


	public class UpdateMissionsASyncTask extends AsyncTask<String, Integer, List<Map<String, Object>>> {
		List<Mission> tempMissions = null;
		
		@Override
		protected List<Map<String, Object>> doInBackground(String... params) { 
			
			try{
				tempMissions = missionNetworkDatasource.getAllMissionsFromServer();
			}
			catch(Exception e){
				tempMissions = null;
			}
			
			return null;
		}
		

		@Override
		protected void onPostExecute(List<Map<String, Object>> result) {
			if(tempMissions == null){
				progressBar.setVisibility(View.INVISIBLE);
				Toast.makeText(AllMissionActivity.this, "Internet inavailable or no data", Toast.LENGTH_SHORT).show();
				
				updateMissionsListUI();
			}
			else{
				progressBar.setVisibility(View.INVISIBLE);
				missionDatabaseDatasource.updateAllMissions(tempMissions);
				missions = missionDatabaseDatasource.getAllMissions();
				Toast.makeText(AllMissionActivity.this, "update missions seccessfully", Toast.LENGTH_SHORT).show();
				
				updateMissionsListUI();
			}
			
		}
	}

}
