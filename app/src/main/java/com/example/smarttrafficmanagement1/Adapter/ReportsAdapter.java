package com.example.smarttrafficmanagement1.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smarttrafficmanagement1.Class.Reports;
import com.example.smarttrafficmanagement1.R;

import java.util.List;

public class ReportsAdapter extends ArrayAdapter<Reports>{
    private Activity context;
    private List<Reports> reportsList;

    public ReportsAdapter(Activity context, List<Reports> reportsList){
        super(context, R.layout.reports_layout, reportsList);
        this.context = context;
        this.reportsList = reportsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.reports_layout,null,true);

        TextView reporter_Email = listViewItem.findViewById(R.id.reporter_Email);
        TextView reporter_Location = listViewItem.findViewById(R.id.reporter_Location);
        TextView reporter_Report = listViewItem.findViewById(R.id.reporter_Report);

        Reports reports = reportsList.get(position);
        reporter_Email.setText(reports.getUserName());
        reporter_Location.setText(reports.getLocation());
        reporter_Report.setText(reports.getReport());

        return listViewItem;
    }
}
