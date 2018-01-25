package com.sa45team7.lussis.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sa45team7.lussis.R;
import com.sa45team7.lussis.rest.model.RetrievalItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nhatton on 1/23/18.
 */

public class RetrievalAdapter extends RecyclerView.Adapter<RetrievalAdapter.RetrievalHolder> {

    private List<RetrievalItem> mValues;
    private OnRetrievalListInteractionListener mListener;

    public RetrievalAdapter(List<RetrievalItem> list, OnRetrievalListInteractionListener listener) {
        mValues = list;
        Collections.sort(mValues);
        mListener = listener;
    }

    @Override
    public RetrievalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_retrieval, parent, false);
        return new RetrievalHolder(view);
    }

    @Override
    public void onBindViewHolder(final RetrievalHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mBinText.setText(holder.mItem.getBinNum());
        holder.mDescriptionText.setText(holder.mItem.getDescription());
        holder.mRequestQty.setText(String.valueOf(holder.mItem.getRequestedQty()));
        holder.mAvailableQty.setText(String.valueOf(holder.mItem.getAvailableQty()));
        holder.mUomText.setText(holder.mItem.getUnitOfMeasure());
        holder.mAdjustButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) mListener.onSelectAdjust(holder.mItem);
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onSelectRetrievalItem(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class RetrievalHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mBinText;
        final TextView mDescriptionText;
        final TextView mRequestQty;
        final TextView mAvailableQty;
        final TextView mUomText;
        final Button mAdjustButton;
        RetrievalItem mItem;

        RetrievalHolder(View view) {
            super(view);
            mView = view;
            mBinText = view.findViewById(R.id.bin_num_text);
            mDescriptionText = view.findViewById(R.id.description_text);
            mRequestQty = view.findViewById(R.id.request_qty_text);
            mAvailableQty = view.findViewById(R.id.available_qty_text);
            mUomText = view.findViewById(R.id.uom_text);
            mAdjustButton = view.findViewById(R.id.adjust_button);
        }
    }

    public interface OnRetrievalListInteractionListener {
        void onSelectRetrievalItem(RetrievalItem item);
        void onSelectAdjust(RetrievalItem item);
    }
}
