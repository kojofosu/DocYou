package com.edue.docyou;

import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import androidx.core.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;


public class DeleteBottomSheetFragment extends BottomSheetDialogFragment {

    Button confirmDel, cancelDel;
    String mNoteFileName;
    Note mLoadedNote;
    String NOTE_FILE_2;

    public DeleteBottomSheetFragment() {
        // Required empty public constructor
    }

    public static DeleteBottomSheetFragment newInstance(){
        return new DeleteBottomSheetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_layout, container, false);

        if (getArguments() != null) {
            NOTE_FILE_2 = getArguments().getString("NOTE_FILE_2");//Get pass data with its key value
            mNoteFileName = NOTE_FILE_2;

            if(mNoteFileName != null && !mNoteFileName.isEmpty()){
                mLoadedNote = Utilities.getNoteByName(Objects.requireNonNull(getContext()), mNoteFileName);

            }
        }


        confirmDel = view.findViewById(R.id.confirm_delete);
        cancelDel = view.findViewById(R.id.cancel_delete);

        confirmDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLoadedNote == null) {
                    Objects.requireNonNull(getActivity()).finish();
                } else {
                    Utilities.deleteNote(Objects.requireNonNull(getContext()), mLoadedNote.getDateTime() + Utilities.FILE_EXTENSION);

                    Toast.makeText(getContext(), /*mEtTitle.getText().toString()+ */ "Note deleted", Toast.LENGTH_SHORT).show();

                    Objects.requireNonNull(getActivity()).finish();
                }
            }
        });

        cancelDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }
}
