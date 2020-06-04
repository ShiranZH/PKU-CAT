package com.example.pkucat.manage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pkucat.R;
import com.example.pkucat.net.APIException;
import com.example.pkucat.net.Client;

import java.util.Arrays;

public class FeederList extends BaseAdapter {
    private Context mContext;
    private Client client;
    private String[] feeders;
    public FeederList(Context mContext, Client client)
    {
        super();
        this.mContext = mContext;
        this.client = client;
        try{
            String[] feedersID = client.feeder.getFeeders();
            feeders = new String[feedersID.length];
            for(int i=0; i<feedersID.length; i++)
            {
                feeders[i] = client.user.getProfile(feedersID[i]).username;
            }
            Arrays.sort(feeders);
        }
        catch (APIException e)
        {
            Toast.makeText(mContext, e.getDescription(), Toast.LENGTH_SHORT).show();
            feeders = new String[0];
        }

    }

    @Override
    public int getCount() {
        return feeders.length;
    }

    @Override
    public Object getItem(int position) { return feeders[position];}

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.feeder_list_item,null);
        String feederName = (String) getItem(position);
        TextView textViewName = view.findViewById(R.id.feeder_name);
        textViewName.setText(feederName);
        return view;
    }
}
