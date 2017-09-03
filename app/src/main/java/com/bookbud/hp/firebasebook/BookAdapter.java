package com.bookbud.hp.firebasebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HP on 08-08-2017.
 */

public class BookAdapter extends BaseAdapter implements Filterable {

    public Context context;
    public ArrayList<book> bookArrayList;
    public ArrayList<book> orig;

    public BookAdapter(Context context, ArrayList<book> bookArrayList) {
        super();
        this.context = context;
        this.bookArrayList = bookArrayList;
    }
    public class bookHolder
    {
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
                                    .contains(constraint.toString()) ||g.returnCourse().toLowerCase()
                                    .contains(constraint.toString())|| g.returnAuthor().contains(constraint.toString()) ||g.returnCourse()
                                    .contains(constraint.toString()) )
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
        bookHolder holder;
        if(convertView==null)
        {
            convertView=LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            holder=new bookHolder();
            holder.nameTextView=(TextView) convertView.findViewById(R.id.name);
            holder.authorTextView=(TextView) convertView.findViewById(R.id.author);
            holder.coursecodeTextView=(TextView) convertView.findViewById(R.id.coursecode);
            holder.coursenameTextView=(TextView) convertView.findViewById(R.id.course);
            holder.desc=(TextView) convertView.findViewById(R.id.description);
            holder.editionTextView=(TextView) convertView.findViewById(R.id.edition);
            holder.publisherTextView=(TextView) convertView.findViewById(R.id.publisher);
            holder.free=(ImageView) convertView.findViewById(R.id.free);
            //holder.regnoTextView=(TextView) convertView.findViewById(R.id.reg_no);
            holder.priceTextView=(TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        }
        else
        {
            holder=(bookHolder) convertView.getTag();
        }

        holder.nameTextView.setText(bookArrayList.get(position).returnName());
        holder.authorTextView.setText(bookArrayList.get(position).returnAuthor());
        holder.coursecodeTextView.setText(bookArrayList.get(position).returnCode());
        holder.coursenameTextView.setText(bookArrayList.get(position).returnCourse());
        //holder.regnoTextView.setText(bookArrayList.get(position).returnRegno());
        holder.priceTextView.setText(bookArrayList.get(position).returnPrice());
        holder.editionTextView.setText(bookArrayList.get(position).returnEdition());
        holder.publisherTextView.setText(bookArrayList.get(position).returnPublisher());
        holder.desc.setText(bookArrayList.get(position).returnDesc());
        //Log.e("Length is ",bookArrayList.get(position).returnDesc());
        if( holder.desc.length()==0)
        {
            holder.desc.setText("No content available");
        }
        if( holder.editionTextView.length()==0)
        {
            holder.editionTextView.setText("Not specified");
        }
        if( holder.publisherTextView.length()==0)
        {
            holder.publisherTextView.setText("Not specified");
        }
        //Log.e("Price is :",bookArrayList.get(position).returnPrice());

        if (bookArrayList.get(position).returnPrice()==": Rs. 0/-") {
            holder.free.setImageResource(R.drawable.free);
            holder.free.setTag(R.drawable.free);
        }
        return convertView;

    }

}
