package com.slb.poker.model;

import android.widget.ImageView;
import android.widget.TextView;

public class OptionalCompViewMap {
	private ImageViewItem one = null;
	private ImageViewItem two = null;
	private ImageViewItem three = null;
	private ImageViewItem four = null;
	private ImageView block = null;
	private TextView blockText = null;
	
	public OptionalCompViewMap(ImageViewItem one,ImageViewItem two,ImageViewItem three,ImageViewItem four,
			ImageView block,TextView blockText){
		this.one = one;
		this.two = two;
		this.three = three;
		this.four = four;
		this.block = block;
		this.blockText = blockText;
	}
	public ImageViewItem getOne() {
		return one;
	}

	public void setOne(ImageViewItem one) {
		this.one = one;
	}

	public ImageViewItem getTwo() {
		return two;
	}

	public void setTwo(ImageViewItem two) {
		this.two = two;
	}

	public ImageViewItem getThree() {
		return three;
	}

	public void setThree(ImageViewItem three) {
		this.three = three;
	}

	public ImageViewItem getFour() {
		return four;
	}

	public void setFour(ImageViewItem four) {
		this.four = four;
	}

	public ImageView getBlock() {
		return block;
	}
	public void setBlock(ImageView block) {
		this.block = block;
	}
	public TextView getBlockText() {
		return blockText;
	}
	public void setBlockText(TextView blockText) {
		this.blockText = blockText;
	}
	public ImageViewItem getTargetOptionalCompViews(OptionalCompEnum oce) {
		if(oce==OptionalCompEnum.ONE)
			return one;
		else if(oce == OptionalCompEnum.TWO)
			return two;
		else if(oce == OptionalCompEnum.THREE)
			return three;
		else
			return four;
	}
}
