package com.supcon.mes.module_lims.ui.popu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.supcon.mes.middleware.model.bean.PopupWindowEntity;
import com.supcon.mes.module_lims.R;

import java.util.List;

/**
 * Author by fengjun1,
 * Date on 2020/4/7.
 */
public class LIMSPopupWindowAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<PopupWindowEntity> mItems;

    public LIMSPopupWindowAdapter(Context context, List<PopupWindowEntity> items) {
        mContext = context;
        mItems = items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopupWindowHolder popupWindowHolder = null;
        if (convertView == null) {
            popupWindowHolder = new PopupWindowHolder();
            convertView = inflater.inflate(R.layout.item_lims_popuwindow, null);
            popupWindowHolder.mTextView = convertView.findViewById(R.id.popupTv);
            popupWindowHolder.mPopIv=convertView.findViewById(R.id.ic_popIv);
            convertView.setTag(popupWindowHolder);

        } else {
            popupWindowHolder = (PopupWindowHolder) convertView.getTag();
        }
        PopupWindowEntity popupWindowEntity = (PopupWindowEntity) getItem(position);
        popupWindowHolder.mTextView.setText(popupWindowEntity.getText());
        popupWindowHolder.mPopIv.setImageResource(popupWindowEntity.getIconId());
        return convertView;
    }

    private class PopupWindowHolder {
        TextView mTextView;
        ImageView mPopIv;
    }
}
