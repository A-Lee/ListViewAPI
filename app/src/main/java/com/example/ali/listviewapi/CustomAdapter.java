package com.example.ali.listviewapi;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<DataItem>
{

    Context context;
    int layoutID;
    List<DataItem> datalist = null;
    public CustomAdapter(Context context, int resource, List<DataItem> objects)
    {
        super(context, resource, objects);
        this.context = context;
        this.datalist = objects;
        this.layoutID = resource;
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
