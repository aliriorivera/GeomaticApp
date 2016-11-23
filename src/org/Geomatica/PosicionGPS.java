package org.Geomatica;

import com.google.android.maps.MapActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class PosicionGPS extends MapActivity{
	private LocationManager manager;
    private LocationListener listener; 
    
    public PosicionGPS(){

    	manager = (LocationManager) getSystemService
    	(Context.LOCATION_SERVICE);
    	Location myLocation = manager.getLastKnownLocation("gps"); 
    	listener = new MyLocationListener();
        manager.requestLocationUpdates("gps" ,10000L, 10.0f,listener); 
    }

    
    private class MyLocationListener implements LocationListener{

        public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                if (location != null){
                        
                }
        }

        public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub
        }
    } 
    
    
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
