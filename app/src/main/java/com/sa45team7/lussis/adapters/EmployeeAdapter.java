package com.sa45team7.lussis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.sa45team7.lussis.rest.model.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhatton on 1/22/18.
 */

public class EmployeeAdapter extends BaseAdapter implements ListAdapter, Filterable {

    private Context mContext;
    private List<Employee> mList;
    private ArrayList<String> nameList = new ArrayList<>();

    public EmployeeAdapter(List<Employee> list, Context context) {
        mList = list;
        mContext = context;
        for (Employee e : list) {
            nameList.add(e.getFullName());
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Employee getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                FilterResults results = new FilterResults();
                if(mList == null) mList = new ArrayList<>();

                if (prefix == null || prefix.length() == 0) {
                    results.values = mList;
                    results.count = mList.size();
                } else {
                    ArrayList<Employee> resultData = new ArrayList<>();
                    for (Employee e : mList) {
                        if (e.getFullName().contains(prefix)) {
                            resultData.add(e);
                        }
                    }

                    results.values = resultData;
                    results.count = resultData.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mList = (ArrayList<Employee>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_dropdown_item_1line, null);
            holder = new ViewHolder();
            holder.fullNameText = convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Employee employee = getItem(position);
        holder.fullNameText.setText(employee.getFullName());

        return convertView;
    }

    private class ViewHolder {
        TextView fullNameText;
    }
}
