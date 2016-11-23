package org.Geomatica;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registro extends Activity{
	/** Called when the activity is first created. */
	
	
	
	EditText nombre, apellidos,correo,user,pass;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        
        
        nombre = (EditText)findViewById(R.id.nombre);
        apellidos = (EditText)findViewById(R.id.apellidos);
        correo = (EditText)findViewById(R.id.correo);
        user = (EditText)findViewById(R.id.usuario);
        pass = (EditText)findViewById(R.id.contrasena);
        Button registro =(Button) findViewById(R.id.registro);
        
        registro.setOnClickListener(new View.OnClickListener(){

        	/******************************************************************
        	 * este onclick que sigue es para el registro
        	 * ***************************************************************/
        	
            public void onClick(View view){
            	if(nombre.getText().toString().equals("") | apellidos.getText().toString().equals("") | correo.getText().toString().equals("") | user.getText().toString().equals("") | pass.getText().toString().equals("")){
            		Toast.makeText(getBaseContext(),"Error: Datos en Blanco.", Toast.LENGTH_SHORT).show();
            	}
            	else{
            		/*Toast.makeText(getBaseContext(),"OK.", Toast.LENGTH_SHORT).show();*/
            		
            		/***aqui empieza lo de PHP**/
            		ArrayList parametros = new ArrayList();
            		parametros.add("Nombre");
                	parametros.add(nombre.getText().toString());
                	parametros.add("Apellidos");
                	parametros.add(apellidos.getText().toString());
                	parametros.add("Correo");
                	parametros.add(correo.getText().toString());
                	parametros.add("Usuario");
                	parametros.add(user.getText().toString());
                	parametros.add("Contrasena");
                	parametros.add(pass.getText().toString());
                	// Llamada a Servidor Web PHP
                	try {
                	     Post post = new Post();
                	     JSONArray datos = post.getServerData(parametros,"http://alirio.dyndns.org:81/registro.php");
                	if (datos != null && datos.length() > 0) {
                	                        JSONObject json_data = datos.getJSONObject(0);
                	                        int numRegistrados = json_data.getInt("value");
                	if (numRegistrados == 1) {
                	    Toast.makeText(getBaseContext(),"Error en El registro. ", Toast.LENGTH_SHORT).show();
                		//aqui entramos a la actividad siguiente si los datos son correctos
                	                        }
                	} else {
                		 nombre.setText("");
                		 apellidos.setText("");
                		 correo.setText("");
                		 user.setText("");
                		 pass.setText("");
                	     Toast.makeText(getBaseContext(),"Usuario Registrado. ", Toast.LENGTH_SHORT).show();
                	                        }
                	} catch (Exception e) {
                	                        Toast.makeText(getBaseContext(),
                	                                                                       "Error al conectar con el servidor. ",
                	                                                                       Toast.LENGTH_SHORT).show();
                	}
                	// FIN Llamada a Servidor Web PHP
            		
            		
            		
            	}
            }
            });//fin del onclick del registro
        
	        /*******************************************************************
	    	 * fin del onclick de registro
	    	 * *****************************************************************/     
        
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

