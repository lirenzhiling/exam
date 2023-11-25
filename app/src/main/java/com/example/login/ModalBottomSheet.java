package com.example.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class ModalBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    public interface EditHead{
        void onClicks(View v);

    }
    public EditHead editHead;
    View view;
    private TextView take_photo;
    private TextView album;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        take_photo = view.findViewById(R.id.take_photo);
        album = view.findViewById(R.id.album);
        view.findViewById(R.id.take_photo).setOnClickListener(this);
        view.findViewById(R.id.album).setOnClickListener(this);
        return view;
    }

    public static final String TAG = "ModalBottomSheet";

    @Override
    public void onClick(View v) {
        editHead.onClicks(v);
//        if (v == take_photo) {
//            Toast.makeText(view.getContext(), "拍照", Toast.LENGTH_SHORT).show();
//
//        }
//        if (v == album) {
//            Toast.makeText(view.getContext(), "相册", Toast.LENGTH_SHORT).show();
//        }
    }

}

