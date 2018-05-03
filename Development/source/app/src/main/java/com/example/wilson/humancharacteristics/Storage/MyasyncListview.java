package com.example.wilson.humancharacteristics.Storage;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wilson.humancharacteristics.HumanInformation.HumanInformationActivity;
import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.bean.HumanDatabaseHelper;
import com.example.wilson.humancharacteristics.bean.HumanModel;
import com.example.wilson.humancharacteristics.model.FaceResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wilson on 5/2/2018.
 */

public class MyasyncListview extends AsyncTask<Void, HumanModel, Void> {
    private Activity myactivity;
    private List<HumanModel> listHuman = new ArrayList<HumanModel>();
    private ListView listView;
    public CustomListAdaptor customListAdaptor;
    public MyasyncListview(Activity activity) {
        this.myactivity = activity;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listView = myactivity.findViewById(R.id.list_human);
        customListAdaptor = new CustomListAdaptor(myactivity,listHuman);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        setOnItemClick();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HumanDatabaseHelper humanDatabaseHelper = new HumanDatabaseHelper(myactivity);
        List<HumanModel> listHumanLayout = new ArrayList<HumanModel>();
        listHumanLayout = humanDatabaseHelper.getListHuman();
        for (int i = 0; i < listHumanLayout.size(); i++) {
            publishProgress(listHumanLayout.get(i));
            SystemClock.sleep(10);
        }

        humanDatabaseHelper.close();
        return null;
    }

    @Override
    protected void onProgressUpdate(HumanModel... values) {
        super.onProgressUpdate(values);
        listHuman.add(values[0]);
        listView.setAdapter(customListAdaptor);
        customListAdaptor.notifyDataSetInvalidated();
    }
    //    @Override
//    protected void onProgressUpdate(HumanModel values) {
//        super.onProgressUpdate(values);
//
//        listView.setAdapter(customListAdaptor);
////        setItemLongClick();
//
//    }
    //    @Override
//    protected Void doInBackground() {
//
//        listHuman = humanDatabaseHelpers();
//        listView = (ListView)myactivity.findViewById(R.id.list_human);
//        customListAdaptor = new CustomListAdaptor(myactivity,listHuman);
//        listView.setAdapter(customListAdaptor);
//        return null;
//    }

    public void setItemLongClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
//                customListAdaptor.isLongClick  = !customListAdaptor.isLongClick;
//                customListAdaptor.notifyDataSetChanged();
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
//                if (customListAdaptor.isLongClick) {
//                    CheckBox cb = (CheckBox)view.findViewById(R.id.check_delete_item);
//                    boolean isCheck = !cb.isChecked();
//                    cb.setChecked(isCheck);
//                }
//                else
////                {
                    Intent intent = new Intent(myactivity, HumanInformationActivity.class).putExtra("human", customListAdaptor.getItem(i));
                    myactivity.startActivity(intent);
                    myactivity.finish();
////                }
            };
        });
    }


}
