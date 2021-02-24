package com.utflnx.whokonws.ui.explore.extension;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.utflnx.whokonws.R;
import com.utflnx.whokonws.model.ExploreModel;

import java.util.List;

public class ExploreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();
    private List<ExploreModel> mExploreModels;

    public void setData(List<ExploreModel> exploreModels){
        mExploreModels = exploreModels;

        notifyDataSetChanged();
    }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GeneralViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore_general, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExploreModel exploreModel = mExploreModels.get(position);

        ((GeneralViewHolder)holder).bind(exploreModel, position);
    }

    @Override
    public int getItemCount() {
        return mExploreModels.size();
    }

    static class GeneralViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvFullName;
        Chip chipCompleted, chipTopic;
        Button btnParticipate;

        public GeneralViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvFullName = itemView.findViewById(R.id.tv_full_name);
            chipCompleted = itemView.findViewById(R.id.chip_finish);
            chipTopic = itemView.findViewById(R.id.chip_topic);
            btnParticipate = itemView.findViewById(R.id.btn_participate);
        }

        public void bind(ExploreModel exploreModel, int position) {
            tvTitle.setText(exploreModel.getQuestion());
            tvFullName.setText(exploreModel.getUserName());
            chipTopic.setText(exploreModel.getTitle());

            if (exploreModel.isExpired()){
                btnParticipate.setVisibility(View.VISIBLE);
            }else {
                chipCompleted.setText(R.string.uncompleted);
                chipCompleted.setVisibility(View.VISIBLE);
            }
        }
    }
}
