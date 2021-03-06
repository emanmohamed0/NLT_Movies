package com.example.emyeraky.nlt_movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Emy Eraky on 2/16/2017.
 */
public class AdapterImage extends BaseAdapter {
    Context context;
    String [] image;
    AdapterImage(Context context,String[] image){
      this.context=context;
      this.image=image;
    }
    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public Object getItem(int i) {
        return image[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_image,null);
        }

        ImageView imageView =(ImageView)view.findViewById(R.id.image);

        String baseUrl = "http://image.tmdb.org/t/p/w185";
        if (image[i].contains(baseUrl)){
            Picasso.with(context).load(image[i]).into(imageView);
        }else {
            String poster_url = baseUrl+image[i];
            Picasso.with(context).load(poster_url).into(imageView);
        }


        return view;
    }
}
