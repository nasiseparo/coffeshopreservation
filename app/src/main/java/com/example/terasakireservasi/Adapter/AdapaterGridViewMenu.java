package com.example.terasakireservasi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.terasakireservasi.Model.ModelMenu;
import com.example.terasakireservasi.R;


import java.util.ArrayList;

public class AdapaterGridViewMenu extends ArrayAdapter<ModelMenu> {
	Context mContext;
	int resourceId;
	ArrayList<ModelMenu> data = new ArrayList<ModelMenu>();

	public AdapaterGridViewMenu(Context context, int layoutResourceId, ArrayList<ModelMenu> data) {
		super(context, layoutResourceId, data);
		this.mContext = context;
		this.resourceId = layoutResourceId;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		ViewHolder holder = null;

		if (itemView == null) {
			final LayoutInflater layoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemView = layoutInflater.inflate(resourceId, parent, false);

			holder = new ViewHolder();
			holder.imgItem = (ImageView) itemView.findViewById(R.id.thumbnails);
			holder.txtItem = (TextView) itemView.findViewById(R.id.name);
			itemView.setTag(holder);
		} else {
			holder = (ViewHolder) itemView.getTag();
		}

		ModelMenu item = getItem(position);
		holder.imgItem.setImageDrawable(item.getImage());
		holder.txtItem.setText(item.getNamamenu());

		return itemView;
	}

	static class ViewHolder {
		ImageView imgItem;
		TextView txtItem;
	}

}