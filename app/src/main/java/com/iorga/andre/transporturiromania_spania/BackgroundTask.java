package com.iorga.andre.transporturiromania_spania;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Andrei on 07.12.2016.
 */

public class BackgroundTask extends AsyncTask<Void, Void, String>{

    String json_url;
    String json_string;
    TextView textView;
    ReservationsList reservationsList;

    public BackgroundTask(ReservationsList reservationsList, TextView textView) {
        this.json_string = reservationsList.getJsonString();
        this.textView = textView;
        this.reservationsList = reservationsList;

    }

    @Override
    protected void onPreExecute() {

        json_url = "http://transporturirosiori.go.ro:8000/reservations/andrei";
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {
            URL url = new URL(json_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));



            StringBuilder stringBuilder = new StringBuilder();

            while((json_string=bufferedReader.readLine())!=null){
                stringBuilder.append(json_string+"\n");
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            json_string = stringBuilder.toString();

            return json_string;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json_string;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        setData(result);

        reservationsList.showList(result);
    }

    private void setData(String data){
        json_string = data;
    }
}
