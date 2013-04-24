package newcanuck.client.views.mission;

import java.util.Date;

import newcanuck.client.localDatabase.datasource.FeelingDatabaseDatasource;
import newcanuck.client.localDatabase.datasource.MyMissionDatabaseDatasource;
import newcanuck.client.tools.GPSManager.GPSManager;
import newcanuck.client.tools.lazyloaderdemo.ImageLoader;
import newcanuck.client.utility.GlobalParams;
import newcanuck.client.views.R;
import newcanuck.client.views.map.SeeLocationActivity;
import newcanuck.entity.Feeling;
import newcanuck.entity.Mission;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MissionPageActivity extends Activity {

	private static final int ADD_FEELING_RETURN = 0;

	// data
	private Mission mission;

	// datasource
	private MyMissionDatabaseDatasource myMissionDatabaseDatasource;
	private FeelingDatabaseDatasource feelingDatabaseDatasource;

	// tools
	private ImageLoader mImageLoader;
	private GPSManager gpsManager;

	// widgets
	private TextView missionNameTextView;
	private ImageView missionImageView;
	private TextView descriptionTextView;
	private TextView addressTextView;
	private TextView ratingTextView;
	private RatingBar ratingBar;
	private Button takeTheMissionButton;
	private Button completeButton;
	private Button giveUpButton;
	private Button seeFeelingButton;
	private Button moreCommentButton;
	private Button googleMapButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mission_page);

		// init datasources
		myMissionDatabaseDatasource = new MyMissionDatabaseDatasource(this);
		feelingDatabaseDatasource = new FeelingDatabaseDatasource(this);

		gpsManager = new GPSManager(this);
		if (!gpsManager.initialize()) {
			Toast.makeText(MissionPageActivity.this,
					"GPS device not available", Toast.LENGTH_SHORT).show();
		}

		mission = (Mission) getIntent().getSerializableExtra("mission");
		if (mission != null) {
			mImageLoader = new ImageLoader(this);
			initWidgets();
		}
	}

	@Override
	protected void onPause() {
		gpsManager.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		gpsManager.onResume();
		super.onResume();
	}

	private void initWidgets() {
		missionNameTextView = (TextView) findViewById(R.id.textViewMissionName);
		missionNameTextView.setText(mission.getName());

		missionImageView = (ImageView) findViewById(R.id.imageViewMission);
		String url = GlobalParams.URL_USER_IMAGE_LARGE_PREFIX
				+ mission.getImgFileName() + ".jpg";
		mImageLoader.DisplayImage(url, missionImageView, false);

		descriptionTextView = (TextView) findViewById(R.id.textViewDescription);
		descriptionTextView.setText(mission.getDescription());

		addressTextView = (TextView) findViewById(R.id.TextViewAddress);
		addressTextView.setText(mission.getAddress());

		ratingTextView = (TextView) findViewById(R.id.textViewRating);
		ratingTextView.setText(mission.getRating() + " / by "
				+ mission.getRatetimes() + " users");


		ratingBar = (RatingBar)findViewById(R.id.ratingBar);
		ratingBar.setRating(mission.getRating().floatValue());
		
		takeTheMissionButton = (Button) findViewById(R.id.buttonTakeTheMission);
		completeButton = (Button) findViewById(R.id.buttonComplete);
		giveUpButton = (Button) findViewById(R.id.buttonGiveUp);
		seeFeelingButton = (Button) findViewById(R.id.buttonSeeMyFeeling);
		moreCommentButton = (Button) findViewById(R.id.commentButton);
		googleMapButton = (Button) findViewById(R.id.buttonMap);
		
		initButtons();
	}

	// buttons will change depending on the mission state.
	// call when need to update the mission state.
	private void initButtons() {
		if (mission.getState() == null || mission.getState().equals(0L)) {
			takeTheMissionButton.setVisibility(View.VISIBLE);
			completeButton.setVisibility(View.INVISIBLE);
			giveUpButton.setVisibility(View.INVISIBLE);
			seeFeelingButton.setVisibility(View.INVISIBLE);

			initTakeTheMissionButton();
		} else if (mission.getState().equals(1L)){
			takeTheMissionButton.setVisibility(View.INVISIBLE);
			completeButton.setVisibility(View.VISIBLE);
			giveUpButton.setVisibility(View.VISIBLE);
			seeFeelingButton.setVisibility(View.INVISIBLE);

			initGiveUpButton();
			initCompleteButton();
		} else if (mission.getState().equals(2L)){
			takeTheMissionButton.setVisibility(View.INVISIBLE);
			completeButton.setVisibility(View.INVISIBLE);
			giveUpButton.setVisibility(View.INVISIBLE);
			seeFeelingButton.setVisibility(View.VISIBLE);
			
			initSeeFeelingButton();
		}
		
		initCommentButton();
		initMapButton();
	}

	private void initMapButton() {
		googleMapButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MissionPageActivity.this,SeeLocationActivity.class);
				intent.putExtra("lng", mission.getLongitude());
				intent.putExtra("lat", mission.getLatitude());
				startActivityForResult(intent,0);
			}
			
		});
		
	}

	private void initCommentButton() {
		moreCommentButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MissionPageActivity.this,CommentsActivity.class);
				intent.putExtra("missionId", mission.getId());
				intent.putExtra("missionName", mission.getName());
				
				startActivityForResult(intent,0);
			}
			
		});
		
	}

	private void initSeeFeelingButton() {
		seeFeelingButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Feeling myFeeling = feelingDatabaseDatasource.getFeelingByMission(mission.getId());
				
				if(myFeeling != null){
					Bundle b = new Bundle();
					if (myFeeling != null)
						b.putSerializable("feeling", myFeeling);
				
					Intent i = new Intent(MissionPageActivity.this, FeelingActivity.class);
					i.putExtras(b);

					startActivityForResult(i, 0);
				}
			}
		});
	}

	private void initTakeTheMissionButton() {
		takeTheMissionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mission.setState(1L);
				mission.setAddDate(new Date().getTime());
				myMissionDatabaseDatasource.insertMission(mission);
				Toast.makeText(MissionPageActivity.this,
						"Add mission successfully", Toast.LENGTH_SHORT).show();
				initButtons();
			}

		});
	}

	private void initCompleteButton() {

		completeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!gpsManager.gpsEnabled()) {
					Toast.makeText(MissionPageActivity.this,
							"GPS device not available", Toast.LENGTH_SHORT)
							.show();
				} else {
					Location currentLocation = gpsManager.getCurrentLocation();
					double latitudeDifference = Math.abs(mission.getLatitude()
							- currentLocation.getLatitude());
					double longitudeDifference = Math.abs(mission
							.getLongitude() - currentLocation.getLongitude());

					if (latitudeDifference < 1d
							&& longitudeDifference < 1d) {
						// TODO: do research in the precision.
						Toast.makeText(MissionPageActivity.this, "Complete!",
								Toast.LENGTH_SHORT).show();
						Bundle b = new Bundle();
						b.putSerializable("mission", mission);

						Intent i = new Intent(MissionPageActivity.this,
								AddFeelingActivity.class);
						i.putExtras(b);
						startActivityForResult(i, ADD_FEELING_RETURN);

					} else {
						Toast.makeText(
								MissionPageActivity.this,
								"You are not at the agreed position of this mission.",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ADD_FEELING_RETURN && resultCode == Activity.RESULT_OK){
			mission.setState(2L);
			initButtons();
			Feeling myFeeling = feelingDatabaseDatasource.getFeelingByMission(mission.getId());
			
			if(myFeeling != null){
				Bundle b = new Bundle();
				if (myFeeling != null)
					b.putSerializable("feeling", myFeeling);
			
				Intent i = new Intent(MissionPageActivity.this, FeelingActivity.class);
				i.putExtras(b);
				startActivityForResult(i, 0);
			}
		}
	}

	private void initGiveUpButton() {
		giveUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						MissionPageActivity.this);

				builder.setMessage("Confirm to give up the mission?").setTitle(
						"Alert");

				builder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								myMissionDatabaseDatasource
										.deleteMyMission(mission.getId());
								Toast.makeText(MissionPageActivity.this,
										"Give up mission successfully",
										Toast.LENGTH_SHORT).show();
								mission.setState(0L);
								initButtons();
							}
						});
				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog, return and do
								// nothing
							}
						});

				AlertDialog dialog = builder.create();
				dialog.show();
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_mission_page, menu);
		return true;
	}

}
