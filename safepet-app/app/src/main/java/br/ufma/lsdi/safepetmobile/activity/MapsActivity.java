
package br.ufma.lsdi.safepetmobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufma.lsdi.safepetmobile.R;
import br.ufma.lsdi.safepetmobile.ServicesSQLite.PetServiceSQLite;
import br.ufma.lsdi.safepetmobile.ServicesSQLite.TutorServiceSQLite;
import br.ufma.lsdi.safepetmobile.databaseSQLite.DatabaseHelper;
import br.ufma.lsdi.safepetmobile.domain.Pet;
import br.ufma.lsdi.safepetmobile.domain.Tutor;
import br.ufma.lsdi.safepetmobile.mqtt.BrokerMqtt;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MqttAndroidClient client;
    private Map<Integer, List<LatLng>> locations;
    private Map<Integer, Marker> markers;
    private Map<Integer, Integer> contMarkers;
    private Map<Integer, List<Marker>> hashMapListMarkers;
    private Map<Integer, Polyline> polylines;

    private Toolbar toolbar;

    private DatabaseHelper databaseHelper;

    private List<Pet> pets;
    private Tutor tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Mapa");
        setSupportActionBar(this.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(onClick -> finish());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        markers = new HashMap<>();
        hashMapListMarkers = new HashMap<>();
        polylines = new HashMap<>();
        contMarkers = new HashMap<>();

        databaseHelper = new DatabaseHelper(this);
        pets = PetServiceSQLite.getPets(databaseHelper);
        tutor = TutorServiceSQLite.getTutor(databaseHelper);

//        pets.forEach(pet -> contMarkers.put(pet.getIdColeira(), 0));


//        contMarkers.put(71, 0);
//        contMarkers.put(72, 0);


        locations = new HashMap<>();
        client = BrokerMqtt.getClient();

//        for(int i = 1; i <= 100; i++) {
//            try {
//                contMarkers.put(i, 0);
//                String payload = +i+";200;-5.512638352723324,-43.21173949860797";
//                client.publish("create/epl", new MqttMessage(payload.getBytes()));
//            } catch (MqttException e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLngHome = new LatLng(tutor.getSafePlace().getLatitude(), tutor.getSafePlace().getLongitude());

        //Home
        mMap.addMarker(new MarkerOptions()
                .title("Home")
                .position(latLngHome)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.home))
        );

        pets.forEach(pet -> {
            contMarkers.put(pet.getIdColeira(), 0);
            mMap.addCircle(new CircleOptions()
                    .center(latLngHome)
                    .radius(pet.getRadiusInMeters())
                    .strokeColor(Color.GREEN)
                    .strokeWidth(5));
        });

        // Add a marker in Sydney and move the camera
        LatLng matoesCity = new LatLng(-5.5205377037365375, -43.20400404132213);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(matoesCity));
        mMap.setMinZoomPreference(14.0f);
        

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String[] payload = new String(message.getPayload()).split(";");

                String eventType = payload[0];
                Integer idPet = new Integer(payload[1]);
                Double latitude = new Double(payload[2]);
                Double longitude = new Double(payload[3]);
                Double distance = new Double(payload[4]);


                Marker markerOfPet = markers.get(idPet);
                if(markerOfPet != null) {


                    if(contMarkers.get(idPet) == 0) {
                        String dateHora = dateFormatter(Calendar.getInstance().getTime());

                        markerOfPet.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.point));
                        markerOfPet.setTitle(String.format("%.2f metros - %s", distance, dateHora));

                        List<Marker> listMarkersOfPet = hashMapListMarkers.get(idPet);
                        if (listMarkersOfPet == null) {
                            listMarkersOfPet = new ArrayList<>();
                        }

                        listMarkersOfPet.add(markerOfPet);
                        hashMapListMarkers.put(idPet, listMarkersOfPet);

                        contMarkers.put(idPet, 4);

                    } else {
                        markerOfPet.remove();
                    }
                    contMarkers.put(idPet, contMarkers.get(idPet) - 1);
                }

                Polyline polylineOfPet = polylines.get(idPet);
                if(polylineOfPet != null) {
                    polylineOfPet.remove();
                }

                List<LatLng> locationsOfPet = locations.get(idPet);
                if(locationsOfPet == null) {
                    locations.put(idPet, new ArrayList<>());
                } else {
                    LatLng latLng = new LatLng(latitude, longitude);

                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Dog-"+idPet)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.dog))
                    );


                    markers.put(idPet, marker);


                    if(eventType.equals("out")) {
                        locationsOfPet.add(latLng);
                    } else {
                        locationsOfPet.clear();

                        List<Marker> listMarkersOfPet = hashMapListMarkers.get(idPet);
                        listMarkersOfPet.forEach(m -> m.remove());
                        listMarkersOfPet.clear();
                    }

                    runOnUiThread(() -> {

                        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                                .clickable(true)
                                .addAll(locationsOfPet)
                        );

                        polyline.setColor(Color.RED);
                        polylines.put(idPet, polyline);

                    });
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

    public String dateFormatter(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}

