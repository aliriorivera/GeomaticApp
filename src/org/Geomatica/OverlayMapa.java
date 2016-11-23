package org.Geomatica;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class OverlayMapa extends Overlay {
	
	public Double latitud = 37.40*1E6;
	public Double longitud = -5.99*1E6;
	public String user="";
	
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) 
	{
		Projection projection = mapView.getProjection();
		GeoPoint geoPoint = 
			new GeoPoint(latitud.intValue(), longitud.intValue());
		
		if (shadow == false) 
		{
			Point centro = new Point();
			projection.toPixels(geoPoint, centro);

			//Definimos el pincel de dibujo
			Paint p = new Paint();
			p.setColor(Color.RED);
			
			//Marca Ejemplo 1: Círculo y Texto
			//canvas.drawCircle(centro.x, centro.y, 5, p);
			canvas.drawText(user, centro.x+10, centro.y+5, p);
			
			//Marca Ejemplo 2: Bitmap
			Bitmap bm = BitmapFactory.decodeResource(
					mapView.getResources(), 
					R.drawable.marcador_google_maps);
			
			canvas.drawBitmap(bm, centro.x - bm.getWidth(), 
					              centro.y - bm.getHeight(), p);
		}
	}
	
	@Override
	public boolean onTap(GeoPoint point, MapView mapView) 
	{
		Context contexto = mapView.getContext();
		String msg = "Lat: " + point.getLatitudeE6()/1E6 + " - " + 
		             "Lon: " + point.getLongitudeE6()/1E6;
		
		Toast toast = Toast.makeText(contexto, msg, Toast.LENGTH_SHORT);
		toast.show();
	
		return true;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}