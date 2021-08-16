package com.eugeniojava.compufix.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eugeniojava.compufix.R;

import java.util.List;

public class ComputerAdapter extends BaseAdapter {

    private final Context context;
    private final List<Computer> computers;

    public ComputerAdapter(Context context, List<Computer> computers) {
        this.context = context;
        this.computers = computers;
    }

    @Override
    public int getCount() {
        return computers.size();
    }

    @Override
    public Object getItem(int position) {
        return computers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ComputerHolder computerHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(
                    R.layout.list_view_computer, parent, false);
            computerHolder = new ComputerHolder();

            computerHolder.textViewOwner =
                    convertView.findViewById(R.id.textViewOwner);
            computerHolder.textViewType = convertView.findViewById(R.id.textViewType);
            computerHolder.textViewCustomer = convertView.findViewById(R.id.textViewCustomer);
            computerHolder.textViewUrgent = convertView.findViewById(R.id.textViewUrgente);
            convertView.setTag(computerHolder);
        } else {
            computerHolder = (ComputerHolder) convertView.getTag();
        }
        computerHolder.textViewOwner.setText(
                context.getString(R.string.computer_adapter_owner_prefix, computers.get(position).getOwner()));
        computerHolder.textViewType.setText(
                context.getString(R.string.computer_adapter_type_prefix, computers.get(position).getTypeId()));
        computerHolder.textViewCustomer.setText(
                context.getString(R.string.computer_adapter_customer_prefix,
                        computers.get(position).getCustomerType()));
        String urgent;
        if (computers.get(position).isUrgent()) {
            urgent = context.getString(R.string.computer_adapter_urgent_yes);
        } else {
            urgent = context.getString(R.string.computer_adapter_urgent_no);
        }
        computerHolder.textViewUrgent.setText(context.getString(R.string.computer_adapter_urgent_prefix, urgent));

        return convertView;
    }

    private static class ComputerHolder {
        public TextView textViewOwner;
        public TextView textViewType;
        public TextView textViewCustomer;
        public TextView textViewUrgent;
    }
}
