package com.flyingzone.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ListOfCustomerAdapter extends ArrayAdapter<Customer> {

    Context context;
    public ListOfCustomerAdapter(Context context1, List<Customer> list) {
        // TODO Auto-generated constructor stub
        super(context1, 0, list);
        context=context1;
    }


    @SuppressLint("SetTextI18n")
    @Override
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
// Get the data item for this position
        final Customer customer = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_child, parent, false);
        }
        final TextView name =  convertView.findViewById(R.id.userName);
        final TextView phone =  convertView.findViewById(R.id.phoneNumber);
        final TextView aaddhar =  convertView.findViewById(R.id.addharNumber);
        final TextView pen =  convertView.findViewById(R.id.penCard);
        final TextView email =  convertView.findViewById(R.id.emailId);
        final TextView addrss =  convertView.findViewById(R.id.address);
        final TextView gender =  convertView.findViewById(R.id.gender);
        final TextView dob =  convertView.findViewById(R.id.dob);
        final LinearLayout parent_layout = convertView.findViewById(R.id.parent_layout);

        try {
            if (customer != null) {
                name.setText(":  "+customer.getName());
                phone.setText(":  "+customer.getPhone());
                aaddhar.setText(":  "+customer.getAddhar());
                pen.setText(":  "+customer.getPen());
                email.setText(":  "+customer.getEmail());
                addrss.setText(":  "+customer.getAddress());
                gender.setText(":  "+customer.getGender());
                dob.setText(":  "+customer.getDob());
                parent_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

}
