package com.bookbud.hp.firebasebook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class EditorActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    public static String key = NULL;
    private EditText name;
    private int countnew = 0;
    private int upload = 0;
    private EditText regno;
    private EditText contact;
    private EditText coursename;
    private EditText coursecode;
    private EditText author;
    private EditText edition;
    private EditText publisher;
    private Uri filePath;
    private EditText email;
    private Spinner bookSpinner;
    private EditText price;
    private ImageView imageView;
    private EditText desc;
    private ScrollView scrollView;
    private String iE = "";
    private int d;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editor);
        scrollView = (ScrollView) findViewById(R.id.scroll);
        name = (EditText) findViewById(R.id.input_name);
        //regno = (EditText) findViewById(R.id.input_reg_n0);
        contact = (EditText) findViewById(R.id.input_phone);
        coursename = (EditText) findViewById(R.id.input_course);
        coursecode = (EditText) findViewById(R.id.input_code);
        author = (EditText) findViewById(R.id.input_author);
        email = (EditText) findViewById(R.id.input_email);
        price = (EditText) findViewById(R.id.input_price);
        desc = (EditText) findViewById(R.id.input_desc);
        //free=(ImageView) findViewById(R.id.free);
        edition = (EditText) findViewById(R.id.input_edition);
        publisher = (EditText) findViewById(R.id.input_publisher);
        bookSpinner = (Spinner) findViewById(R.id.book_spinner);
        setupSpinner();
    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_distribute_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        bookSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        bookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("Free")) {
                        d = 0;
                    } else if (selection.equals("Sell")) {
                        d = 1;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void insertbook() {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("books");

        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_photo:
                upload = 0;
                showFileChooser();
                Log.e("Value of upload in show", String.valueOf(upload));
            case R.id.upload_photo:
                Log.e("Value of uploa in uploa", String.valueOf(upload));
                key = myRef.push().getKey();
                Log.e("Key of upload", key);
                break;
            case R.id.action_save:
                uploadFile();
                countnew = 0;
                String p;
                Log.e("Value of d: ", String.valueOf(d));
                if (d == 0) {
                    p = "Rs.0/-";
                } else {
                    p = "Rs. " + price.getText().toString() + "/-";
                }
                String n = name.getText().toString();
                String a = author.getText().toString();
                String r = "";
                String c = contact.getText().toString();
                String cn = coursename.getText().toString();
                String cc = coursecode.getText().toString();
                String e = email.getText().toString();
                String d = desc.getText().toString();
                String ed = edition.getText().toString();
                String pub = publisher.getText().toString();
                if (name.getText().toString().length() == 0)
                    name.setError("Name is required!");
                else
                    countnew += 1;
                if (email.getText().toString().length() == 0)
                    email.setError("Name is required!");
                else
                    countnew += 1;
                if (coursename.getText().toString().length() == 0)
                    coursename.setError("Book name is required!");
                else
                    countnew += 1;
                if (author.getText().toString().length() == 0)
                    author.setError("Author is required!");
                else
                    countnew += 1;
                if (contact.getText().toString().length() == 0)
                    contact.setError("Contact is required!");
                else
                    countnew += 1;
                if (coursecode.getText().toString().length() == 0)
                    cc = "VIT, Vellore";

                Log.e("Value of countnew", String.valueOf(countnew));
                Log.e("Value of upload", String.valueOf(upload));
                if (countnew == 5 && upload == 0) {

                    myRef.child(key).setValue(new book(n, a, cn, cc, r, c, e, p, d, ed, pub, iE));
                    Toast.makeText(EditorActivity.this, "Wait for the image to be uploaded to server, once done click check", Toast.LENGTH_LONG).show();
                    return true;
                }
                if (countnew == 5 && upload == 1) {

                    myRef.child(key).setValue(new book(n, a, cn, cc, r, c, e, p, d, ed, pub, iE));
                    Toast.makeText(EditorActivity.this, "BOOK SUCCESSFULLY ADDED", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditorActivity.this, "Book not added, Please see if the required fields are filled", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_back:
                finish();
                return true;
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageView = (ImageView) findViewById(R.id.imageView);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void uploadFile() {
        //if there is a file to upload

        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            //final ProgressDialog progressDialog = new ProgressDialog(this);
            // progressDialog.setTitle("Uploading");
            //progressDialog.show();
            final Snackbar snackbar1 = Snackbar
                    .make(scrollView, "Image uploading ...", Snackbar.LENGTH_INDEFINITE);

            snackbar1.show();
            key=key+".jpg";
            mStorage = FirebaseStorage.getInstance().getReference();
            StorageReference riversRef = mStorage.child(key);
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            // progressDialog.dismiss();
                            snackbar1.dismiss();
                            upload += 1;
                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "Image Uploaded ", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            // progressDialog.dismiss();
                            snackbar1.dismiss();
                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            // progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
            // progressDialog.dismiss();
        }
        //if there is not any file
        else {
            Toast.makeText(getApplicationContext(), "No image file found", Toast.LENGTH_LONG).show();

        }


    }

}

