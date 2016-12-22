package com.iorga.andre.transporturiromania_spania;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ReservationsList extends AppCompatActivity {


    JSONArray jsonArray;
    JSONObject jsonObject;
    ReservationAdapter reservationAdapter;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations_list);

        reservationAdapter = new ReservationAdapter(this, R.layout.row_layout);

        listView = (ListView) findViewById(R.id.reservationListView);

        getDataFromServer();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getBaseContext(), "click", Toast.LENGTH_SHORT).show();
            }

        });
        listView.setItemsCanFocus(true);

    }

    public void getDataFromServer() {
        ReservationListViewBuilderAsync reservationListViewBuilderAsync = new ReservationListViewBuilderAsync(this);

        reservationListViewBuilderAsync.execute();
    }

    public void showList(String jsonString) {
        if (jsonString == null) {
            Toast.makeText(this, "Nu s-a putut conecta la server!", Toast.LENGTH_LONG).show();
        } else {
            listView.setAdapter(reservationAdapter);

            try {
                jsonObject = new JSONObject();
                jsonArray = new JSONArray(jsonString);
                int count = 0;

                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    Reservation reservation = new Reservation(jo.getString("name"), jo.getString("location"), jo.getString("telephone"), jo.getString("id"), jo.getString("hasBeenCalled"));
                    reservationAdapter.add(reservation);
                    count++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
