package com.slb.poker.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.slb.poker.R;
import com.slb.poker.utils.Property;
import com.slb.poker.utils.Util;

import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MallActivity extends CompsActivity{
	private void addGoods(List<Map<String,Object>> list){
		int previousId = R.id.ddz_title_id;
		int height = 200;
		for(Map<String,Object> item : list){
			ImageView bottomLine = createBackGround();
			addButtomLine(bottomLine,previousId,height);
			
			ImageView GoodsIntroImageView = createGoodsIntroImage();
			//add introduction images
			addGoodsIntroImage(GoodsIntroImageView,bottomLine.getId());
			//add titles
			addGoodsIntroTitles(createGoodsIntroTitlesImage(item.get("title").toString(),item.get("subtitle").toString()),GoodsIntroImageView.getId());
			//add discount tickets
			ImageView goodsDiscountTicketsView = createGoodsDiscountTicketsView();
			addGoodsDiscountTicketsView(goodsDiscountTicketsView,GoodsIntroImageView.getId());
			addGoodsDiscountTicketsText(createGoodsDiscountTicketsText(item.get("discount").toString()),
					goodsDiscountTicketsView.getId(),
					-(getBitmapFromImageView(goodsDiscountTicketsView).getWidth()*3)/5);
			
			//add buy buttom
			ImageView goodsBuyBtnView = createGoodsBuyButton(item.get("discount").toString(),item.get("price").toString(),getMessageFromItem(item));
			addGoodsBuyButton(goodsBuyBtnView,GoodsIntroImageView.getId());
			addGoodsBuyButtonText(createGoodsBuyButtonText(item.get("price").toString()),goodsBuyBtnView.getId());
			previousId = bottomLine.getId();
		}
	}
	private String getMessageFromItem(Map<String,Object> item){
		String message = item.get("message").toString();
		String title = item.get("title").toString();
		String subtitle = item.get("subtitle").toString();
		String price = item.get("price").toString();
		String coins = item.get("discount").toString();
		return message.replaceAll("<title>", title).
		replaceAll("<subtitle>", subtitle).replaceAll("<price>", price).replaceAll("<discount>", coins);
	}
	private void addGoodsBuyButtonText(TextView textView,int id){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_LEFT, id);
		params.addRule(RelativeLayout.ALIGN_BOTTOM, id);
		params.leftMargin = 30;
		params.bottomMargin = 10;
		addViewToContainer(textView, params);
	}
	private TextView createGoodsBuyButtonText(String price){
		TextView textView = new TextView(this);
		textView.setText("￥"+price);
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(16);
		return textView;
	}
	private ImageView createGoodsBuyButton(String discount,String price,String message){
		Options options = new Options();
		options.inScaled = true;
		ImageView imageView = buildImageView(R.drawable.quick_buy_click_btn, options);
		imageView.setId(Util.getUniqueViewId());
		addToClickScaleView(imageView,createMap(Property.BUY_IDENTITY,price,message,discount));
		return imageView;
	}
	private void addGoodsBuyButton(ImageView view,int id){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.ALIGN_TOP,id);
		params.rightMargin = 40;
		addViewToContainer(view, params);
	}
	private void addGoodsDiscountTicketsText(TextView textView,int id, int leftMargin){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.RIGHT_OF, id);
		params.addRule(RelativeLayout.ALIGN_BOTTOM, id);
		params.bottomMargin = 10;
		params.leftMargin = leftMargin;
		addViewToContainer(textView, params);
	}
	private TextView createGoodsDiscountTicketsText(String text){
		TextView textView = new TextView(this);
		textView.setText(text);
		textView.setTextSize(8);
		textView.setTextColor(Color.WHITE);
		return textView;
	}
	private ImageView createGoodsDiscountTicketsView(){
		Options options = new Options();
		options.inScaled = true;
		ImageView imageView = buildImageView(R.drawable.recharge_free, options);
		imageView.setId(Util.getUniqueViewId());
		return imageView;
	}
	private void addGoodsDiscountTicketsView(ImageView view,int goodsIntroViewId){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.leftMargin = (getScreenWidth()/2)+20;
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.ALIGN_TOP, goodsIntroViewId);
		addViewToContainer(view, params);
	}
	private void addGoodsIntroTitles(TextView[] textViews,int goodsIntroImageViewId){
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(0,0);
		params1.width = params1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params1.addRule(RelativeLayout.RIGHT_OF, goodsIntroImageViewId);
		params1.addRule(RelativeLayout.ALIGN_TOP,goodsIntroImageViewId);
		params1.leftMargin = 40;
		addViewToContainer(textViews[0], params1);
		
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(0, 0);
		params2.width = params2.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params2.addRule(RelativeLayout.RIGHT_OF,goodsIntroImageViewId);
		params2.addRule(RelativeLayout.ALIGN_BOTTOM, goodsIntroImageViewId);
		params2.addRule(RelativeLayout.ALIGN_LEFT, textViews[0].getId());
		addViewToContainer(textViews[1], params2);
	}
	private TextView[] createGoodsIntroTitlesImage(String title,String subTitle){
		TextView titleView = new TextView(this);
		titleView.setText(title);
		titleView.setTextColor(Color.WHITE);
		titleView.setTextSize(16);
		titleView.setId(Util.getUniqueViewId());
		
		TextView subTitleView = new TextView(this);
		subTitleView.setText(subTitle);
		subTitleView.setTextColor(Color.GRAY);
		subTitleView.setTextSize(14);
		subTitleView.setId(Util.getUniqueViewId());
		return new TextView[]{titleView,subTitleView};
	}
	private ImageView createGoodsIntroImage(){
		Options options = new Options();
		options.inScaled = true;
		ImageView iview = buildImageView(R.drawable.quick_buy_logo, options);
		iview.setId(Util.getUniqueViewId());
		return iview;
	}
	private void addGoodsIntroImage(ImageView imageView,int bottomlineId){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_BOTTOM, bottomlineId);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.leftMargin = 30;
		params.bottomMargin = 40;
		addViewToContainer(imageView, params);
	}
	private ImageView createBackGround(){
		Options options = new Options();
		options.inScaled = true;
		ImageView iview = buildImageView(R.drawable.more_gold_1_mg_line, options);
		iview.setId(Util.getUniqueViewId());
		iview.setScaleType(ScaleType.FIT_XY);
		return iview;
	}
	private void addButtomLine(ImageView imageView,int privousId,int height){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.topMargin = height;
		params.addRule(RelativeLayout.BELOW, privousId);
		addViewToContainer(imageView, params);
	}
	@Override
	public String getCompsTitle() {
		return "充值";
	}
	@Override
	public void dobody() {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("title", "购买黄金VIP");
		map.put("subtitle", "包月只需10元，享黄金VIP特权");
		map.put("price", "10.00");
		map.put("discount", "500金币");
		map.put("message", "恭喜您！您已<title>,成功获得<discount>");
		
		list.add(map);
		
		Map<String,Object> map2 = new HashMap<String,Object>();
		map2.put("title", "购买黄金VIP");
		map2.put("subtitle", "包月只需15元，享黄金VIP特权");
		map2.put("price", "15.00");
		map2.put("discount", "1000金币");
		map2.put("message", "恭喜您！您已<title>,成功获得<discount>");
		list.add(map2);

		Map<String,Object> map3 = new HashMap<String,Object>();
		map3.put("title", "购买黄金VIP");
		map3.put("subtitle", "包月只需20元，享黄金VIP特权");
		map3.put("price", "20.00");
		map3.put("discount", "1500金币");
		map3.put("message", "恭喜您！您已<title>,成功获得<discount>");
		list.add(map3);

		Map<String,Object> map4 = new HashMap<String,Object>();
		map4.put("title", "购买黄金VIP");
		map4.put("subtitle", "包月只需25元，享黄金VIP特权");
		map4.put("price", "25.00");
		map4.put("discount", "2000金币");
		map4.put("message", "恭喜您！您已<title>,成功获得<discount>");
		list.add(map4);
		addGoods(list);
	}
	private Map<String, String> createMap(String type, String price,
			String message,String discount) {
		return createMap(type,new String[][]{{"price",price},{"message",message},{"discount",discount}});
	}
	@Override
	protected void clickedBackcall(View v, Map<?, ?> map) {
		Object type = map.get(Property.TYPE_IDENTITY);
//		String price = ConvertFrom(map.get("price"));
		String message = ConvertFrom(map.get("message"));
		String discount =  ConvertFrom(map.get("discount"));
		if(Property.BUY_IDENTITY.equals(type)){
			int coins = Integer.parseInt(discount.replace("金币", ""));
			addCoins(coins);
			showMessage(message);
		}				
	}
}
