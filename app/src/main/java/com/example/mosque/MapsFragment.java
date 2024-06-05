package com.example.mosque;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {

    private RecyclerView recyclerView;
    private StoreAdapter adapter;
    private List<Store> storeList;
    private FirebaseFirestore db;
    private Button btnAddStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnAddStore = view.findViewById(R.id.btnAddStore);
        db = FirebaseFirestore.getInstance();
        storeList = new ArrayList<>();

        loadStoresFromFirestore();

        adapter = new StoreAdapter(storeList);
        recyclerView.setAdapter(adapter);

        btnAddStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddStoreDialog();
            }
        });

        return view;
    }

    private void loadStoresFromFirestore() {
        CollectionReference storesRef = db.collection("stores");
        storesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    Store store = document.toObject(Store.class);
                    storeList.add(store);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al cargar las tiendas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Uri imageUri;

    private void showAddStoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_store, null);
        builder.setView(view);

        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextAddress = view.findViewById(R.id.editTextAddress);
        final EditText editTextType = view.findViewById(R.id.editTextType);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnSelectImage = view.findViewById(R.id.btnSelectImage); // Botón para seleccionar imagen

        final AlertDialog dialog = builder.create();
        dialog.show();

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
                String type = editTextType.getText().toString().trim();

                if (name.isEmpty() || address.isEmpty() || type.isEmpty()) {
                    Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImageAndSaveStore(name, address, type, dialog);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
        }
    }

    private void uploadImageAndSaveStore(final String name, final String address, final String type, final AlertDialog dialog) {
        if (imageUri != null) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference("store_images/" + System.currentTimeMillis() + ".jpg");
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            saveStoreToFirestore(new Store(name, address, type, imageUrl));
                            dialog.dismiss();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Si no se seleccionó una imagen, guarda la tienda sin imagen
            saveStoreToFirestore(new Store(name, address, type, null));
            dialog.dismiss();

        }
    }
            private void saveStoreToFirestore(Store store) {
                db.collection("stores").add(store).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        storeList.add(store);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Tienda añadida exitosamente", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error al añadir la tienda", Toast.LENGTH_SHORT).show();
                    }
                });
            }
    }