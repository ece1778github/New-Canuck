package newcanuck.client.views.mission;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import newcanuck.client.network.datasource.MissionNetworkDatasource;
import newcanuck.client.tools.CameraManager.CameraManager;
import newcanuck.client.tools.GPSManager.GPSManager;
import newcanuck.client.views.R;
import newcanuck.client.views.map.SetLocationMapActivity;
import newcanuck.entity.Mission;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateMissionActivity extends Activity {


	private static final int GET_LOCATION_FROM_MAP = 1233;

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	protected static final int SLEECT_IMAGE_ACTIVITY_REQUEST_CODE = 101;
	
	//widgets
	private EditText editTextMissionName;
	private EditText editTextDescription;
	private TextView textViewLocation;
	private Button btnTakePic;
	private Button btnChangePic;
	private ImageView imageViewPic;
	private Button buttonSubmit;
	
	private Button buttonMap;
	
	//data
	private Mission mission;
	private Location location;
	
	// tools
	private CameraManager cameraManager;
	private GPSManager gpsManager;
	
    //datasource
	MissionNetworkDatasource missionNetworkDatasource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("track","create mission on create");
		
		setContentView(R.layout.activity_create_mission);
		
		//init data
		mission = new Mission();

		//init datasource
		missionNetworkDatasource = new MissionNetworkDatasource();
		
		//init Widgets
		initWidgets();
		
		//start gps service
		initGPSManager();
		setMissionLocation();
		//update widget
		textViewLocation.setText(mission.getAddress());
		
		//start camera service
		cameraManager = new CameraManager(this);
		
	}

	private void setMissionLocation() {
		if (gpsManager.gpsEnabled()){
			setMissionLocationAndAddress();
		}
		else{
			Toast.makeText(this,"GPS device not available", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private void setMissionLocationAndAddress() {
		//update data
		location = gpsManager.getCurrentLocation();
		if(location == null){
			Toast.makeText(this,"Your current location is not available", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		mission.setLatitude(location.getLatitude());
		mission.setLongitude(location.getLongitude());
		
		NumberFormat ddf = NumberFormat.getNumberInstance();
		ddf.setMaximumFractionDigits(3);
		String lat_info = ddf.format(location.getLatitude());
		String lng_info = ddf.format(location.getLongitude());
		String address = "(" + lat_info + "," + lng_info + ")";
		
		mission.setAddress(address);
		
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK){
				cameraManager.generateImageFromCamera();
				updateImageUI();
			}
		}
		else if (requestCode == SLEECT_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK){
			    Uri uri = data.getData();
		        if (uri != null) {
		            Cursor cursor = getContentResolver().query(uri, new String[] { android.provider.MediaStore.Images.Media.DATA }, null, null, null);
		            if(cursor != null){
		            	cursor.moveToFirst();
		    
		            	final String imageFilePath = cursor.getString(0);
		            	cursor.close();

		            	cameraManager.generateImageFromFileSystem(imageFilePath);
		            }
		        }
				updateImageUI();
			}
		}
		else if (requestCode == GET_LOCATION_FROM_MAP && resultCode == Activity.RESULT_OK){
			double lng = data.getDoubleExtra("lng", 0.0f);
			double lat = data.getDoubleExtra("lat", 0.0f);
			
			location.setLongitude(lng);
			location.setLatitude(lat);
			
			mission.setLatitude(location.getLatitude());
			mission.setLongitude(location.getLongitude());
			
			NumberFormat ddf = NumberFormat.getNumberInstance();
			ddf.setMaximumFractionDigits(3);
			String lat_info = ddf.format(location.getLatitude());
			String lng_info = ddf.format(location.getLongitude());
			String address = "(" + lat_info + "," + lng_info + ")";
			
			mission.setAddress(address);
			textViewLocation.setText(mission.getAddress());
		}
	}

	private void updateImageUI() {
		imageViewPic.setImageBitmap(cameraManager.getCameraImage());
		imageViewPic.setVisibility(View.VISIBLE);
		btnChangePic.setVisibility(View.VISIBLE);
		btnTakePic.setVisibility(View.INVISIBLE);
	}
	
	private void initGPSManager() {
		gpsManager = new GPSManager(this);
		if (!gpsManager.initialize()) {
			Toast.makeText(CreateMissionActivity.this,
					"GPS device not available", Toast.LENGTH_SHORT).show();
		}
	}

	private void initWidgets() {
		editTextMissionName = (EditText) findViewById(R.id.editTextMissionName);
		editTextDescription = (EditText) findViewById(R.id.editTextDescription);
		textViewLocation = (TextView)findViewById(R.id.textViewLocation);
		
		initBtnMap();
		initBtnTakePic();
		initBtnChangePic();
		initImagePic();
		initButtonSubmit();
	}

	private void initBtnMap() {
		buttonMap = (Button)findViewById(R.id.buttonGoogleMap);
		buttonMap.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CreateMissionActivity.this, SetLocationMapActivity.class);
				startActivityForResult(intent,GET_LOCATION_FROM_MAP);
			}
		});
	}

	private void initButtonSubmit() {
		buttonSubmit = (Button)findViewById(R.id.buttonSubmit);
		buttonSubmit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				MissionActivity parent = (MissionActivity) getParent();
				parent.setTitle("Sending Mission......");
				parent.setCurrentTab(0);
				PostMissionASyncTask getTask = new PostMissionASyncTask();
				getTask.execute();
			}
		});
		
		
	}
	
	

	public class PostMissionASyncTask extends AsyncTask<String, Integer, List<Map<String, Object>>> {

		private String result;
		@Override
		protected List<Map<String, Object>> doInBackground(String... params) {
			
			String missionName = editTextMissionName.getText().toString();
			if(missionName!=null && !missionName.equals("")){
				mission.setName(missionName);
			}
			else{
				result = "Please enter the mission name.";
				return null;
			}
			
			String missionDescription = editTextDescription.getText().toString();
			if(missionDescription!=null && !missionDescription.equals("")){
				mission.setDescription(missionDescription);
			}
			else{
				result = "Please enter the mission description.";
				return null;
			}

			if(cameraManager.getCameraImage() == null){
				result = "Please select a picture of the mission.";
				return null;
			}
			
			mission.setCreateDate(new Date().getTime());
			
			result = missionNetworkDatasource.postMission(mission, cameraManager.getCameraImage());
			
			return null;
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> out) {
			//update widget
			textViewLocation.setText(mission.getAddress());
			Activity parent = CreateMissionActivity.this.getParent();
			Toast.makeText(parent, (CharSequence) result, Toast.LENGTH_SHORT).show();

			parent.setTitle("Tour Mission");
		}

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
				selectAnImage();
			}
		});
		btnChangePic.setVisibility(View.INVISIBLE);
	}

	private void initBtnTakePic() {
		btnTakePic = (Button) findViewById(R.id.buttonTakePic);
		btnTakePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {		
				selectAnImage();
			}
		});
	}
	
	private void selectAnImage() {
		AlertDialog.Builder builder = new AlertDialog.Builder(CreateMissionActivity.this);
		builder.setTitle("Select an image");
		builder.setItems(new String[]{"From Camera","From File System"}, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
               if(which == 0){
   					cameraManager.startCameraActivityForResult(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
               }else if(which==1){
            	   	cameraManager.startPickingImageFileActivityForResult(SLEECT_IMAGE_ACTIVITY_REQUEST_CODE);
               }
           }
        });

		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_create_mission, menu);
		return true;
	}
	

}
