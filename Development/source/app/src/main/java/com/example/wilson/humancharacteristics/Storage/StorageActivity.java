package com.example.wilson.humancharacteristics.Storage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wilson.humancharacteristics.HumanInformation.HumanInformationActivity;
import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.bean.HumanModel;

import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity {

    private List<HumanModel> listHuman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        List<HumanModel> image_Details = this.getListData();
        final ListView listView = (ListView)findViewById(R.id.list_human);
        listView.setAdapter(new CustomListAdaptor(this,image_Details));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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
        list.add(vietnam);
        list.add(usa);
        list.add(russia);
        return list;
    }
}
