package com.utflnx.whokonws.ui.room.ownership.extension;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        TextView textTitle, textNumber;
        public GeneralViewHolder(@NonNull View itemView) {
            super(itemView);
            textNumber = itemView.findViewById(R.id.item_number);
            textTitle = itemView.findViewById(R.id.item_title);
        }

        public void bind(RoomModel itemRoomModel, int position, RoomOwnerMainContract.Presenter mPresenter) {
            textNumber.setText(itemRoomModel.getMinute()+" min.");
            textTitle.setText(itemRoomModel.getDesc());

            if(itemRoomModel.isExpired())
                textNumber.setTextColor(Color.parseColor("#5cb85c")); //success | danger ~> #d9534f
            else
                textNumber.setTextColor(Color.parseColor("#f0ad4e")); //warning | info ~> #5bc0de

            itemView.setOnClickListener(view -> mPresenter.selectRoomItem(itemRoomModel));
            itemView.setOnLongClickListener(view -> mPresenter.selectLongRoomItem(itemRoomModel, position));
        }
    }
}
