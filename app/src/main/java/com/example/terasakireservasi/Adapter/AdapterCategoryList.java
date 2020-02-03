package com.example.terasakireservasi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.terasakireservasi.Fragment.FragmentHome;
import com.example.terasakireservasi.Koneksi.Config;
import com.example.terasakireservasi.Koneksi.JSONParser;
import com.example.terasakireservasi.R;
import com.squareup.picasso.Picasso;

// adapter class for custom category list
public class AdapterCategoryList extends BaseAdapter {

    private Activity activity;

    public AdapterCategoryList(Activity act) {
        this.activity = act;
    }

    public int getCount() {
        return FragmentHome.Id_Category.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_list_item, null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtText = (TextView) convertView.findViewById(R.id.txtText);
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);

        holder.txtText.setText(FragmentHome.Nama_Category.get(position));
        Picasso.get().load(Config.PATH_URL_IMG+FragmentHome.Img_Category.get(position)).into(holder.imgThumb);
        return convertView;
    }

    static class ViewHolder {
        TextView txtText;
        ImageView imgThumb;
    }


}