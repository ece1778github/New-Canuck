package newcanuck.client.views.record;

import java.util.List;

import newcanuck.client.localDatabase.datasource.FeelingDatabaseDatasource;
import newcanuck.client.localDatabase.datasource.MyMissionDatabaseDatasource;
import newcanuck.client.views.R;
import newcanuck.client.views.R.layout;
import newcanuck.client.views.R.menu;
import newcanuck.client.views.mission.FeelingActivity;
import newcanuck.client.views.mission.MissionPageActivity;
import newcanuck.client.views.mission.MyMissionActivity;
import newcanuck.client.views.mission.MyMissionLoaderAdapter;
import newcanuck.entity.Feeling;
import newcanuck.entity.Mission;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class GrowthPathActivity extends Activity {

	//widgets
	private ListView listViewFeelings;
	
	//data
	private List<Feeling> myFeelings = null;

	//datasource
	private FeelingDatabaseDatasource myFeelingDatabaseDatasource;

	private GrowthPathAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_growth_path_list);
		setupListView();
		myFeelingDatabaseDatasource = new FeelingDatabaseDatasource(this);
		updateFeelingsListUI();
	}

	private void updateFeelingsListUI() {
		// TODO Auto-generated method stub
		myFeelings = myFeelingDatabaseDatasource.getAllFeelings();
		if (myFeelings != null) {
			adapter.setFeelings(myFeelings);
			adapter.notifyDataSetChanged();
		}
		
	}

	private void setupListView() {
		
		listViewFeelings = (ListView)findViewById(R.id.listViewFeelings);
		adapter = new GrowthPathAdapter(this);
		listViewFeelings.setAdapter(adapter);

		listViewFeelings.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Feeling myFeeling = myFeelings.get(position);

				Bundle b = new Bundle();
				if (myFeeling != null)
					b.putSerializable("feeling", myFeeling);

				Intent i = new Intent(GrowthPathActivity.this, FeelingActivity.class);
				i.putExtras(b);

				startActivityForResult(i, 0);
			}

		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.growth_path, menu);
		return true;
	}

}
