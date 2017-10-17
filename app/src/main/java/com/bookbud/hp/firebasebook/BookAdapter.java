package com.bookbud.hp.firebasebook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import javax.sql.DataSource;

import static android.content.ContentValues.TAG;
import static com.bookbud.hp.firebasebook.R.id.imageView;


public class BookAdapter extends BaseAdapter implements Filterable {

    public Context context;
    public ArrayList<book> bookArrayList;
    public ArrayList<book> orig;
    private String key;
    private String nKey;

    public BookAdapter(Context context, ArrayList<book> bookArrayList) {
        super();
        this.context = context;
        this.bookArrayList = bookArrayList;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<book> results = new ArrayList<book>();
                if (orig == null)
                    orig = bookArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final book g : orig) {
                            if (g.returnAuthor().toLowerCase()
                                    .contains(constraint.toString()) || g.returnCourse().toLowerCase()
                                    .contains(constraint.toString()) || g.returnAuthor().contains(constraint.toString()) || g.returnCourse()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                bookArrayList = (ArrayList<book>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bookArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final bookHolder holder;
        if (convertView == null) {




            holder = new bookHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

            holder.click = (Button) convertView.findViewById(R.id.button2);


            holder.nameTextView = (TextView) convertView.findViewById(R.id.name);
            holder.authorTextView = (TextView) convertView.findViewById(R.id.author);
            holder.coursecodeTextView = (TextView) convertView.findViewById(R.id.coursecode);
            holder.coursenameTextView = (TextView) convertView.findViewById(R.id.course);
            holder.desc = (TextView) convertView.findViewById(R.id.description);
            holder.editionTextView = (TextView) convertView.findViewById(R.id.edition);
            holder.publisherTextView = (TextView) convertView.findViewById(R.id.publisher);
            holder.priceTextView = (TextView) convertView.findViewById(R.id.price);
            holder.free = (ImageView) convertView.findViewById(R.id.free1);


            convertView.setTag(holder);
        } else {

            holder = (bookHolder) convertView.getTag();
        }


        holder.nameTextView.setText(bookArrayList.get(position).returnName());
        holder.authorTextView.setText(bookArrayList.get(position).returnAuthor());
        holder.coursecodeTextView.setText(bookArrayList.get(position).returnCode());
        holder.coursenameTextView.setText(bookArrayList.get(position).returnCourse());

        key = bookArrayList.get(position).returnRegno();

        holder.priceTextView.setText(bookArrayList.get(position).returnPrice());
        holder.editionTextView.setText(bookArrayList.get(position).returnEdition());
        holder.publisherTextView.setText(bookArrayList.get(position).returnPublisher());
        holder.desc.setText(bookArrayList.get(position).returnDesc());
        Log.e("Price is :",bookArrayList.get(position).returnPrice());
        key=key+".jpg";
        final StorageReference ref = FirebaseStorage.getInstance().getReference(key);

        holder.click.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.image);
                dialog.setTitle("This is my custom dialog box");
                dialog.setCancelable(true);

                //set up image view
                ImageView img = (ImageView) dialog.findViewById(R.id.img_glide);
                dialog.getWindow().getAttributes().width = RelativeLayout.LayoutParams.FILL_PARENT;
                //now that the dialog is set up, it's time to show it
                dialog.show();
                Log.e("Key in BA is",key);
                Glide.with(context)
                        .using(new FirebaseImageLoader())
                        .load(ref)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(img);
            }
        });

        //StorageReference ref = FirebaseStorage.getInstance().getReference(key);




        if (bookArrayList.get(position).returnPrice().contains(".0/")) {
            Log.e("Inside if"," loop");
            holder.free.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.free1));
        }
            else
        {
            Log.e("Not Inside if"," loop");
            holder.free.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dogfold1));
        }

        if (holder.desc.length() == 0) {
            holder.desc.setText("No content available");
        }
        if (holder.editionTextView.length() == 0) {
            holder.editionTextView.setText("Not specified");
        }
        if (holder.publisherTextView.length() == 0) {
            holder.publisherTextView.setText("Not specified");
        }



        return convertView;

    }

    public static class bookHolder {
        TextView nameTextView;
        TextView authorTextView;
        TextView coursenameTextView;
        TextView coursecodeTextView;
        TextView regnoTextView;
        TextView editionTextView;
        TextView publisherTextView;
        TextView priceTextView;
        ImageView free;
        TextView desc;
        Button click;
        ImageView bookImage;
        ProgressDialog progressDialog;


    }
    public class FirebaseImageLoader implements StreamModelLoader<StorageReference> {

        @Override
        public DataFetcher<InputStream> getResourceFetcher(StorageReference model, int width, int height) {
            return new BookAdapter.FirebaseImageLoader.FirebaseStorageFetcher(model);
        }

        private class FirebaseStorageFetcher implements DataFetcher<InputStream> {

            private StorageReference mRef;

            FirebaseStorageFetcher(StorageReference ref) {
                mRef = ref;
            }

            @Override
            public InputStream loadData(Priority priority) throws Exception {
                return Tasks.await(mRef.getStream()).getStream();
            }

            @Override
            public void cleanup() {
                // No cleanup possible, Task does not expose cancellation
            }

            @Override
            public String getId() {
                return mRef.getPath();
            }

            @Override
            public void cancel() {
                // No cancellation possible, Task does not expose cancellation
            }
        }
    }


    }
