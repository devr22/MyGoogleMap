package com.example.mygooglemap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mygooglemap.model.addressInfo;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private addressInfo maddressInfo;

    private TextView address;

    public BottomSheetDialog() {
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.info_bottom_sheet, container, false);

        address = v.findViewById(R.id.address);

        maddressInfo = new addressInfo();

        address.setText("Latitude: " + maddressInfo.getLatitude() + "\n" +
                "Longitude: " + maddressInfo.getLongitude() + "\n" +
                "Country: " + maddressInfo.getContry() + "\n" +
                "Locality: " + maddressInfo.getLocality() + "\n" +
                "Postal Code: " + maddressInfo.getPostalcode() + "\n" +
                "Url: " + maddressInfo.getUrl() + "\n" +
                "Phone: " + maddressInfo.getPhoneNumber());

        Button btn_close = v.findViewById(R.id.btn_close);;
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;

    }
}
