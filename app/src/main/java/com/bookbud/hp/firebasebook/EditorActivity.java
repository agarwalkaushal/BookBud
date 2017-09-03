package com.bookbud.hp.firebasebook;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class EditorActivity extends AppCompatActivity {
    private EditText name;
    public static String key=NULL;
    private  EditText regno;
    private  EditText contact;
    private  EditText coursename;
    private  EditText coursecode;
    private EditText author;
    private EditText edition;
    private EditText publisher;
    private EditText email;
    private Spinner bookSpinner;
    private EditText price;
    private ImageView free;
    private EditText desc;
    private int d;
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        name = (EditText) findViewById(R.id.input_name);
        regno = (EditText) findViewById(R.id.input_reg_n0);
        contact = (EditText) findViewById(R.id.input_phone);
        coursename = (EditText) findViewById(R.id.input_course);
        coursecode = (EditText) findViewById(R.id.input_code);
        author = (EditText) findViewById(R.id.input_author);
        email=(EditText) findViewById(R.id.input_email);
        price=(EditText) findViewById(R.id.input_price);
        desc=(EditText) findViewById(R.id.input_desc);
        free=(ImageView) findViewById(R.id.free);
        edition=(EditText) findViewById(R.id.input_edition);
        publisher=(EditText) findViewById(R.id.input_publisher);
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
    private void insertbook()
    {
        count=0;
        String p;
        Log.e("Value of d: ",String.valueOf(d));
        if(d==0)
        {
            p="Rs. 0/-";
        }
        else {
            p = "Rs. "+price.getText().toString()+"/-";
        }
        String n=name.getText().toString();
        String a=author.getText().toString();
        String r=regno.getText().toString();
        String c=contact.getText().toString();
        String cn=coursename.getText().toString();
        String cc=coursecode.getText().toString();
        String e=email.getText().toString();
        String d=desc.getText().toString();
        String ed=edition.getText().toString();
        String pub=publisher.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("books");

        if( name.getText().toString().length() == 0 )
            name.setError( "Name is required!" );
        else
            count+=1;
        if( email.getText().toString().length() == 0 )
            email.setError( "Name is required!" );
        else
            count+=1;
        if( coursename.getText().toString().length() == 0 )
            coursename.setError( "Book name is required!" );
        else
            count+=1;
        if( author.getText().toString().length() == 0 )
            author.setError( "Author is required!" );
        else
            count+=1;
        if( contact.getText().toString().length() == 0 )
            contact.setError( "Contact is required!" );
        else
            count+=1;
        if( coursecode.getText().toString().length() == 0 )
            cc="VIT, Vellore";
        if(count ==5){
        myRef.push().setValue(new book(n,a,cn,cc,r,c,e,p,d,ed,pub));
            key = myRef.push().getKey();
        Toast.makeText(EditorActivity.this,"BOOK SUCCESSFULLY ADDED",Toast.LENGTH_SHORT).show();}
        else
        {
            Toast.makeText(EditorActivity.this, "Book not added, Please enter all required fields", Toast.LENGTH_LONG).show();
        }

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
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertbook();
                if(count==5)
                finish();
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

}
