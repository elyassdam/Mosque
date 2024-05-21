package com.example.mosque;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private List<Project> projectList;
    private Context context;

    public ProjectAdapter(Context context, List<Project> projectList) {
        this.context = context;
        this.projectList = projectList;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projectList.get(position);
        holder.textViewProjectTitle.setText(project.getTitle());
        holder.textViewProjectDescription.setText(project.getDescription());
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }
    public void updateProjects(List<Project> newProjectList) {
        projectList = newProjectList;
        notifyDataSetChanged();
    }
    public static class ProjectViewHolder extends RecyclerView.ViewHolder {

        TextView textViewProjectTitle;
        TextView textViewProjectDescription;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProjectTitle = itemView.findViewById(R.id.textViewProjectTitle);
            textViewProjectDescription = itemView.findViewById(R.id.textViewProjectDescription);
        }

    }
}
