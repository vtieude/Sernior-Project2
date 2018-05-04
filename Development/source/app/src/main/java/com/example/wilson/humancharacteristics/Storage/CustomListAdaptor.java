package com.example.wilson.humancharacteristics.Storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.bean.HumanModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wilson on 3/5/2018.
 */

public class CustomListAdaptor extends BaseAdapter implements Filterable{
    private List<HumanModel> listHuman;
    private LayoutInflater layoutInflater;
    private Context context;
    public ArrayList<Boolean> positionArray;
    public Boolean isLongClick = false;
    public List<HumanModel> containResult;
    public CustomListAdaptor(Context context,List<HumanModel> listHuman) {
        this.listHuman = listHuman;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        positionArray = new ArrayList<Boolean>(listHuman.size());
        for(int i =0;i<listHuman.size();i++){
            positionArray.add(false);
        }
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_item_human, null);
            holder = new ViewHolder();
            holder.flagView = (ImageView)view.findViewById(R.id.imageView_flag);
            holder.nameView = (TextView)view.findViewById(R.id.item_name);
            holder.ageView = (TextView)view.findViewById(R.id.item_age);
            holder.isButtondeleted = (CheckBox)view.findViewById(R.id.check_delete_item);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
            holder.isButtondeleted.setOnCheckedChangeListener(null);
        }
        if (isLongClick) {
            holder.isButtondeleted.setVisibility(View.VISIBLE);
        }
        else  {
            holder.isButtondeleted.setVisibility(View.INVISIBLE);
        }
        HumanModel human = this.listHuman.get(i);
        holder.isButtondeleted.setChecked(positionArray.get(i));
        holder.isButtondeleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                positionArray.set(i,isChecked);
            }
        });
        holder.nameView.setText(context.getString(R.string.name)+ ": " +human.getName());
        holder.ageView.setText(context.getString(R.string.attractiveness) + ": " + human.getAttracttive());
        if (human.getImage() != null) {
            Bitmap bitmap= BitmapFactory.decodeByteArray(human.getImage(), 0, human.getImage().length);
            holder.flagView.setImageBitmap(bitmap);
        }

        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<HumanModel> results = new ArrayList<HumanModel>();
                if (containResult == null) {
                    containResult = listHuman;
                }
                if (constraint != null) {
                    if (containResult !=null && containResult.size() >0 ){
                        for (final HumanModel g:containResult) {
                            if (g.getName().toLowerCase().contains(constraint.toString()))results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listHuman = (ArrayList<HumanModel>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder {
        ImageView flagView;
        TextView nameView;
        TextView ageView;
        CheckBox isButtondeleted;
    }
}
