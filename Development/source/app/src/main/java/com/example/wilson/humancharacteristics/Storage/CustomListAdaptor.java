package com.example.wilson.humancharacteristics.Storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.bean.HumanModel;

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
    public HumanModel getItem(int i) {
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
            holder.nameView = (TextView)view.findViewById(R.id.item_name);
            holder.ageView = (TextView)view.findViewById(R.id.item_age);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }
        HumanModel human = this.listHuman.get(i);
        holder.nameView.setText("Name: " +human.getName());
        holder.ageView.setText("Age: " + human.getAttracttive());
        if (human.getImage() != null) {
            Bitmap bitmap= BitmapFactory.decodeByteArray(human.getImage(), 0, human.getImage().length);
            holder.flagView.setImageBitmap(bitmap);
        }
        return view;
    }
    static class ViewHolder {
        ImageView flagView;
        TextView nameView;
        TextView ageView;
    }
}
