package dev.android.bruno.appcoordenadasgps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] permissoesRequiridas = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CONTACTS};

    public static final int APP_PERMISSOES_ID = 2021;


    TextView txtValorLatitude, txtValorLongitude;

    double latitude, longitude;

    LocationManager locationManager;

    boolean gpsAtivo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtValorLatitude = findViewById(R.id.txtValorLatitude);
        txtValorLongitude = findViewById(R.id.txtValorLongitude);


        locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);

        gpsAtivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (gpsAtivo) {

            obterCoordenadas();

        } else {

            latitude = 0.00;
            longitude = 0.00;

            txtValorLatitude.setText(String.valueOf(latitude));
            txtValorLongitude.setText(String.valueOf(longitude));

            Toast.makeText(this,
                    "Coordenadas não Disponíveis", Toast.LENGTH_LONG).show();
        }

    }

    private void obterCoordenadas() {

        boolean permissaoAtiva = solicitarPermissaoParaObterLocalizacao();

        if (permissaoAtiva) {

            capturarUltimaLocalizacaoValida();

        }


    }

    private boolean solicitarPermissaoParaObterLocalizacao() {

        Toast.makeText(this,
                "Aplicativo não tem permissão....", Toast.LENGTH_LONG).show();

        List<String> permissoesNegadas = new ArrayList<>();

        int permissaoNegada;

        for (String permissao : this.permissoesRequiridas) {

            permissaoNegada = ContextCompat.checkSelfPermission(MainActivity.this, permissao);

            if (permissaoNegada != PackageManager.PERMISSION_GRANTED) {
                permissoesNegadas.add(permissao);
            }

        }

        if (!permissoesNegadas.isEmpty()) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    permissoesNegadas.toArray(new String[permissoesNegadas.size()]),
                    APP_PERMISSOES_ID);

            return false;
        } else {
            return true;
        }

    }

    private void capturarUltimaLocalizacaoValida() {


        @SuppressLint("MissingPermission")
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null){

            latitude = location.getLatitude();
            longitude = location.getLongitude();

        }else{

            latitude = 0.00;
            longitude = 0.00;

        }

        txtValorLatitude.setText(formatarGeopoint(latitude));
        txtValorLongitude.setText(formatarGeopoint(longitude));

        Toast.makeText(this,
                "Coordenadas obtidas com sucesso", Toast.LENGTH_LONG).show();


    }

    private String formatarGeopoint(double valor){

        DecimalFormat decimalFormat = new DecimalFormat("#.####");

        return decimalFormat.format(valor);

    }


}