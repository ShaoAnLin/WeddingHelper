package com.wedding.weddinghelper.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;
import com.wedding.weddinghelper.R;

import org.w3c.dom.Text;

import java.util.List;
import java.util.StringTokenizer;


/**
 * Created by Neal on 2016/9/3.
 */

public class GuestListAdapter extends BaseAdapter{
    private LayoutInflater myInflater;
    private List<ParseObject> guestList;

    public GuestListAdapter(Context context, List<ParseObject> theGuestList){
        myInflater = LayoutInflater.from(context);
        guestList = theGuestList;
    }

    @Override
    public int getCount() {
        Log.d("Neal","GuestListAdapter.size = "+guestList.size());
        return guestList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return guestList.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return guestList.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView==null){
            convertView = myInflater.inflate(R.layout.guest_detail_custom_list, null);
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.session),
                    (TextView) convertView.findViewById(R.id.name),
                    (TextView) convertView.findViewById(R.id.people_number),
                    (TextView) convertView.findViewById(R.id.diet));
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ParseObject guestInfo = (ParseObject) getItem(position);
        holder.nameTxt.setText(guestInfo.getString("Name"));
        if (guestInfo.getNumber("AttendingWilling").intValue() == 0){
            if (guestInfo.getNumber("Session").intValue() == 1) {
                holder.sessionTxt.setText("結婚場");
                holder.sessionTxt.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(),R.color.colorRedDark));
                holder.peopleNumberTxt.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(),R.color.colorRedDark));
            }
            else if (guestInfo.getNumber("Session").intValue() == 0){
                holder.sessionTxt.setText("訂婚場");
                holder.sessionTxt.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(),R.color.colorAccent));
                holder.peopleNumberTxt.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(),R.color.colorAccent));
            }
            else if (guestInfo.getNumber("Session").intValue() == -1){
                holder.sessionTxt.setText("參加");
                holder.sessionTxt.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(),R.color.colorAccent));
                holder.peopleNumberTxt.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(),R.color.colorAccent));
            }
            holder.dietTxt.setText(guestInfo.getNumber("MeatNumber").toString()+"人葷食，"+guestInfo.getNumber("VagetableNumber").toString()+"人素食");
            holder.peopleNumberTxt.setText(guestInfo.getNumber("PeopleNumber").toString()+"人\n"+"出席");
            holder.peopleNumberTxt.setVisibility(View.VISIBLE);
        }
        else {
            holder.sessionTxt.setText("不出席");
            holder.sessionTxt.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(),R.color.colorGrayDark));
            //holder.peopleNumberTxt.setVisibility(View.INVISIBLE);
            holder.peopleNumberTxt.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(),R.color.colorGrayDark));
            holder.peopleNumberTxt.setText("");
            String notation = guestInfo.getString("Notation");
            if (notation.length() != 0){
                holder.dietTxt.setText(guestInfo.getString("Notation"));
            }
            else {
                holder.dietTxt.setText("無備注");
            }

        }
        return convertView;
    }

    private class ViewHolder {
        TextView sessionTxt;
        TextView nameTxt;
        TextView peopleNumberTxt;
        TextView dietTxt;
        public ViewHolder(TextView sessionTxt, TextView nameTxt, TextView peopleNumberTxt, TextView dietTxt){
            this.sessionTxt = sessionTxt;
            this.nameTxt = nameTxt;
            this.peopleNumberTxt = peopleNumberTxt;
            this.dietTxt = dietTxt;
        }
    }
}


