package com.example.rahulkrishnan;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {


    private ArrayList<Pair<String, String>> dataset;
    private Context context;

    public TaskListAdapter(Cursor dataset, Context context) {
        this.dataset = cursorToList(dataset);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_list_row, viewGroup, false);

        return new ViewHolder(v);
    }

    public ArrayList<Pair<String, String>> cursorToList(Cursor cursor) {
        ArrayList<Pair<String, String>> list = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                Pair<String, String> temp = new Pair<String, String>(cursor.getString(1), cursor.getString(2));
                list.add(temp);
            }
        }
        return list;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        View view;
        Pair<String, String> row_item = dataset.get(i);
        viewHolder.taskName.setText(row_item.first);
        viewHolder.taskDate.setText(row_item.second);

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskName, taskDate;

        public ViewHolder(View itemView) {
            super(itemView);

            taskName = itemView.findViewById(R.id.task_name_row);
            taskDate = itemView.findViewById(R.id.task_date_row);

        }
    }
}
