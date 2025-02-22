package edu.northeastern.numad25sp_senaytilahun;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private final List<Contact> contactList;
    private final Context context;

    // Constructor
    public ContactsAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Convert XML layout to View object
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        // Get the contact at this position
        Contact contact = contactList.get(position);

        // Display the contact name and phone number
        holder.nameTextView.setText(contact.getName());
        holder.phoneTextView.setText(contact.getPhoneNumber());

        // Tap to call
        holder.itemView.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
            context.startActivity(callIntent);
        });

        // Delete contact
        holder.deleteButton.setOnClickListener(v -> {
            Contact removedContact = contactList.get(position);
            contactList.remove(position);
            notifyItemRemoved(position);

            // Show Snackbar for undo action
            Snackbar.make(holder.itemView, "Contact deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo", view -> {
                        contactList.add(position, removedContact);
                        notifyItemInserted(position);
                    })
                    .show();
        });

        // Edit contact functionality - for when they click the icon
        holder.editButton.setOnClickListener(v -> {
            showEditContactDialog(contact, position);
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    private void showEditContactDialog(Contact contact, int position) {
        // Inflate the custom dialog layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_add_contact, null);

        // Get references to EditText fields
        EditText editTextName = dialogView.findViewById(R.id.edit_text_name);
        EditText editTextPhone = dialogView.findViewById(R.id.edit_text_phone);

        // Pre-fill the existing contact details
        editTextName.setText(contact.getName());
        editTextPhone.setText(contact.getPhoneNumber());

        // Build and show the dialog
        new AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle("Edit Contact")
                .setPositiveButton("Update", (dialog, which) -> {
                    String updatedName = editTextName.getText().toString().trim();
                    String updatedPhone = editTextPhone.getText().toString().trim();

                    if (!updatedName.isEmpty() && !updatedPhone.isEmpty()) {
                        // Update the contact in the list
                        contactList.set(position, new Contact(updatedName, updatedPhone));
                        notifyItemChanged(position);
                        Toast.makeText(context, "Contact updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Both fields are required", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    // Holds the views for each contact
    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, phoneTextView;
        ImageButton deleteButton, editButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.contact_name);
            phoneTextView = itemView.findViewById(R.id.contact_phone);
            deleteButton = itemView.findViewById(R.id.button_delete);
            editButton = itemView.findViewById(R.id.button_edit);
        }
    }
}
