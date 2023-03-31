package com.example.googleanalyticsass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesCategoryAdapter extends RecyclerView.Adapter<NotesCategoryAdapter.ViewHolder> {
    private List<NotesCategory> mData;
    private LayoutInflater mInflater;
    private NotesCategoryAdapter.ItemClickListener mClickListener;
    private NotesCategoryAdapter.ItemClickListener2 itemClickListener2;


    NotesCategoryAdapter(Context context, List<NotesCategory> data, NotesCategoryAdapter.ItemClickListener onClick, NotesCategoryAdapter.ItemClickListener2 onClick2) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mClickListener = onClick;
        this.itemClickListener2 = onClick2;
    }

    @Override
    public NotesCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.note_row, parent, false);
        return new NotesCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotesCategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.notename.setText(mData.get(position).getUsername());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener2.onItemClick2(holder.getAdapterPosition());

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView notename;
        public CardView card;

        ViewHolder(View itemView) {
            super(itemView);
            this.notename = itemView.findViewById(R.id.category_name);
            this.card = itemView.findViewById(R.id.card2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }

    public NotesCategory getItem(int id) {
        return mData.get(id);
    }

    void setClickListener(NotesCategoryAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public interface ItemClickListener2{
        void onItemClick2(int position);
    }
}
