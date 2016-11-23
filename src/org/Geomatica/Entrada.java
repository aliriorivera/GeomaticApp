package org.Geomatica; 
	import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
	 
	import android.graphics.drawable.Drawable;
	import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
	 
	import android.location.Location;
	import android.location.LocationListener;
	import android.location.LocationManager;
	import android.content.Context;
import android.content.Intent;
	
	import com.google.android.maps.GeoPoint;
	import com.google.android.maps.MapActivity;
	import com.google.android.maps.MapController;
	import com.google.android.maps.MapView;
	import com.google.android.maps.Overlay;
	import com.google.android.maps.OverlayItem;
	import org.Geomatica.CustomItemizedOverlay;
import org.Geomatica.OverlayMapa;
import org.Geomatica.GeomaticaActivity.Post;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
	 
	public class Entrada extends MapActivity {
	     
	    private MapView mapa;
	     
	    Double latitud = 4.637900366385035 * 1e6;
		Double longitud = -74.08417403697968 * 1e6;
		
		Double latitud2 = 4.6375395 * 1e6;
		Double longitud2 = -74.0826747 * 1e6;
		private MapController controlMapa;
		
		private LocationManager manager;
	    private LocationListener listener;
	     
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	         
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.entrada);
	        
	        mapa = (MapView)findViewById(R.id.map_view);
	         
	        Button cevento =(Button) findViewById(R.id.crearev);
	        Button vsatelite =(Button) findViewById(R.id.Vsatelital);
	        Button veventos =(Button) findViewById(R.id.vereventos);
	        Button vpersonas =(Button) findViewById(R.id.verpersonas);
	        
			/*******************parte de obtencion de posiciones****************/
	        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    	Location myLocation = manager.getLastKnownLocation("gps"); 
	    	listener = new MyLocationListener();
	        manager.requestLocationUpdates("gps" ,10000L, 10.0f,listener);
	        /***************fin de la obtencion de coordenadas*****************/
	        
	        
	        
	        cevento.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Entrada.this, CrearEvento.class);
	        		//intent.putExtra("id",numRegistrados);
	        		startActivity(intent);					
				} 
				
			});
			
			vsatelite.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(mapa.isSatellite())
						mapa.setSatellite(false);
					else
						mapa.setSatellite(true);					
				} 
				
			});
			
			veventos.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					try {
                        Post post = new Post();
                        JSONArray datos = post.getServerData(null,
                                                                       "http://alirio.dyndns.org:81/eventos.php");
                        // No se puede poner localhost, carga la consola de Windows
                        // y escribe ipconfig/all para ver tu IP
                        if (datos != null && datos.length() > 0) {
                        	mapa.getOverlays().clear();
                        	int val = datos.length();
                        	//val = val+1;
                        	//val = val/6;
                        	Toast.makeText(getBaseContext(),
                                    val + " evento(s)", Toast.LENGTH_SHORT)
                                                           .show();
                        	for(int i=0; i<val; i++){
                        		JSONObject json_data = datos.getJSONObject(i);
                        		String nombre = json_data.getString("nombre");
                        		String tiempo = json_data.getString("tiempo");
                        		String hora = json_data.getString("hora");
                        		Double lat = json_data.getDouble("latitud")* 1e6;
                        		Double lon = json_data.getDouble("longitud")* 1e6;
                        		
                        		//Controlador del mapa
            			        controlMapa = mapa.getController();
            			        
            			        //Mostramos los controles de zoom sobre el mapa
            			        mapa.setBuiltInZoomControls(true);
            			        
            					//Añadimos la capa de marcas
            					List<Overlay> capas = mapa.getOverlays();
            					OverlayMapa om = new OverlayMapa();
            					om.setLatitud(latitud);
            					om.setLongitud(longitud);
            					String a = nombre + "\n "+ tiempo +"\n " + hora;
            					om.setUser(a);
            					capas.add(om);
            					Toast.makeText(getBaseContext(),
                                        a , Toast.LENGTH_SHORT)
                                                               .show();
            					
                        		
                        	}
                        	mapa.postInvalidate();

                        } else {
                        	Toast.makeText(getBaseContext(),
                                                                       "No hay eventos Registrados. ", Toast.LENGTH_SHORT)
                                                                                              .show();
                        	}
					} catch (Exception e) {
							Toast.makeText(getBaseContext(),
                                                                       "Error al conectar con el servidor. ",
                                                                       Toast.LENGTH_SHORT).show();
					}			
				}
			});
			
			vpersonas.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					 ///Obtenemos una referencia a los controles
					mapa.getOverlays().clear();
			        
			        //Controlador del mapa
			        controlMapa = mapa.getController();
			        
			        //Mostramos los controles de zoom sobre el mapa
			        mapa.setBuiltInZoomControls(true);
			        
					//Añadimos la capa de marcas
					List<Overlay> capas = mapa.getOverlays();
					OverlayMapa om = new OverlayMapa();
					OverlayMapa om2 = new OverlayMapa();
					om.setLatitud(latitud);
					om.setLongitud(longitud);
					om.setUser("usuario1");
					om2.setLatitud(latitud2);
					om2.setLongitud(longitud2);
					om2.setUser("usuario2");
					capas.add(om);
					capas.add(om2);
					mapa.postInvalidate();
				}
			});
	         
	    }
	    
	    
	    private class MyLocationListener implements LocationListener{

	        public void onLocationChanged(Location location) {
	                // TODO Auto-generated method stub
	                if (location != null){
	                	
	                	mapa.getOverlays().clear();
	                	//Controlador del mapa
	        	        controlMapa = mapa.getController();
	        	        
	        	        Double l=location.getLatitude() * 1E6,lo=location.getLongitude() * 1E6;
	        	        
	        			//Añadimos la capa de marcas
	        			List<Overlay> capas = mapa.getOverlays();
	        			OverlayMapa om = new OverlayMapa();
	        			OverlayMapa om2 = new OverlayMapa();
	        			om.setLatitud(l);
	        			om.setLongitud(lo);
	        			om.setUser("usuario1");
	        			om2.setLatitud(latitud2);
	        			om2.setLongitud(longitud2);
	        			om2.setUser("usuario2");
	        			capas.add(om);
	        			capas.add(om2);
	        			GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
	                    controlMapa.animateTo(point);
	                    controlMapa.setZoom(15);
	        			mapa.postInvalidate();
	        			
	                    
	                    //devuelve la posición
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
	        return false;
	    }

	    
	    
	    class Post {//clase que se conecta con servidor PHP a traves de JSON y HHTP atributes
			
	        private InputStream is = null;
	        private String respuesta = "";

			private void conectaPost(ArrayList parametros, String URL) {
			        ArrayList nameValuePairs;
			        try {
			
			                               HttpClient httpclient = new DefaultHttpClient();
			
			                               HttpPost httppost = new HttpPost(URL);
			                               nameValuePairs = new ArrayList();
			
			                               if (parametros != null) {
			                                                       for (int i = 0; i < parametros.size() - 1; i += 2) {
			nameValuePairs.add(new BasicNameValuePair((String)parametros.get(i), (String)parametros.get(i + 1)));
			                                                       }
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			                               }
			                               HttpResponse response = httpclient.execute(httppost);
			                               HttpEntity entity = response.getEntity();
			                               is = entity.getContent();
			        } catch (Exception e) {
			
			                               Log.e("log_tag", "Error in http connection " + e.toString());
			
			        } finally {
			
			        }
			}
			
			private void getRespuestaPost() {
			try {
			BufferedReader reader = new BufferedReader(
			new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
			        sb.append(line + "\n");
			}
			is.close();
			respuesta = sb.toString();
			Log.e("log_tag", "Cadena JSon " + respuesta);
			} catch (Exception e) {
			
			        Log.e("log_tag", "Error converting result " + e.toString());
			
			        }
			}
			
			@SuppressWarnings("finally")
			private JSONArray getJsonArray() {
			        JSONArray jArray = null;
			        try {
			           jArray = new JSONArray(respuesta);
			        } catch (Exception e) {
			
			        } finally {
			           return jArray;
			        }
			}
			
			public JSONArray getServerData(ArrayList parametros, String URL) {
			        conectaPost(parametros, URL);
			        if (is != null) {
			                               getRespuestaPost();
			        }
			        if (respuesta != null && respuesta.trim() != "") {
			                                                                              return getJsonArray();
			                                                       } else {
			                                                                              return null;
			                                                       }
			                               }
			} 
	     
	}