package com.example.smarttrafficmanagement1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttrafficmanagement1.Class.Reports;
import com.example.smarttrafficmanagement1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ImageViewHolder> {
    private Activity mContext;
    private List<Reports> mReportsList;

    public ReportsAdapter(Context context, List<Reports> reportsList){
        mContext= (Activity) context;
        mReportsList=reportsList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.reports_layout,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Reports reports=mReportsList.get(position);
        holder.reporter_Email.setText(reports.getUserName());
        holder.reporter_Location.setText(reports.getLocation());
        holder.reporter_Report.setText(reports.getReport());
        Picasso.get().load(reports.getImage()).fit().centerCrop().into(holder.image_Report);

    }

    @Override
    public int getItemCount() {
        return mReportsList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView reporter_Email;
        public TextView reporter_Location;
        public TextView reporter_Report;
        public ImageView image_Report;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
        reporter_Email = itemView.findViewById(R.id.reporter_Email);
        reporter_Location = itemView.findViewById(R.id.reporter_Location);
        reporter_Report = itemView.findViewById(R.id.reporter_Report);
        image_Report=itemView.findViewById(R.id.img_Report);

        }
    }

//    public ReportsAdapter(Activity context, List<Reports> reportsList){
//        super(context, R.layout.reports_layout, reportsList);
//        this.context = context;
//        this.reportsList = reportsList;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater inflater = context.getLayoutInflater();
//        View listViewItem = inflater.inflate(R.layout.reports_layout,null,true);
//
//        TextView reporter_Email = listViewItem.findViewById(R.id.reporter_Email);
//        TextView reporter_Location = listViewItem.findViewById(R.id.reporter_Location);
//        TextView reporter_Report = listViewItem.findViewById(R.id.reporter_Report);
//
//
//        Reports reports = reportsList.get(position);
//        reporter_Email.setText(reports.getUserName());
//        reporter_Location.setText(reports.getLocation());
//        reporter_Report.setText(reports.getReport());
//
//        return listViewItem;
//    }
}
