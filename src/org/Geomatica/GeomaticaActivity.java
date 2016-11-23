	package org.Geomatica;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;


public class GeomaticaActivity extends Activity {
    /** Called when the activity is first created. */
	EditText user;
	EditText pass;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button ingresar =(Button) findViewById(R.id.ingreso);
        Button registrar =(Button) findViewById(R.id.registro);
        user = (EditText)findViewById(R.id.user);
        pass =(EditText) findViewById(R.id.pass);
        ingresar.setOnClickListener(new View.OnClickListener(){

        public void onClick(View view){

        // Código a ejecutar al pulsar el botón
        	
        	
        	ArrayList parametros = new ArrayList();
        	parametros.add("Usuario");
        	parametros.add(user.getText().toString());
        	parametros.add("Contrasena");
        	parametros.add(pass.getText().toString());
        	// Llamada a Servidor Web PHP
        	try {
        	                        Post post = new Post();
        	                        JSONArray datos = post.getServerData(parametros,
        	                                                                       "http://186.85.50.202:81/login.php");
        	// No se puede poner localhost, carga la consola de Windows
        	// y escribe ipconfig/all para ver tu IP
        	if (datos != null && datos.length() > 0) {
        	                        JSONObject json_data = datos.getJSONObject(0);
        	                        int numRegistrados = json_data.getInt("ID_USUARIO");
        	if (numRegistrados > 0) {
        	    /*Toast.makeText(getBaseContext(),"Usuario correcto. ", Toast.LENGTH_SHORT).show();*/
        		//aqui entramos a la actividad siguiente si los datos son correctos
	        		Intent intent = new Intent(GeomaticaActivity.this, Entrada.class);
	        		intent.putExtra("id",numRegistrados);
	        		startActivity(intent);
        	                        }
        	} else {
        	                        Toast.makeText(getBaseContext(),
        	                                                                       "Usuario incorrecto. ", Toast.LENGTH_SHORT)
        	                                                                                              .show();
        	                        }
        	} catch (Exception e) {
        	                        Toast.makeText(getBaseContext(),
        	                                                                       "Error al conectar con el servidor. ",
        	                                                                       Toast.LENGTH_SHORT).show();
        	}
        	// FIN Llamada a Servidor Web PHP
        	                                               }

        	                        });//fin del metodo de onClick de validacion credenciales
        
        
        registrar.setOnClickListener(new View.OnClickListener(){

        	/******************************************************************
        	 * este onclick que sigue es para el registro
        	 * ***************************************************************/
        	
            public void onClick(View view){
            	Intent intent = new Intent(GeomaticaActivity.this, Registro.class);
        		startActivity(intent);
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


/*if(user.getText().toString().equals("ali") && pass.getText().toString().equals("ali")){
	Toast.makeText(GeomaticaActivity.this, "Muy BIEN", Toast.LENGTH_SHORT).show();
}
else{
	Toast.makeText(GeomaticaActivity.this, "NO MAL REMAL...", Toast.LENGTH_SHORT).show();
}*/
