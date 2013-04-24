package newcanuck.client.views.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import newcanuck.client.localDatabase.datasource.MissionDatabaseDatasource;
import newcanuck.client.localDatabase.datasource.MyMissionDatabaseDatasource;
import newcanuck.client.tools.GPSManager.GPSManager;
import newcanuck.client.views.R;
import newcanuck.client.views.R.layout;
import newcanuck.client.views.R.menu;
import newcanuck.client.views.mission.AllMissionActivity;
import newcanuck.client.views.mission.CreateMissionActivity;
import newcanuck.client.views.mission.MissionPageActivity;
import newcanuck.entity.Mission;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

public class MissionsInMapActivity extends AbstractMapActivity {

	private ArrayList<Marker> missionMarkers;
	private Marker currentLocationMarker;

	private List<Mission> missions;
	private GoogleMap map;

	private GPSManager gpsManager;

	private MissionDatabaseDatasource missionDatabaseDatasource;
	private MyMissionDatabaseDatasource myMissionDatabaseDatasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (readyToGo()) {
			setContentView(R.layout.activity_map);
		}

		initMapAndGPS();

		missionDatabaseDatasource = new MissionDatabaseDatasource(this);
		myMissionDatabaseDatasource = new MyMissionDatabaseDatasource(this);
		missions = missionDatabaseDatasource.getAllMissions();

		missionMarkers = new ArrayList<Marker>();

		for (Mission mission : missions) {

			Double longitude = mission.getLongitude();
			Double latitude = mission.getLatitude();
			LatLng pos = new LatLng(latitude, longitude);

			StringBuffer snippet = new StringBuffer();
			String[] strs = mission.getDescription().split(" ");

			int wordcount = 0;
			for (String str : strs) {
				wordcount++;
				snippet.append(str).append(" ");
				if (wordcount == 13)
					break;
			}
			snippet.append("бнбн");

			Marker marker = map
					.addMarker(new MarkerOptions()
							.position(pos)
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.star))
							.title(mission.getName()));
			//marker.setSnippet(snippet.toString());

			missionMarkers.add(marker);
		}

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);

		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				int index = missionMarkers.indexOf(marker);
				if (index != -1) { // means find it as a mission
					Mission mission = missions.get(index);
					Mission myMission = myMissionDatabaseDatasource.getMyMission(mission.getId());
					
					Bundle b= new Bundle();
					if(myMission != null)
						b.putSerializable("mission", myMission);
					else
						b.putSerializable("mission", mission);
					
					Intent i = new Intent(MissionsInMapActivity.this, MissionPageActivity.class);
					i.putExtras(b);
					
					startActivityForResult(i, 0);
				}
			}

		});
	}

	private void initMapAndGPS() {
		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		gpsManager = new GPSManager(this);
		gpsManager.setLocationListener(listener);

		if (!gpsManager.initialize()) {
			Toast.makeText(MissionsInMapActivity.this,
					"GPS device not available", Toast.LENGTH_SHORT).show();
			return;
		}

		Location currentLocation = gpsManager.getCurrentLocation();
		if(currentLocation == null){
			Toast.makeText(MissionsInMapActivity.this,
					"Your current location is not available", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
		LatLng currentLatLng = new LatLng(currentLocation.getLatitude(),
				currentLocation.getLongitude());
		currentLocationMarker = map.addMarker(new MarkerOptions()
				.position(currentLatLng)
				.title("Your current location")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.current_location)));

		// Move the camera instantly to Bahen Centre with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));

	}

	LocationListener listener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			currentLocationMarker.setPosition(new LatLng(
					location.getLatitude(), location.getLongitude()));

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	};

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

}
