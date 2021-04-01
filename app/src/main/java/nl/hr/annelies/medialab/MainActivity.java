package nl.hr.annelies.medialab;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private GoogleMap mMap;
    private String markerIcon = "https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png";
    Hotspot[] hotspots = new Hotspot[3] ;






//    hotspots[0] = new Hotspot("strandwacht", new LatLng(-32, 138.2), "hallo");
//    {name: "hoi", location: new LatLng(32, 42), message: "lol"}];

//    List<{name: String, location: LatLng, message: String}> hotspots = [{name: "strandwacht", location: new LatLng(-32, 138.2), message: "hallo"}];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hotspots[0] = new Hotspot("strandwacht", new LatLng(52.11463474012185, 4.280247697237467), "hallo");
        hotspots[1] = new Hotspot("pier", new LatLng(52.11796848556339, 4.280011749484001), "pier");
        hotspots[2] = new Hotspot("restaurantje ofzo", new LatLng(52.11224552563259, 4.278095452177875), "restaurantje denk ik");
//        hotspots[1] = new Hotspot("informatie punt", new LatLng(52.11796848556339, 4.280011749484001), "info enzo");

        // request permission for recording audio
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION}, 200);



        // map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        fetchLocation();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // mapp stuffffff

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("no permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    if(currentLocation != null) {
                        Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        assert supportMapFragment != null;
                        supportMapFragment.getMapAsync(MainActivity.this);
                    }
                }
            }
        });
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("Setmylocationenabled trueee");
            mMap.setMyLocationEnabled(true);
        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show user a dialog for displaying why the permission is needed and then ask for the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            }
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableUserLocation();

        if(currentLocation != null) {
            mMap.getUiSettings().setCompassEnabled(true);

            // user
            LatLng userLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
//            MarkerOptions markOptUser = new MarkerOptions().position(userLocation).title("Huidige locatie").icon(BitmapDescriptorFactory.fromResource(R.drawable.rudy_grey));
//            mMap.addMarker(markOptUser);

            // hotspots
            System.out.println(hotspots);
            for(int i = 0; i < hotspots.length; i++) {
                System.out.println(hotspots[i]);
                String name = "markHotspot" + i;
                mMap.addMarker(new MarkerOptions().position(hotspots[i].location).title("Hotspot " + i).icon(BitmapDescriptorFactory.fromResource(R.drawable.rudy_grey_small)));
            }
//            MarkerOptions markOptStrWacht= new MarkerOptions().position(userLocation).title("Huidige locatie").icon(BitmapDescriptorFactory.fromResource(R.drawable.rudy_colored));
        }

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

//
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(userLocation));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 1));
    }
}

class Hotspot {
    String name;
    LatLng location;
    String message;

    Hotspot(String n, LatLng l, String m) {
        name = n;
        location = l;
        message = m;
    }
};
