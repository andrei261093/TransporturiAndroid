package com.iorga.andre.transporturiromania_spania;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andre on 04.12.2016.
 */

public class FcmInstanceIdService extends FirebaseInstanceIdService{
    String app_server_url = "http://transporturirosiori.go.ro:8000/token";

    @Override
    public void onTokenRefresh() {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.FCM_TOKEN), recent_token);
        editor.commit();
        final String username = sharedPreferences.getString(getString(R.string.USERNAME), "");
        sendTokenToServerDatabase(recent_token, username);
    }

    private void sendTokenToServerDatabase(String recent_token, String username){

        final String token = recent_token;
        final String user_name = username;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app_server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Nu s-a putut trimite token-ul!", Toast.LENGTH_LONG).show();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("fcm_token", token);
                params.put("fcm_token", user_name);

                return params;
            }
        };
        MySingleton.getmInstance(this).addToRequestque(stringRequest);
    }
}
