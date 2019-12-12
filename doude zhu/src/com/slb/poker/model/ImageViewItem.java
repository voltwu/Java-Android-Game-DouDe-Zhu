package com.slb.poker.model;

import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ImageViewItem {
	private ImageView imageView = null;
	private OnClickListener onClickListener = null;
	
	public ImageViewItem(ImageView imageView,OnClickListener onClickListener){
		this.imageView = imageView;
		this.onClickListener = onClickListener;
	}
	public ImageView getImageView(){
		return imageView;
	}
	public OnClickListener getOnClickListener(){
		return onClickListener;
	}
	public void setImageView(ImageView imageView){
		this.imageView = imageView;
	}
	public void setOnClickListener(OnClickListener onClickListener){
		this.onClickListener = onClickListener;
	}
}
