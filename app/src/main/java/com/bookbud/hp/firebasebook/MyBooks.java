package com.bookbud.hp.firebasebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyBooks extends AppCompatActivity {
    private String n;
    private String c;
    private String a;
    private String cc;
    private String r;
    private String cn;
    private String e;
    private String p;
    private String d;
    private String contact;
    private String email;
    private String nKey;
    private ListView listView ;
    private book obj;
    private ArrayList<book> adap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);
        listView = (ListView) findViewById(R.id.listview_with_fab);
        adap = new ArrayList<book>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("fir-book-12e70");

        Log.e("Key is: ", EditorActivity.key);
        nKey = "\"" + EditorActivity.key + "\"";
        Log.e("nKey is: ", nKey);
        DatabaseReference nref=ref.child(EditorActivity.key);
        if (EditorActivity.key != "SAFE_PARCELABLE_NULL_STRING") {
            nref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    adap.clear();
                    for (DataSnapshot child : snapshot.getChildren()) {

                        obj = (book) child.child(nKey).getValue();
                        //Log.e("Name is :", n);
                        n=obj.returnName();
                        a = obj.returnAuthor();
                        cc = obj.returnCode();
                        cn = obj.returnCourse();
                        c = obj.returnContact();
                        r = obj.returnRegno();
                        e = obj.returnEmail();
                        p = obj.returnPrice();
                        d= obj.returnDesc();
                        //adap.add(new book(n, a, cn, cc, r, c, e, p,d));

                    }
                    BookAdapter itemsAdapter = new BookAdapter(MyBooks.this, adap);
                    listView.setAdapter(itemsAdapter);
                    listView.setTextFilterEnabled(true);
                    itemsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    Log.e("onCancelled", " cancelled");
                }
            });
        }
    }
}
