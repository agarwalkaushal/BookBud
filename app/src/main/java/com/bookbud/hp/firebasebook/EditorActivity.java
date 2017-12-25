package com.bookbud.hp.firebasebook;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class EditorActivity extends AppCompatActivity {
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    private static final int PICK_IMAGE_REQUEST = 234;
    public static String key = NULL;
    View rootLayout;
    private EditText name;
    private int countnew = 0;
    private int upload = 0;
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
    private RelativeLayout addrel;
    private int d;
    private Bitmap bitmap;
    private FloatingActionButton add;
    private StorageReference mStorage;
    private int revealX;
    private int revealY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editor);
        final Intent intent = getIntent();

        rootLayout = findViewById(R.id.scroll);

        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);


            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }
        scrollView = (ScrollView) findViewById(R.id.scroll);
        name = (EditText) findViewById(R.id.input_name);
        contact = (EditText) findViewById(R.id.input_phone);
        coursename = (EditText) findViewById(R.id.input_course);
        coursecode = (EditText) findViewById(R.id.input_code);
        author = (EditText) findViewById(R.id.input_author);
        email = (EditText) findViewById(R.id.input_email);
        price = (EditText) findViewById(R.id.input_price);
        desc = (EditText) findViewById(R.id.input_desc);
        addrel = (RelativeLayout) findViewById(R.id.add_image_rel);
        add = (FloatingActionButton) findViewById(R.id.add_image);

        addrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload = 0;
                showFileChooser();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload = 0;
                showFileChooser();
            }
        });
        edition = (EditText) findViewById(R.id.input_edition);
        publisher = (EditText) findViewById(R.id.input_publisher);
        bookSpinner = (Spinner) findViewById(R.id.book_spinner);
        setupSpinner();
    }

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();

        }
    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter SpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_distribute_options, R.layout.custom_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        bookSpinner.setAdapter(SpinnerAdapter);

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
                    if (price.getText().toString().length() == 0) {
                        p = "Rs.0/-";
                    } else {
                        p = "Rs. " + price.getText().toString() + "/-";
                    }
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
                    email.setError("Email is required!");
                else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                        email.setError("Invalid");
                    } else {
                        countnew += 1;
                    }

                }
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
                else {
                    if (!Patterns.PHONE.matcher(contact.getText()).matches()) {
                        contact.setError("Invalid");
                    } else {
                        countnew += 1;
                    }

                }
                if (coursecode.getText().toString().length() == 0)
                    cc = "VIT, Vellore";

                Log.e("Value of countnew", String.valueOf(countnew));
                Log.e("Value of upload", String.valueOf(upload));
                if (countnew == 5 && upload == 0) {

                    //myRef.child(key).setValue(new book(n, a, cn, cc, r, c, e, p, d, ed, pub));
                    //Toast.makeText(EditorActivity.this, "Wait for the image to be uploaded, once done tap check", Toast.LENGTH_LONG).show();
                    return true;
                }
                if (countnew == 5 && upload >= 1) {

                    myRef.child(key).setValue(new book(n, a, pub, cc, r, c, e, p, d, ed, cn));
                    Toast.makeText(EditorActivity.this, "BOOK SUCCESSFULLY ADDED", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditorActivity.this, "BOOK NOT ADDED", Toast.LENGTH_SHORT).show();
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
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                add.setVisibility(View.INVISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void uploadFile() {
        //if there is a file to upload

        if (filePath != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            byte[] byteArray = out.toByteArray();
            //displaying a progress dialog while upload is going on
            //final ProgressDialog progressDialog = new ProgressDialog(this);
            // progressDialog.setTitle("Uploading");
            //progressDialog.show();
            final Snackbar snackbar1 = Snackbar
                    .make(scrollView, "Image uploading ...", Snackbar.LENGTH_INDEFINITE);

            snackbar1.show();
            mStorage = FirebaseStorage.getInstance().getReference();
            StorageReference riversRef = mStorage.child(key + ".jpg");
            riversRef.putBytes(byteArray)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            // progressDialog.dismiss();
                            snackbar1.setText("IMAGE UPLOADED");
                            snackbar1.dismiss();
                            upload += 1;
                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "Image Uploaded, Click check to finish adding other details ", Toast.LENGTH_LONG).show();

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
            Toast.makeText(getApplicationContext(), "No image file found", Toast.LENGTH_SHORT).show();

        }


    }

}

