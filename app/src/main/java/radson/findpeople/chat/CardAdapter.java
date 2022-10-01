package radson.findpeople.chat;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import radson.findpeople.R;
import radson.findpeople.recyclercardview.ListItem;

/**
 * Created by Belal on 10/29/2015.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    List<ListItem> items;

    public CardAdapter(String[] names, String[] urls, String[] REG_ID,Bitmap[] images){
        super();
        items = new ArrayList<ListItem>();
        for(int i =0; i<names.length; i++){
            ListItem item = new ListItem();
            item.setName(names[i]);
            item.setUrl(urls[i]);
            item.setreg_id(REG_ID[i]);
            item.setImage(images[i]);
            items.add(item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(Chat_Name_Fragment.myOnClickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem list =  items.get(position);
        holder.imageView.setImageBitmap(list.getImage());
        holder.textViewName.setText(list.getName());
        holder.textViewRegId.setText(list.get_reg_id());
        holder.textViewUrl.setText(list.getUrl());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textViewName;
        public TextView textViewRegId;
        public TextView textViewUrl;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewRegId = (TextView)itemView.findViewById(R.id.textView_reg_id);
            textViewUrl = (TextView) itemView.findViewById(R.id.textViewUrl);

        }
    }
}
