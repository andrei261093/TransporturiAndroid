package com.iorga.andre.transporturiromania_spania;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrei on 07.12.2016.
 */

public class ReservationAdapter extends ArrayAdapter {
    String url = "http://transporturirosiori.go.ro:8000/test";
    List<Reservation> list = new ArrayList<>();
    int serverResponceCode = 0;

    public ReservationAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Reservation object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Reservation getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;

        row = convertView;
        final ReservationHolder reservationHolder;

        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout, parent, false);
            reservationHolder = new ReservationHolder();

            reservationHolder.tx_name = (TextView) row.findViewById(R.id.tx_name);
            reservationHolder.tx_location = (TextView) row.findViewById(R.id.tx_location);
            reservationHolder.tx_phone = (TextView) row.findViewById(R.id.tx_phone);
            reservationHolder.checkBox = (CheckBox) row.findViewById(R.id.checkBox);
            reservationHolder.callButton = (Button) row.findViewById(R.id.button);

            row.setTag(reservationHolder);

        } else {
            reservationHolder = (ReservationHolder) row.getTag();
        }

        final Reservation reservation = (Reservation) this.getItem(position);
        reservationHolder.tx_name.setText(reservation.getName());
        reservationHolder.tx_phone.setText(reservation.getPhone());
        reservationHolder.tx_location.setText(reservation.getLocation());

        if (reservation.hasBeenCalled) {
            reservationHolder.checkBox.setChecked(true);
        } else {
            reservationHolder.checkBox.setChecked(false);
        }

        reservationHolder.callButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String number = reservation.getPhone();
                Uri call = Uri.parse("tel:" + number);
                Intent surf = new Intent(Intent.ACTION_DIAL, call);
                getContext().startActivity(surf);
            }
        });

        reservationHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String isCalled;
                serverResponceCode = 0;

                if (reservationHolder.checkBox.isChecked()) {
                    isCalled = "1";
                } else {
                    isCalled = "0";
                }

                String param = "id=" + reservation.getId() + "&hasBeenCalled=" + isCalled;
                MarkAsCalledAsync markAsCalledAsync = new MarkAsCalledAsync(param, reservation.getName());
                markAsCalledAsync.execute();
            }
        });


        return row;
    }

    static class ReservationHolder {
        TextView tx_name, tx_location, tx_phone;
        CheckBox checkBox;
        Button callButton;
    }


    //Send POST
    public class MarkAsCalledAsync extends AsyncTask<Void, Void, String> {
        String param;
        String userName;

        public MarkAsCalledAsync(String param, String userName) {
            this.param = param;
            this.userName = userName;

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                sendPost(param);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Sent!";
        }

        @Override
        protected void onPostExecute(String s) {
            if (serverResponceCode == 200) {
                Toast.makeText(getContext(), "Salvat cu succes! " + userName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Erroare la " + userName + "! Server Respose Code: " + serverResponceCode, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendPost(String param) throws Exception {

        //Your server URL

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        con.setDoOutput(true);// Should be part of code only for .Net web-services else no need for PHP
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(param);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + param);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        serverResponceCode = responseCode;

        //print result
        System.out.println(response.toString());

    }

}
