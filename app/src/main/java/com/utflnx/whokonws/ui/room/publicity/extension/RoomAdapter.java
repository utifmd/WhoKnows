package com.utflnx.whokonws.ui.room.publicity.extension;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utflnx.whokonws.R;
import com.utflnx.whokonws.model.QuizModel;
import com.utflnx.whokonws.ui.room.publicity.RoomMainContract;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();
    private RoomMainContract.Presenter mPresenter;
    private List<QuizModel> mQuizModelList;
    private Context mContext;

    public RoomAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<QuizModel> originalList){
        mQuizModelList = originalList;

        notifyDataSetChanged();
    }

    public void setPresenter(RoomMainContract.Presenter presenter){
        mPresenter = presenter;
    }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GeneralViewHolder((View)
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_general, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        QuizModel quizModel = mQuizModelList.get(position);

        ((GeneralViewHolder)holder).bind(quizModel, mPresenter);
    }

    @Override
    public int getItemCount() {
        return mQuizModelList.size();
    }

    private class GeneralViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        public GeneralViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.item_title);
        }

        public void bind(QuizModel quizModel, RoomMainContract.Presenter mPresenter) {
            textViewTitle.setText(quizModel.getQuestion());

            itemView.setOnClickListener(view -> mPresenter.selectItemQuiz(quizModel));
        }
    }
}
