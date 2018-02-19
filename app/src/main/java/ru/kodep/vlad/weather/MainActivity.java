package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Objects;

import ru.kodep.vlad.weather.entity.GuestProvaider;
import ru.kodep.vlad.weather.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    Handler handler;
    GuestProvaider guestProvaider;
    MainFragment mainFragment;
    private GeoLocation geo;
    public int onGeo;


    @SuppressLint({"CommitTransaction", "WrongViewCast", "NewApi", "CutPasteId", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        guestProvaider = new GuestProvaider(this);
        guestProvaider.open();
        if (savedInstanceState == null)
            addMainFragment();
    }

    private void addMainFragment() {
        mainFragment = new MainFragment();
        getSupportFragmentManager()
                .addOnBackStackChangedListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, mainFragment)
                .commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) showInputDialog();
        if (item.getItemId() == R.id.action_updater) {
            geo = new GeoLocation(getSystemService(LocationManager.class), mainFragment);
            onGeo = 1;
            MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.mainFragment);
            fragment.visibleProgressBar();
        }
        return true;
    }


    private void showInputDialog() {
        AlertDialog.Builder chooseCity = new AlertDialog.Builder(this);
        chooseCity.setIcon(R.mipmap.ic_launcher);
        chooseCity.setTitle(R.string.choose_city);
        final EditText input = new EditText(this);
        input.setHint("Moscow");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        chooseCity.setView(input);
        chooseCity.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!Objects.equals(input.getText().toString(), "")) {
                    String city = input.getText().toString();
                    MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.mainFragment);
                    fragment.visibleProgressBar();
                    fragment.weatherAroundTheCity(city);

                }
            }
        });
        chooseCity.show();
    }

    @Override
    public void onBackStackChanged() {
        Log.i("BACKSTACK", " mainFragment сработало");
    }

    @SuppressLint("NewApi")
    @Override
    protected void onStop() {
        super.onStop();
        if (geo != null)
            geo.unsubscribe();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("onGeo", onGeo);


    }

    @SuppressLint("NewApi")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onGeo = savedInstanceState.getInt("onGeo");
        if (onGeo == 1) {
            MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.mainFragment);
            geo = new GeoLocation(getSystemService(LocationManager.class), fragment);
            fragment.visibleProgressBar();
        }
    }


}

