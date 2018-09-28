package com.example.ali.listviewapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends BaseAdapter
{
    List<listObject> list = new ArrayList<listObject>();
    LayoutInflater mInflater;


    public ItemAdapter(Context c, List list)
    {
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    public void add(listObject object)
    {
        list.add(object);
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public listObject getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = mInflater.inflate(R.layout.customitemrow, null);
        TextView orgName = (TextView) view.findViewById(R.id.orgName);
        TextView orgNumber = (TextView) view.findViewById(R.id.orgNumber);

        String orgNameValue = list.get(position).getOrgName();
        String orgNumbValue = list.get(position).getOrgNumber();
        orgName.setText(orgNameValue);
        orgNumber.setText(orgNumbValue);

        return view;
    }
}
