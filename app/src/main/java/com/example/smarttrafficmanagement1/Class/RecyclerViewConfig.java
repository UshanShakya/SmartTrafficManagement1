package com.example.smarttrafficmanagement1.Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttrafficmanagement1.R;

import java.util.List;

public class RecyclerViewConfig {
    private Context mContext;
    private ReportsAdapter mReportsAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Reports> reports, List<String> keys){
        mContext = context;
        mReportsAdapter = new ReportsAdapter(reports,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mReportsAdapter);
    }
    class ReportsItemView extends RecyclerView.ViewHolder{
        private TextView tvUsername;
        private TextView tvLocation;
        private TextView tvReport;

        private String key;


        public ReportsItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.reports_layout,parent,false));

            tvUsername = itemView.findViewById(R.id.reporter_Email);
            tvLocation = itemView.findViewById(R.id.reporter_Location);
            tvReport = itemView.findViewById(R.id.reporter_Report);
        }

        public void bind(Reports reports, String key){
            tvUsername.setText(reports.getUserName());
            tvLocation.setText(reports.getLocation());
            tvReport.setText(reports.getReport());
            this.key = key;
        }
    }
    class ReportsAdapter extends RecyclerView.Adapter<ReportsItemView>{


        private List<Reports> mReportsList;
        private List<String> mKeys;

        public ReportsAdapter(List<Reports> mReportsList, List<String> mKeys) {
            this.mReportsList = mReportsList;
            this.mKeys = mKeys;
        }
        @NonNull
        @Override
        public ReportsItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ReportsItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ReportsItemView holder, int position) {
            holder.bind(mReportsList.get(position),mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mReportsList.size();
        }
    }
}
