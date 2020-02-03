package com.example.terasakireservasi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.terasakireservasi.Activity.ListMenuActivity;
import com.example.terasakireservasi.Koneksi.Config;
import com.example.terasakireservasi.R;
import com.squareup.picasso.Picasso;

public class AdapterListMenu extends BaseAdapter {

    private Activity activity;

    public AdapterListMenu(Activity act) {
        this.activity = act;
    }

    public int getCount() {
        return ListMenuActivity.ID_MENU.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lsv_item_menu_list, null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtText = (TextView) convertView.findViewById(R.id.txtText);
        holder.txtSubText = (TextView) convertView.findViewById(R.id.txtSubText);
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);

        holder.txtText.setText(ListMenuActivity.NAMA_MENU.get(position));

        holder.txtSubText.setText(ListMenuActivity.HARGA.get(position));

        Picasso.get().load(Config.PATH_URL_IMG_CAT+ ListMenuActivity.IMAGE.get(position)).into(holder.imgThumb);

        return convertView;
    }


    static class ViewHolder {
        TextView txtText, txtSubText;
        ImageView imgThumb;
    }


}
