package com.example.pkucat.archive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pkucat.R;

public class ArchiveFragment extends Fragment {
    private ArchiveViewModel archiveViewModel;
    private Button bSearchArchive;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_archive, container, false);
        bSearchArchive = (Button) root.findViewById(R.id.searchArchive_button);
        return root;
    }
    public void search(){

    }
}
