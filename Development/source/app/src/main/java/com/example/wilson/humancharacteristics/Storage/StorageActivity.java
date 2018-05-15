package com.example.wilson.humancharacteristics.Storage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wilson.humancharacteristics.Author.AuthorInformationActivity;
import com.example.wilson.humancharacteristics.CameraDetect.CameraDetectActivity;
import com.example.wilson.humancharacteristics.HumanInformation.HumanInformationActivity;
import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.Setting.SettingActivity;
import com.example.wilson.humancharacteristics.bean.HumanDatabaseHelper;
import com.example.wilson.humancharacteristics.bean.HumanModel;

import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private List<HumanModel> listHuman = new ArrayList<HumanModel>();
    private ListView listView;
    public CustomListAdaptor customListAdaptor;
    public HumanDatabaseHelper databaseHelper;
    private MyasyncListview myasyncListview;
    private DrawerLayout drawerLayout;
    private Menu menu;
    private MenuItem menuItemDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
//         that get value from setting page
//        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this);
////        int a = setting.getInt("amount_face", 0);
//        databaseHelper = new HumanDatabaseHelper(this);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Storage");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_start, R.string.navigation_start);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        myasyncListview = new MyasyncListview(this);
//        databaseHelper.createDefaultValue();
        myasyncListview.execute();
        Menu menuNav = navigationView.getMenu();
        MenuItem navitem= menuNav.findItem(R.id.storage_homepage);
        navitem.setEnabled(false);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            if (myasyncListview.customListAdaptor.isLongClick) {
                myasyncListview.customListAdaptor.isLongClick = ! myasyncListview.customListAdaptor.isLongClick;

                menuItemDelete.setTitle(R.string.select_more);
            }
            else {
                super.onBackPressed();
                finish();
            }
            return true; //I have tried here true also
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        databaseHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_storage, menu);
        this.menu = menu;
        menuItemDelete = menu.findItem(R.id.delete_storage);
        myasyncListview.menuItem = menuItemDelete;
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_storage:
                if (myasyncListview.customListAdaptor.isLongClick) {
                    for (int i =0 ; i < myasyncListview.customListAdaptor.positionArray.size(); i ++) {
                        if (myasyncListview.customListAdaptor.positionArray.get(i) && myasyncListview.customListAdaptor.isLongClick) {
                            myasyncListview.deleteItemCheckbox(item);
                            return  true;
                        }
                    }
                    item.setTitle(R.string.select_more);
                    Toast.makeText(StorageActivity.this,  "Please select item delete",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    item.setTitle(R.string.delete);
                    myasyncListview.customListAdaptor.isLongClick = true;
                    myasyncListview.customListAdaptor.notifyDataSetChanged();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent myIntent = null;
        switch (item.getItemId()) {
            case R.id.camera_start:
                super.onBackPressed();
                break;
            case R.id.storage_homepage:
                break;
            case R.id.setting_homepage:
                myIntent = new Intent(this, SettingActivity.class);
                this.startActivity(myIntent);
                finish();
                break;
            case R.id.exit_homepage:
                this.finishAffinity(); System.exit(0);
                break;
            case R.id.author_homepage:
                myIntent = new Intent(this, AuthorInformationActivity.class);
                this.startActivity(myIntent);
                finish();
                break;
            default: break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
}
