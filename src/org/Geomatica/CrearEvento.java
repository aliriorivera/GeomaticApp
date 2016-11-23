package org.Geomatica;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.Geomatica.Registro.Post;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CrearEvento extends Activity {
	
	EditText nombre, fecha,hora,latitud,longitud;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evento);
        
        nombre = (EditText)findViewById(R.id.nombreev);
        fecha = (EditText)findViewById(R.id.fechaev);
        hora = (EditText)findViewById(R.id.horaev);
        latitud = (EditText)findViewById(R.id.latitudev);
        longitud = (EditText)findViewById(R.id.longitudev);
        Button revento =(Button) findViewById(R.id.reventoev);
        
        
        revento.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				if(nombre.getText().toString().equals("") | fecha.getText().toString().equals("") | hora.getText().toString().equals("") | latitud.getText().toString().equals("") | longitud.getText().toString().equals("")){
            		Toast.makeText(getBaseContext(),"Error: Datos en Blanco.", Toast.LENGTH_SHORT).show();
            	}
            	else{
            		/*Toast.makeText(getBaseContext(),"OK.", Toast.LENGTH_SHORT).show();*/
            		
            		/***aqui empieza lo de PHP**/
            		ArrayList parametros = new ArrayList();
            		parametros.add("Nombre");
                	parametros.add(nombre.getText().toString());
                	parametros.add("Fecha");
                	parametros.add(fecha.getText().toString());
                	parametros.add("Hora");
                	parametros.add(hora.getText().toString());
                	parametros.add("Latitud");
                	parametros.add(latitud.getText().toString());
                	parametros.add("Longitud");
                	parametros.add(longitud.getText().toString());
                	// Llamada a Servidor Web PHP
                	try {
                	     Post post = new Post();
                	     JSONArray datos = post.getServerData(parametros,"http://alirio.dyndns.org:81/registroev.php");
                	if (datos != null && datos.length() > 0) {
                	                        JSONObject json_data = datos.getJSONObject(0);
                	                        int numRegistrados = json_data.getInt("value");
                	if (numRegistrados == 1) {
                	    Toast.makeText(getBaseContext(),"Error en El registro. ", Toast.LENGTH_SHORT).show();
                		//aqui entramos a la actividad siguiente si los datos son correctos
                	                        }
                	} else {
                		 nombre.setText("");
                		 fecha.setText("");
                		 hora.setText("");
                		 latitud.setText("");
                		 longitud.setText("");
                	     Toast.makeText(getBaseContext(),"evento Registrado. ", Toast.LENGTH_SHORT).show();
                	                        }
                	} catch (Exception e) {
                	                        Toast.makeText(getBaseContext(),
                	                                                                       "Error al conectar con el servidor. ",
                	                                                                       Toast.LENGTH_SHORT).show();
                	}
                	// FIN Llamada a Servidor Web PHP
            	}
			}
        	
        });
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


