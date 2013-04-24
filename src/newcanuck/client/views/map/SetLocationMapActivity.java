package newcanuck.client.views.map;

import java.util.ArrayList;
import java.util.List;

import newcanuck.client.localDatabase.datasource.MissionDatabaseDatasource;
import newcanuck.client.localDatabase.datasource.MyMissionDatabaseDatasource;
import newcanuck.client.tools.GPSManager.GPSManager;
import newcanuck.client.views.R;
import newcanuck.client.views.mission.CreateMissionActivity;
import newcanuck.client.views.mission.MissionPageActivity;
import newcanuck.entity.Mission;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SetLocationMapActivity extends AbstractMapActivity {
	
	private Marker currentLocationMarker;
	private Marker missionMarker;

	private GoogleMap map;
	private GPSManager gpsManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (readyToGo()) {
			setContentView(R.layout.activity_map);
		}

		initMapAndGPS();
		
		map.setOnMapClickListener(new OnMapClickListener(){

			@Override
			public void onMapClick(LatLng point) {
				missionMarker.setPosition(point);
				missionMarker.showInfoWindow();
			}
			
		});
		
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				if(marker.equals(missionMarker)){
					Intent resultIntent = new Intent();
					resultIntent.putExtra("lat", marker.getPosition().latitude);
					resultIntent.putExtra("lng", marker.getPosition().longitude);
					setResult(Activity.RESULT_OK, resultIntent);
					finish();
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
			Toast.makeText(SetLocationMapActivity.this,
					"GPS device not available", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		Location currentLocation = gpsManager.getCurrentLocation();
		if(currentLocation == null){
			Toast.makeText(SetLocationMapActivity.this,
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
		
		missionMarker = map.addMarker(new MarkerOptions()
				.position(currentLatLng)
				.title("Confirm to choose this location.")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.star)));

		// Move the camera instantly to Bahen Centre with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 20));

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
