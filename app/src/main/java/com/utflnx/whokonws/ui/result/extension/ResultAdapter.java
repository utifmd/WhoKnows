package com.utflnx.whokonws.ui.result.extension;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utflnx.whokonws.R;
import com.utflnx.whokonws.model.ResultModel;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();
    private final Context mContext;
    private List<ResultModel> mResultModels;

    public ResultAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<ResultModel> resultModels){
        this.mResultModels = resultModels;

        notifyDataSetChanged();
    }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GeneralViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_general, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ResultModel resultModel = mResultModels.get(position);

        ((GeneralViewHolder)holder).bind(resultModel, position);
    }

    @Override
    public int getItemCount() {
        return mResultModels.size();
    }

    static class GeneralViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvNum;
        public GeneralViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNum = itemView.findViewById(R.id.item_number);
            tvTitle = itemView.findViewById(R.id.item_title);
        }

        public void bind(ResultModel resultModel, int pos) {
            tvNum.setText((pos+1)+".");
            tvTitle.setText(resultModel.getUserName()+" | Score: "+resultModel.getScore());
        }
    }
}
