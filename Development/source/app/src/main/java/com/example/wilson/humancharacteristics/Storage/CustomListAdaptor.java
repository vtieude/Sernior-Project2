package com.example.wilson.humancharacteristics.Storage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wilson.humancharacteristics.R;

import java.util.List;

/**
 * Created by Wilson on 3/5/2018.
 */

public class CustomListAdaptor extends BaseAdapter{
    private List<HumanModel> listHuman;
    private LayoutInflater layoutInflater;
    private Context context;
    public CustomListAdaptor(Context context,List<HumanModel> listHuman) {
        this.listHuman = listHuman;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listHuman.size();
    }

    @Override
    public Object getItem(int i) {
        return listHuman.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_item_human, null);
            holder = new ViewHolder();
            holder.flagView = (ImageView)view.findViewById(R.id.imageView_flag);
            holder.nameView = (TextView)view.findViewById(R.id.text_name);
            holder.ageView = (TextView)view.findViewById(R.id.text_age);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }
        HumanModel human = this.listHuman.get(i);
        holder.nameView.setText("Name: " +human.getName());
        holder.ageView.setText("Age: " + human.getAge());
        int imageId = getMipmapResIdByName(human.getFlagImage());
        holder.flagView.setImageResource(imageId);
        return view;
    }
    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();

        // Trả về 0 nếu không tìm thấy.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }

    static class ViewHolder {
        ImageView flagView;
        TextView nameView;
        TextView ageView;
    }

}
