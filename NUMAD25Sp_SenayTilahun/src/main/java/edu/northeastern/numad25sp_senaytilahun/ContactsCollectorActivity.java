package edu.northeastern.numad25sp_senaytilahun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ContactsCollectorActivity extends AppCompatActivity {
    private ContactsAdapter contactsAdapter;
    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contacts_collector);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // init contact list data structure and RecyclerView
        contacts = new ArrayList<>();

        // restore saved contacts if available
        // this is for when screen is rotated
        if (savedInstanceState != null) {
            contacts = (ArrayList<Contact>) savedInstanceState.getSerializable("contacts");
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactsAdapter = new ContactsAdapter(this, contacts);
        recyclerView.setAdapter(contactsAdapter);

        // set up the + FAB button
        FloatingActionButton fabButton = findViewById(R.id.fab_add_contact);
        fabButton.setOnClickListener(view -> showAddContactDialog());

        // mskr sure back button works correct whenpresses with OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save list - for later user - orienttaion
        outState.putSerializable("contacts", new ArrayList<>(contacts));
    }

    private void showAddContactDialog() {
        // Inflate the custom dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_contact, null);

        // wire the references to EditText fields to save alter
        EditText editTextName = dialogView.findViewById(R.id.edit_text_name);
        EditText editTextPhone = dialogView.findViewById(R.id.edit_text_phone);

        // Build the dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView)
                .setTitle("Add New Contact")
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = editTextName.getText().toString().trim();
                    String phone = editTextPhone.getText().toString().trim();

                    // Validate input - make sure the fields are not empty when submitedd
                    if (!name.isEmpty() && !phone.isEmpty()) {
                        Contact newContact = new Contact(name, phone);
                        contacts.add(newContact);
                        // notify the adapter to update thee RecyclerView
                        contactsAdapter.notifyItemInserted(contacts.size() - 1);
                        // show snackbar for success
                        Snackbar.make(
                                findViewById(R.id.main), "Contact saved!",
                                        Snackbar.LENGTH_LONG)
                                .setAction("Undo", v -> {
                                    contacts.remove(contacts.size() - 1);
                                    contactsAdapter.notifyItemRemoved(contacts.size());
                                })
                                .show();
                    } else {
                        // show Snackbar for invalid input
                        Snackbar.make(
                                findViewById(R.id.main),
                                "Both fields are required!", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}
