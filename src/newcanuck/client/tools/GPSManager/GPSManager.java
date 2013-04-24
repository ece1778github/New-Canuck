package newcanuck.client.tools.GPSManager;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSManager {
	private Context mContext;

	private LocationManager locationManager;
	private String provider;
	private Location location;
	
	public GPSManager(Context context){
		mContext = context;
	}
	
	public boolean initialize(){
		
		locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			getProvider();
			locationManager.requestLocationUpdates("gps", 1000, 1,locationListener);
			location = locationManager.getLastKnownLocation(provider);
			return true;
		}
		
		return false;
	}
	
	public Location getCurrentLocation(){
		location = locationManager.getLastKnownLocation(provider);
		return location;
	}
	
	public void onPause(){
		locationManager.removeUpdates(locationListener);
	}
	
	public boolean gpsEnabled(){
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	public void onResume(){
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(provider, 1000, 1, locationListener);
		}
	}

	private void getProvider() {
		
		Criteria criteria = new Criteria();
		/*
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(true);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		*/
		provider = locationManager.getBestProvider(criteria, true);
	}
	
	LocationListener locationListener = new LocationListener(){

		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	public void setLocationListener(LocationListener listener){
		locationListener = listener;
	}

}
