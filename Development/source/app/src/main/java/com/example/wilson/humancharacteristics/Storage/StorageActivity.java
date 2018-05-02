package com.example.wilson.humancharacteristics.Storage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wilson.humancharacteristics.HumanInformation.HumanInformationActivity;
import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.bean.HumanDatabaseHelper;
import com.example.wilson.humancharacteristics.bean.HumanModel;

import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity {

    private List<HumanModel> listHuman = new ArrayList<HumanModel>();
    private ListView listView;
    public CustomListAdaptor customListAdaptor;
    public HumanDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        // that get value from setting page
        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this);
        int a = setting.getInt("amount_face", 0);
        databaseHelper = new HumanDatabaseHelper(this);
//        databaseHelper.createDefaultValue();
        listHuman = databaseHelper.getListHuman();
        listView = (ListView) findViewById(R.id.list_human);
        customListAdaptor = new CustomListAdaptor(this,listHuman);
        listView.setAdapter(customListAdaptor);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Storage");
        setOnItemClick();
        setItemLongClick();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_storage, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_storage:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void setItemLongClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                customListAdaptor.isLongClick  = !customListAdaptor.isLongClick;
                customListAdaptor.notifyDataSetChanged();
                return true;
            }
        });
    }
    public void setOnItemClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(StorageActivity.this,  "das" + i,
//                        Toast.LENGTH_LONG).show();
                if (customListAdaptor.isLongClick) {
                    CheckBox cb = (CheckBox)view.findViewById(R.id.check_delete_item);
                    boolean isCheck = !cb.isChecked();
                    cb.setChecked(isCheck);
                }
                else
                {
                    Intent intent = new Intent(getApplication(), HumanInformationActivity.class).putExtra("human", customListAdaptor.getItem(i));
                    startActivity(intent);

                    finish();
                }


            };
        });
    }
}
