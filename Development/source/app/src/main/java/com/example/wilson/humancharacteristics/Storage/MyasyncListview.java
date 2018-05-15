package com.example.wilson.humancharacteristics.Storage;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

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

public class MyasyncListview extends AsyncTask<Void, HumanModel, Void> implements SearchView.OnQueryTextListener {
    private Activity myactivity;
    private List<HumanModel> listHuman = new ArrayList<HumanModel>();
    private ListView listView;
    private Boolean checkClickDelete =false;
    public CustomListAdaptor customListAdaptor;
    private SearchView searchNameHuman;
    public MenuItem menuItem;
    public MyasyncListview(Activity activity) {
        this.myactivity = activity;

    }
    public Boolean deleteItemCheckbox(final MenuItem item) {

        AlertDialog.Builder adb=new AlertDialog.Builder(myactivity);
        adb.setTitle("Delete?");
        adb.setMessage("Are you sure you want to delete ");
        adb.setNegativeButton("Cancel", null);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0 ; i < customListAdaptor.positionArray.size(); i++) {
                    if (customListAdaptor.positionArray.get(i)) {
                        HumanDatabaseHelper humanDatabaseHelper = new HumanDatabaseHelper(myactivity);
                        humanDatabaseHelper.deleteHuman(customListAdaptor.listHuman.get(i).getId());
                        customListAdaptor.containResult.remove(customListAdaptor.listHuman.get(i));
                        humanDatabaseHelper.close();
                    }
                }
                customListAdaptor.positionArray.clear();
                customListAdaptor.listHuman.clear();
                customListAdaptor.listHuman.addAll(customListAdaptor.containResult);
                for (int j =0; j < customListAdaptor.listHuman.size(); j++) {
                    customListAdaptor.positionArray.add(false);
                }
                item.setTitle(myactivity.getString(R.string.select_more));
                customListAdaptor.isLongClick = false;
                customListAdaptor.notifyDataSetChanged();
                checkClickDelete = true;
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkClickDelete =false;
                dialog.cancel();
            }
        });
        adb.show();
        return checkClickDelete;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listView = myactivity.findViewById(R.id.list_human);
        customListAdaptor = new CustomListAdaptor(myactivity,listHuman);
        searchNameHuman = myactivity.findViewById(R.id.search_view_name);
        setupSearchView();
    }

    private void setupSearchView()
    {
        searchNameHuman.setIconifiedByDefault(false);
        searchNameHuman.setOnQueryTextListener(this);
        searchNameHuman.setSubmitButtonEnabled(true);
        searchNameHuman.setQueryHint(myactivity.getString(R.string.search_name));
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        setOnItemClick();
        setItemLongClick();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HumanDatabaseHelper humanDatabaseHelper = new HumanDatabaseHelper(myactivity);
        List<HumanModel> listHumanLayout = new ArrayList<HumanModel>();
        listHumanLayout = humanDatabaseHelper.getListHuman();
        customListAdaptor.containResult.addAll(listHumanLayout);
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
        customListAdaptor.positionArray.add(false);
        listView.setAdapter(customListAdaptor);
        customListAdaptor.notifyDataSetInvalidated();
    }
    public void setItemLongClick() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                customListAdaptor.isLongClick  = !customListAdaptor.isLongClick;
                customListAdaptor.notifyDataSetChanged();
                if (menuItem != null) {
                    if (customListAdaptor.isLongClick) {
                        menuItem.setTitle(myactivity.getString(R.string.delete));
                    }
                    else {
                        menuItem.setTitle(myactivity.getString(R.string.select_more));
                    }
                }
                return true;
            }
        });
    }
    public void setOnItemClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (customListAdaptor.isLongClick) {
                    CheckBox cb = (CheckBox)view.findViewById(R.id.check_delete_item);
                    boolean isCheck = !cb.isChecked();
                    cb.setChecked(isCheck);
                }
                else {
                    Intent intent = new Intent(myactivity, HumanInformationActivity.class).putExtra("human", customListAdaptor.getItem(i));
                    myactivity.startActivity(intent);
                    myactivity.finish();
                }
            };
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        customListAdaptor.filter(newText.toString().trim());
        listView.invalidate();
        return false;
    }
}
