package com.mobiletagreader.boonnarit.mobiletagreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Created by stdisd01 on 18/6/2558.
 */
public class informationAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<String> orderId = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> status = new ArrayList<String>();

    public informationAdapter(Context context, ArrayList<String> orderlist, ArrayList<String> datelist,ArrayList<String> status){
        synchronized (this) {
            this.mContext = context;
            this.orderId = orderlist;
            this.date = datelist;
            this.status = status;
        }
    }

    public int getCount() {
        return orderId.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        try {
            sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LayoutInflater mInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            view = mInflater.inflate(R.layout.custom_information_list, parent, false);


        TextView orderid = (TextView) view.findViewById(R.id.order_id);
        orderid.setText(orderId.get(position));

        TextView datetime = (TextView) view.findViewById(R.id.date_time);
        datetime.setText(date.get(position));

        if(status.get(position).equals("success")) {
            ImageView status = (ImageView) view.findViewById(R.id.imageStatus);
            status.setImageResource(R.drawable.status_active);
        }

        return view;


    }

}