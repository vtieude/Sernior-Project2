package com.example.wilson.humancharacteristics.Storage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wilson.humancharacteristics.HumanInformation.HumanInformationActivity;
import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.bean.HumanModel;

import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity {

    private List<HumanModel> listHuman;
    private ListView listView;
    public CustomListAdaptor customListAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        List<HumanModel> image_Details = this.getListData();
        listView = (ListView) findViewById(R.id.list_human);
        customListAdaptor = new CustomListAdaptor(this,image_Details);
        listView.setAdapter(customListAdaptor);
        setOnItemClick();
        setItemLongClick();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_storage, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_delete_storage) {
            Toast.makeText(this, "back", Toast.LENGTH_LONG).show();
            return true;
        }

        if (id == R.id.menu_delete_storage) {
            Toast.makeText(this, "Setting", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setItemLongClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                customListAdaptor.cb = !customListAdaptor.cb;
                customListAdaptor.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), customListAdaptor.cb.toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    public void setOnItemClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Click one item", Toast.LENGTH_SHORT).show();
                Object o = listView.getItemAtPosition(i);
                HumanModel humanModel = (HumanModel)o;
                Intent intent = new Intent(StorageActivity.this, HumanInformationActivity.class).putExtra("human", humanModel);
                startActivity(intent);
            };
        });
    }
    public void selectListItemClick(){
    }
    private  List<HumanModel> getListData(){
        List<HumanModel> list = new ArrayList<HumanModel>();
        HumanModel vietnam = new HumanModel("Vietnam", "so1", 18);
        HumanModel usa = new HumanModel("United States", "so2", 23);
        HumanModel russia = new HumanModel("Russia", "so3", 44);
        HumanModel tbale = new HumanModel("KaKa", "so1", 38);
        HumanModel us333a = new HumanModel("Nmaemem", "so2", 23);
        HumanModel rus44sia = new HumanModel("RKaKao", "so3", 42);
        HumanModel kaka555 = new HumanModel("Lazio", "so1", 28);
        HumanModel usasa = new HumanModel("Motoro", "so2", 22);
        HumanModel russiafee= new HumanModel("Conggo", "so3", 24);
        list.add(vietnam);
        list.add(usa);
        list.add(russia);
        list.add(tbale);
        list.add(us333a);
        list.add(rus44sia);
        list.add(kaka555);
        list.add(usasa);
        list.add(russiafee);
        return list;
    }
}
