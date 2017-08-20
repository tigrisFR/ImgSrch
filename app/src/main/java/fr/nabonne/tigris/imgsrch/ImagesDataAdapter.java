package fr.nabonne.tigris.imgsrch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tigris on 8/20/17.
 * This is part of the View in our MVP architecture. It adapts Image data from the model into
 * ImageView to fill our results container view
 */

public class ImagesDataAdapter extends RecyclerView.Adapter {
    final List<MVPContracts.ISearchModel.ImageData> data;

    public ImagesDataAdapter(List<MVPContracts.ISearchModel.ImageData> data) {
        this.data = data;
    }

    static class ImageDataViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_grid_item)
        ImageView imageView;

        public ImageDataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ImageDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // This is where we inflate the item views for the RecyclerView's pool of recyclable views
        // Here we have only one type of item so it's easy
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_grid_item, parent, false);

        return new ImageDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // This is where we fill the item view with it's content

        // Fetch the bitmap via bitmap helper lib and attach to ImageView
        Picasso.with(holder.itemView.getContext())
                .load(data.get(position).url)
                .resize(120, 120)
                .centerCrop()
                .into(((ImageDataViewHolder)holder).imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
