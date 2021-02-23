package com.utflnx.whokonws.ui.room.ownership.extension;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.utflnx.whokonws.R;
import com.utflnx.whokonws.model.RoomModel;
import com.utflnx.whokonws.ui.room.ownership.RoomOwnerMainContract;

import java.util.List;

public class RoomOwnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<RoomModel> roomModelList;
    private RoomOwnerMainContract.Presenter mPresenter;

    public RoomOwnerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setPresenter(RoomOwnerMainContract.Presenter presenter){
        mPresenter = presenter;
    }

    public void setData(List<RoomModel> originalList){
        roomModelList = originalList;

        notifyDataSetChanged();
    }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GeneralViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_general, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RoomModel itemRoomModel = roomModelList.get(position);

        ((GeneralViewHolder)holder).bind(itemRoomModel, position, mPresenter);
    }

    @Override
    public int getItemCount() {
        return roomModelList.size();
    }

    private static class GeneralViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle, textSubtitle;
        Chip chipTime, chipTrash;
        Button btnView, btnDelete;

        public GeneralViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.tv_title);
            textSubtitle = itemView.findViewById(R.id.tv_sub_title);
            chipTime = itemView.findViewById(R.id.chip_time);
            chipTrash = itemView.findViewById(R.id.chip_uncompleted);
            btnView = itemView.findViewById(R.id.btn_view);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(RoomModel itemRoomModel, int position, RoomOwnerMainContract.Presenter mPresenter) {
            textTitle.setText(itemRoomModel.getTitle());
            textSubtitle.setText(itemRoomModel.getDesc());
            chipTime.setText(itemRoomModel.getMinute()+" min.");

            if(itemRoomModel.isExpired()) {
                ((MaterialCardView) itemView).setChecked(true); // .setTextColor(Color.parseColor("#5cb85c")); //success | danger ~> #d9534f
                chipTrash.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
            }else {
                ((MaterialCardView) itemView).setChecked(false); //textNumber.setTextColor(Color.parseColor("#f0ad4e")); //warning | info ~> #5bc0de
                chipTrash.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
            }
            btnView.setOnClickListener(view -> mPresenter.selectRoomItem(itemRoomModel));
            btnDelete.setOnClickListener(view -> mPresenter.selectLongRoomItem(itemRoomModel, position));
        }
    }
}
