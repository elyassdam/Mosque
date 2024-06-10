package com.example.mosque;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    private ContentAdapter contentAdapter;
    private List<Content> contentList;

    private Uri imageUri;
    private EditText editTextTitle, editTextDescription;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        firestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        contentList = new ArrayList<>();
        contentAdapter = new ContentAdapter(requireContext(), contentList);
        recyclerView.setAdapter(contentAdapter);

        Button addButtonNews = view.findViewById(R.id.addButtonNews);
        addButtonNews.setOnClickListener(v -> showAddContentDialog("NOTICIA"));

        Button addButtonPhoto = view.findViewById(R.id.addButtonPhoto);
        addButtonPhoto.setOnClickListener(v -> showAddContentDialog("FOTO"));

        Button addButtonProject = view.findViewById(R.id.addButtonProject);
        addButtonProject.setOnClickListener(v -> showAddContentDialog("PROYECTO"));

        Button buttonViewStudents = view.findViewById(R.id.buttonViewStudents);
        buttonViewStudents.setOnClickListener(v -> openStudentsFragment());
        Button buttonLogout = view.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(v -> logout());


        loadData();

        return view;
    }

    private void loadData() {
        firestore.collection("contents").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<Content> contents = queryDocumentSnapshots.toObjects(Content.class);
                        contentList.clear();
                        contentList.addAll(contents);
                        contentAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(requireContext(), "No se encontraron datos", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show());
    }

    private void showAddContentDialog(String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Añadir " + type);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_content, null);
        builder.setView(dialogView);

        editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        imageView = dialogView.findViewById(R.id.imageView);

        Button selectImageButton = dialogView.findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(v -> openFileChooser());

        builder.setPositiveButton("Añadir", (dialog, which) -> addContent(type));
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void addContent(String type) {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (!title.isEmpty() && !description.isEmpty()) {
            Content content;
            if (imageUri != null) {
                content = new Content(title, description, imageUri.toString(), type);
            } else {
                content = new Content(title, description, type);
            }

            firestore.collection("contents").add(content)
                    .addOnSuccessListener(documentReference -> {
                        content.setId(documentReference.getId());
                        firestore.collection("contents").document(content.getId()).set(content)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(requireContext(), type + " añadida correctamente", Toast.LENGTH_SHORT).show();
                                    loadData();
                                })
                                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error al añadir " + type.toLowerCase(), Toast.LENGTH_SHORT).show());
                    })
                    .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error al añadir " + type.toLowerCase(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void openStudentsFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment fragment = new ArabicClassesFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(requireContext(), Login.class));
        requireActivity().finish();
    }
}
