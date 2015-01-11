package com.blstream.marekwernikowski;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;


import java.util.ArrayList;

/**
 * Created by Marek Wernikowski on 10.01.2015.
 */

public class JsonCustomList extends ArrayAdapter<Element> {
    private final Activity contextActivity;

    public JsonCustomList(Activity _contextActivity, ArrayList<Element> elements) {

        super(_contextActivity, R.layout.json_list);
        contextActivity = _contextActivity;
    }

    @Override
    public void add(Element element) {
        super.add(element);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Element element = getItem(position);
        View elementView;
        if (view == null) {
            LayoutInflater inflater = contextActivity.getLayoutInflater();
            elementView = inflater.inflate(R.layout.json_list, null, true);
        } else
            elementView = view;

        TextView title = (TextView) elementView.findViewById(R.id.title);
        TextView description = (TextView) elementView.findViewById(R.id.desc);
        title.setText(element.title);
        description.setText(element.description);
        AQuery aQuery = new AQuery(elementView);
        aQuery.id(R.id.icon).progress(R.id.loading_icon).image(element.image, false, false, 0, R.drawable.ic_launcher);
        return elementView;
    }

}
