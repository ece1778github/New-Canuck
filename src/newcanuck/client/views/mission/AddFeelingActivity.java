package newcanuck.client.views.mission;

import java.util.Date;

import newcanuck.client.localDatabase.datasource.FeelingDatabaseDatasource;
import newcanuck.client.localDatabase.datasource.MyMissionDatabaseDatasource;
import newcanuck.client.tools.CameraManager.CameraManager;
import newcanuck.client.userProfile.UserProfile;
import newcanuck.client.views.R;
import newcanuck.entity.Feeling;
import newcanuck.entity.Mission;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

public class AddFeelingActivity extends Activity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	// widgets
	private EditText editTextFeeling;
	private RatingBar ratingBar;
	private Button btnTakePic;
	private Button btnChangePic;
	private Button btnSubmit;
	private ImageView imageViewPic;

	// data
	private Mission mission;

	// tools
	private CameraManager cameraManager;
	
	//datasource
	private FeelingDatabaseDatasource feelingDatabaseDatasource;
	private MyMissionDatabaseDatasource myMissionDatabaseDatasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_feeling);
		Log.v("new mission","oncreate");
		
		mission = (Mission) getIntent().getSerializableExtra("mission");
		
		cameraManager = new CameraManager(this);
		
		feelingDatabaseDatasource = new FeelingDatabaseDatasource(this);
		myMissionDatabaseDatasource = new MyMissionDatabaseDatasource(this);
		
		initWidgets();

	}

	private void initWidgets() {

		editTextFeeling = (EditText) findViewById(R.id.editTextFeeling);
		initRatingBar();
		initBtnTakePic();
		initBtnChangePic();
		initBtnSubmit();
		initImagePic();
	}

	private void initRatingBar() {
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		ratingBar.setRating(3f);
	}

	private void initBtnSubmit() {
		btnSubmit = (Button)findViewById(R.id.buttonSubmit);
		btnSubmit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {

				//read the feeling info from widgets
				String howDoYouFeel = editTextFeeling.getText().toString();
				Double rating = (double) ratingBar.getRating();
				
				if(howDoYouFeel == null || howDoYouFeel.equals("")){
					Toast.makeText(AddFeelingActivity.this, "Please enter how do you feel.", Toast.LENGTH_SHORT).show();
					return;
				}

				Feeling feeling = new Feeling();
				
				//store the pic at first.
				if(cameraManager.picExist()){
					try {
						String fileName = new Date().getTime()+".jpg";
						cameraManager.storeThePicture(fileName);
						feeling.setMyImgFileName(fileName);
					} catch (Exception e) {
						Log.e("camera", e.getMessage());
					}
				}
				
				//set feeling data
				feeling.setMyFeeling(howDoYouFeel);
				feeling.setMyRating(rating);
				feeling.setCreateDate(new Date().getTime());
				feeling.setMissionId(mission.getId());
				feeling.setMissionName(mission.getName());
				feeling.setMissionAddress(mission.getAddress());
				feeling.setMissionDescription(mission.getDescription());
				feeling.setMissionLatitude(mission.getLatitude());
				feeling.setMissionLongitude(mission.getLongitude());
				
				//insert feeling to db
				feelingDatabaseDatasource.insertFeeling(feeling);
				mission.setState(2L);
				myMissionDatabaseDatasource.updateMyMission(mission);
				
				int totalMissions = UserProfile.getProfile(AddFeelingActivity.this).getTotolCompletedMissions();
				totalMissions += 1;
				UserProfile.getProfile(AddFeelingActivity.this).setTotolCompletedMissions(totalMissions);
				
				Toast.makeText(AddFeelingActivity.this, "Mission Completed!", Toast.LENGTH_SHORT).show();
				
				AddFeelingActivity.this.setResult(Activity.RESULT_OK);
				AddFeelingActivity.this.finish();	
			}
		});
	}

	private void initImagePic() {
		imageViewPic = (ImageView) findViewById(R.id.imageViewPicture);
		imageViewPic.setVisibility(View.INVISIBLE);
	}

	private void initBtnChangePic() {
		btnChangePic = (Button) findViewById(R.id.buttonChangePic);
		btnChangePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cameraManager
						.startCameraActivityForResult(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
		btnChangePic.setVisibility(View.INVISIBLE);
	}

	private void initBtnTakePic() {
		btnTakePic = (Button) findViewById(R.id.buttonTakePic);
		btnTakePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cameraManager
						.startCameraActivityForResult(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK){
				cameraManager.generateImageFromCamera();
				imageViewPic.setImageBitmap(cameraManager.getCameraImage());
				imageViewPic.setVisibility(View.VISIBLE);
				btnChangePic.setVisibility(View.VISIBLE);
				btnTakePic.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_feeling, menu);
		return true;
	}
}
