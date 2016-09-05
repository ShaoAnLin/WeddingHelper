package com.wedding.weddinghelper.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.parse.ParseObject;
import com.wedding.weddinghelper.R;

import java.util.List;


/**
 * Created by Neal on 2016/9/3.
 */

public class GuestListAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<ParseObject> guestList;
    public static int headerAbsentPos = -1;
    public static int headerEngagePos = -1;
    public static int headerMarryPos = -1;

    public GuestListAdapter(Context context, List<ParseObject> theGuestList) {
        myInflater = LayoutInflater.from(context);
        guestList = theGuestList;
    }

    @Override
    public int getCount() {
        Log.d("Neal", "GuestListAdapter.size = " + guestList.size());
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

        if (convertView == null) {
            convertView = myInflater.inflate(R.layout.guest_detail_custom_list, null);
            holder = new ViewHolder(
                    (TableRow) convertView.findViewById(R.id.guest_list_item),
                    (TextView) convertView.findViewById(R.id.session_or_name),
                    (TextView) convertView.findViewById(R.id.people_number),
                    (TextView) convertView.findViewById(R.id.diet));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ParseObject guestInfo = (ParseObject) getItem(position);

        if (headerAbsentPos == position) {
            holder.sessionOrNameTxt.setText("不出席");
            holder.peopleNumberTxt.setText("");
            holder.dietTxt.setText("");
            holder.guestListItem.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(), R.color.colorAbsentDark));
        }
        else if (headerEngagePos == position) {
            if (headerMarryPos != -1) {
                holder.sessionOrNameTxt.setText("訂婚場");
                holder.peopleNumberTxt.setText("");
                holder.dietTxt.setText("");
                holder.guestListItem.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(), R.color.colorEngageDark));
            }
            else {
                holder.sessionOrNameTxt.setText("參加");
                holder.peopleNumberTxt.setText("");
                holder.dietTxt.setText("");
                holder.guestListItem.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(), R.color.colorEngageDark));
            }
        }
        else if (headerMarryPos == position){
            holder.sessionOrNameTxt.setText("結婚場");
            holder.peopleNumberTxt.setText("");
            holder.dietTxt.setText("");
            holder.guestListItem.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(), R.color.colorMarryDark));
        }
        else{
            // not the header
            holder.sessionOrNameTxt.setText(guestInfo.getString("Name"));
            if (guestInfo.getNumber("AttendingWilling").intValue() == 0) {
                if (guestInfo.getNumber("Session").intValue() == 1) {
                    holder.guestListItem.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(), R.color.colorMarryLight));
                } else if (guestInfo.getNumber("Session").intValue() == 0) {
                    holder.guestListItem.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(), R.color.colorEngageLight));
                } else if (guestInfo.getNumber("Session").intValue() == -1) {
                    holder.guestListItem.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(), R.color.colorEngageLight));
                }
                holder.dietTxt.setText(guestInfo.getNumber("MeatNumber").toString() + "葷，" + guestInfo.getNumber("VagetableNumber").toString() + "素");
                holder.peopleNumberTxt.setText(guestInfo.getNumber("PeopleNumber").toString() + "人");
                holder.peopleNumberTxt.setVisibility(View.VISIBLE);
            } else {
                holder.peopleNumberTxt.setText("");
                holder.guestListItem.setBackgroundColor(ContextCompat.getColor(myInflater.getContext(), R.color.colorAbsentLight));
                String notation = guestInfo.getString("Notation");
                if (notation.length() != 0) {
                    holder.dietTxt.setText(guestInfo.getString("Notation"));
                } else {
                    holder.dietTxt.setText("無備注");
                }
            }
        }
        return convertView;
    }

    private class ViewHolder {
        TableRow guestListItem;
        TextView sessionOrNameTxt;
        TextView peopleNumberTxt;
        TextView dietTxt;
        public ViewHolder(TableRow guestListItem, TextView sessionOrNameTxt, TextView peopleNumberTxt, TextView dietTxt){
            this.guestListItem = guestListItem;
            this.sessionOrNameTxt = sessionOrNameTxt;
            this.peopleNumberTxt = peopleNumberTxt;
            this.dietTxt = dietTxt;
        }
    }
}


