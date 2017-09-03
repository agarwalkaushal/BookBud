package com.bookbud.hp.firebasebook;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.content.Intent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,SwipeRefreshLayout.OnRefreshListener{
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
    private String ed;
    private String pubi;
    private AdView mAdView;
    private SearchView mSearchView;
    private ListView listView ;
    private ArrayList<book> b;
    private SwipeRefreshLayout swipeRefreshLayout;
    boolean doubleBackToExitPressedOnce = false;
    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        MobileAds.initialize(this, "ca-app-pub-1093385171813087/6490965707");
        AdRequest request = new AdRequest.Builder().addTestDevice("DB87789ADD286D94F9D5F938BA2BC5A6").build();
        request.isTestDevice(MainActivity.this);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        listView = (ListView) findViewById(R.id.listview_with_fab);
        mSearchView=(SearchView) findViewById(R.id.searchView1);

        b = new ArrayList<book>();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(MainActivity.this, "connection successful", Toast.LENGTH_SHORT).show();
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("books");
        // Read from the database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                b.clear();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    n = (String) child.child("a").getValue();
                    //Log.e("Name is :", n);
                    a = (String) child.child("c").getValue();
                    cc = (String) child.child("e").getValue();
                    cn = (String) child.child("d").getValue();
                    c = (String) child.child("b").getValue();
                    r = (String) child.child("f").getValue();
                    e = (String) child.child("g").getValue();
                    p = (String) child.child("h").getValue();
                    d=(String) child.child("i").getValue();
                    ed=(String) child.child("j").getValue();
                    pubi=(String) child.child("k").getValue();
                    b.add(new book(n, a, cn, cc, r, c, e, p,d,ed,pubi));

                }
                BookAdapter itemsAdapter = new BookAdapter(MainActivity.this, b);
                listView.setAdapter(itemsAdapter);
                listView.setTextFilterEnabled(true);
                setupSearchView();
                itemsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("Failed to read value.", String.valueOf(error.toException()));
            }
        });
        // Write a message to the database
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.e("Swipe ", "onRefresh called from SwipeRefreshLayout");
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference("books");
                        // Read from the database
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                b.clear();
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                for (DataSnapshot child : dataSnapshot.getChildren()) {

                                    n = (String) child.child("a").getValue();
                                    //Log.e("Name is :", n);
                                    a = (String) child.child("c").getValue();
                                    cc = (String) child.child("e").getValue();
                                    cn = (String) child.child("d").getValue();
                                    c = (String) child.child("b").getValue();
                                    r = (String) child.child("f").getValue();
                                    e = (String) child.child("g").getValue();
                                    p = (String) child.child("h").getValue();
                                    d=(String) child.child("i").getValue();
                                    ed=(String) child.child("j").getValue();
                                    pubi=(String) child.child("k").getValue();
                                    b.add(new book(n, a, cn, cc, r, c, e, p,d,ed,pubi));

                                }
                                BookAdapter itemsAdapter = new BookAdapter(MainActivity.this, b);
                                listView.setAdapter(itemsAdapter);
                                listView.setTextFilterEnabled(true);
                                setupSearchView();
                                itemsAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.e("Failed to read value.", String.valueOf(error.toException()));
                            }
                        });
                        Toast.makeText(MainActivity.this, "list updated", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

        );

        // Setup FAB to open EditorActivity
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_action_button_fab_with_listview);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                book books = b.get(i);
                contact=books.returnContact();
                email=books.returnEmail();
                registerForContextMenu(view);

            }
        });


    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 1500);
    }

    @Override
    public void onRefresh() {

    }
    private void setupSearchView()
    {
        swipeRefreshLayout.setRefreshing(false);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("by Book Name or Author");
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        BookAdapter ca = (BookAdapter) listView.getAdapter();
        Filter filter = ca.getFilter();


        filter.filter(newText);
        return true;
        /*
        if (TextUtils.isEmpty(newText)) {
            listView.clearTextFilter();
            //ca.getFilter().filter(null);
        } else {
            listView.setFilterText(newText);
            //ca.getFilter().filter(newText);
        }
        */
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }




    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "SMS");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "EMAIL");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        Log.e("Value is: ","item is selected");
        if(item.getTitle()=="SMS"){
            Log.e("Value is: ",contact);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contact, null)));
        }
        else if(item.getTitle()=="EMAIL"){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {email});
            startActivity(Intent.createChooser(intent, "Send email..."));

        }else{
            return false;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.serach, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.post:
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_item_share:
                mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String sAux = "\nLet me recommend you this application!!!\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.bookbud.hp.firebasebook \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
                //setShareIntent(sharingIntent);
                break;
            case R.id.about:
                Intent intent1 = new Intent(MainActivity.this, info.class);
                startActivity(intent1);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

}