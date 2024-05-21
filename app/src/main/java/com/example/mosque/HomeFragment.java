package com.example.mosque;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewProjects;
    private FloatingActionButton fab;
    private ProjectAdapter projectAdapter;
    private List<Project> projectList;
    private boolean isAdmin = false; // Inicialmente no es admin

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        recyclerViewProjects = view.findViewById(R.id.recyclerViewProjects);
        fab = view.findViewById(R.id.fab);

        // Setup RecyclerView
        recyclerViewProjects.setLayoutManager(new LinearLayoutManager(getContext()));
        projectAdapter = new ProjectAdapter(getContext(), projectList);
        recyclerViewProjects.setAdapter(projectAdapter);

        // Show or hide FAB based on isAdmin
        if (isAdmin) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        } else {
            fab.setVisibility(View.GONE);
        }

        return view;
    }

    // This method can be used to set the isAdmin status from outside the fragment
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    // This method can be used to set the project list from outside the fragment
    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
        if (projectAdapter != null) {
            projectAdapter.updateProjects(projectList);
        }
    }
}
