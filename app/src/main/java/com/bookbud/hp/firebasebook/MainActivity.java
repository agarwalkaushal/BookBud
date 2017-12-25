package com.bookbud.hp.firebasebook;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    public int count;
    boolean doubleBackToExitPressedOnce = false;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    ProgressBar progressBarHolder;
    private String n;
    private String c;
    private String a;
    private String cc;
    private String r;
    private String cn;
    private String e;
    private String p;
    private String d;
    private String i;
    private String contact;
    private String email;
    private String ed;
    private String pubi;
    private String choice = "VIT";
    private SearchView mSearchView;
    private ListView listView;
    private ArrayList<book> b;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        progressBarHolder = (ProgressBar) findViewById(R.id.progressBarHolder);
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);

        /*
        MobileAds.initialize(this, "ca-app-pub-1093385171813087/6490965707");
        AdRequest request = new AdRequest.Builder().addTestDevice("DB87789ADD286D94F9D5F938BA2BC5A6").build();
        request.isTestDevice(MainActivity.this);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        */

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //Toast.makeText(MainActivity.this, "Active internet connection found", Toast.LENGTH_SHORT).show();
            Snackbar snackbar1 = Snackbar
                    .make(swipeRefreshLayout, "Active internet connection found", Snackbar.LENGTH_LONG);
            snackbar1.show();
        } else {
            //Toast.makeText(MainActivity.this, "No internet connection ", Toast.LENGTH_SHORT).show();
            Snackbar snackbar1 = Snackbar
                    .make(swipeRefreshLayout, "No internet connection ", Snackbar.LENGTH_LONG);
            snackbar1.show();
        }
        listView = (ListView) findViewById(R.id.listview_with_fab);
        mSearchView = (SearchView) findViewById(R.id.searchView1);
        b = new ArrayList<book>();

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");

        connectedRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    progressBarHolder.setAnimation(outAnimation);
                    progressBarHolder.setVisibility(View.GONE);

                    Toast.makeText(MainActivity.this, "connection successful", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(MainActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("books").orderByChild("a").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Value of choice", choice);
                b.clear();
                count = 0;
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.e("Failed to read value.", "Before reading name");
                    n = (String) child.child("a").getValue();
                    Log.e("Failed to read value.", "After reading name");
                    Log.e("Name is :", n);
                    a = (String) child.child("c").getValue();
                    cc = (String) child.child("e").getValue();
                    cn = (String) child.child("d").getValue();
                    c = (String) child.child("b").getValue();
                    r = child.getKey();
                    e = (String) child.child("g").getValue();
                    p = (String) child.child("h").getValue();
                    d = (String) child.child("i").getValue();
                    ed = (String) child.child("j").getValue();
                    pubi = (String) child.child("k").getValue();
                    Log.e("Price is", p);

                    switch (choice) {
                        case "VIT":
                            if (cc.contains("VIT")) {
                                Log.e("It is VIT cc is ", cc);
                                b.add(new book(n, a, cn, cc, r, c, e, p, d, ed, pubi));
                                count += 1;
                            }
                            break;
                        case "Others":
                            if (!cc.contains("VIT")) {
                                Log.e("Its not VIT cc is", cc);

                                b.add(new book(n, a, cn, cc, r, c, e, p, d, ed, pubi));
                                count += 1;
                            }
                            break;
                        case "All Records":
                            b.add(new book(n, a, cn, cc, r, c, e, p, d, ed, pubi));
                            count += 1;
                    }

                }
                //Toast.makeText(MainActivity.this, "Number of books listed: "+count, Toast.LENGTH_SHORT).show();

                BookAdapter itemsAdapter = new BookAdapter(MainActivity.this, b);
                listView.setAdapter(itemsAdapter);
                swipeRefreshLayout.setRefreshing(false);
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
                        database.getReference("books").orderByChild("a").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.e("Value of choice", choice);
                                b.clear();
                                count = 0;
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                for (DataSnapshot child : dataSnapshot.getChildren()) {

                                    n = (String) child.child("a").getValue();
                                    //Log.e("Name is :", n);
                                    a = (String) child.child("c").getValue();
                                    cc = (String) child.child("e").getValue();
                                    cn = (String) child.child("d").getValue();
                                    c = (String) child.child("b").getValue();
                                    r = child.getKey();
                                    e = (String) child.child("g").getValue();
                                    p = (String) child.child("h").getValue();
                                    d = (String) child.child("i").getValue();
                                    ed = (String) child.child("j").getValue();
                                    pubi = (String) child.child("k").getValue();
                                    Log.e("Value of cc", cc);

                                    switch (choice) {
                                        case "VIT":
                                            if (cc.contains("VIT")) {
                                                Log.e("It is VIT cc is ", cc);
                                                b.add(new book(n, a, cn, cc, r, c, e, p, d, ed, pubi));
                                                count += 1;
                                            }
                                            break;
                                        case "Others":
                                            if (!cc.contains("VIT")) {
                                                Log.e("Its not VIT cc is", cc);

                                                b.add(new book(n, a, cn, cc, r, c, e, p, d, ed, pubi));
                                                count += 1;
                                            }
                                            break;
                                        case "All Records":
                                            b.add(new book(n, a, cn, cc, r, c, e, p, d, ed, pubi));
                                            count += 1;
                                    }

                                }
                                //Toast.makeText(MainActivity.this, "Number of books listed: "+count, Toast.LENGTH_SHORT).show();
                                BookAdapter itemsAdapter = new BookAdapter(MainActivity.this, b);
                                listView.setAdapter(itemsAdapter);
                                swipeRefreshLayout.setRefreshing(false);
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_action_button_fab_with_listview);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(MainActivity.this, view, "transition");
                int revealX = (int) (view.getX() + view.getWidth() / 2);
                int revealY = (int) (view.getY() + view.getHeight() / 2);
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra(EditorActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
                intent.putExtra(EditorActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);

                ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listView.performLongClick();
                book books = b.get(i);
                Log.e("Value of i is", String.valueOf(i));
                contact = books.returnContact();
                email = books.returnEmail();


            }
        });
        registerForContextMenu(listView);

    }

    /*
        public void myClickHandler(View v) {

            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.image);
            dialog.setTitle("This is my custom dialog box");
            dialog.setCancelable(true);
            RelativeLayout lin = (RelativeLayout) dialog.findViewById(R.id.child);
            StorageReference ref = FirebaseStorage.getInstance().getReference(r);
            //set up image view
            ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);

            Glide.with(this)
                    .using(new FirebaseImageLoader()).load(ref)
                    .into(img);

            //now that the dialog is set up, it's time to show it
            dialog.show();
        }
    */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact, menu);


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.e("Value is: ", "item is selected");
        switch (item.getItemId()) {
            case R.id.sms:
                Log.e("Value is: ", contact);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contact, null)));
                return true;
            case R.id.email:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
                startActivity(Intent.createChooser(intent, "Send email..."));
                return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.serach, menu);
        swipeRefreshLayout.setRefreshing(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //case R.id.post:
            // Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            //  startActivity(intent);
            //  break;
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
            case R.id.vit:
                choice = "VIT";
                Snackbar snackbar = Snackbar
                        .make(listView, "Swipe down to refresh list", Snackbar.LENGTH_LONG);

                snackbar.show();
                //Toast.makeText(MainActivity.this, "Swipe down to refresh list", Toast.LENGTH_SHORT).show();
                break;
            case R.id.others:
                choice = "Others";
                Snackbar snackbar1 = Snackbar
                        .make(listView, "Swipe down to refresh list", Snackbar.LENGTH_LONG);

                snackbar1.show();

                //Toast.makeText(MainActivity.this, "Swipe down to refresh list", Toast.LENGTH_SHORT).show();
                break;
            case R.id.all:
                Snackbar snackbar2 = Snackbar
                        .make(listView, "Swipe down to refresh list", Snackbar.LENGTH_LONG);

                snackbar2.show();
                choice = "All Records";
                //Toast.makeText(MainActivity.this, "Swipe down to refresh list", Toast.LENGTH_SHORT).show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
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
                doubleBackToExitPressedOnce = false;
            }
        }, 1500);
    }

    @Override
    public void onRefresh() {

    }

    private void setupSearchView() {
        swipeRefreshLayout.setRefreshing(false);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("by Book Name or Author");
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        BookAdapter ca = (BookAdapter) listView.getAdapter();
        Filter filter = ca.getFilter();
        filter.filter(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}