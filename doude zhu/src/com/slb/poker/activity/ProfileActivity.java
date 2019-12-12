package com.slb.poker.activity;

import java.util.Map;

import com.slb.poker.model.user.Player;
import com.slb.poker.utils.Property;
import com.slb.poker.utils.SoundEffectPlayer;
import com.slb.poker.utils.Util;
import com.slb.poker.views.SelectedBoxView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class ProfileActivity extends CompsActivity{
	private String name_name = "用户名";
	private String gender_name = "性别";
	private String header_name = "头像";
	private int leftmargin = 30;
	private int selectedBoxViewId = 0;
	private int nameEditTextId = 0;
	private int genderRadioGroupId = 0;
	private int genderRadioButtonNanId = 0;
	private int genderRadioButtonNvId = 0;
	private static Bitmap[] HeadImageViewBitmaps = null;
	@Override
	public String getCompsTitle() {
		return "档案";
	}
	@Override
	public void dobody() {
		selectedBoxViewId = Util.getUniqueViewId();
		nameEditTextId = Util.getUniqueViewId();
		genderRadioGroupId = Util.getUniqueViewId();
		genderRadioButtonNanId = Util.getUniqueViewId();
		genderRadioButtonNvId = Util.getUniqueViewId();
		
		Player player = getPlayer();
		int privousNameId = addNameArea(player.getName());
		privousNameId = addGenderArea(player.getGender(),privousNameId);
		privousNameId = addHeadImageArea(privousNameId,player.getHead_img());
		addSaveButtonView(privousNameId);
	}
	private void addSaveButtonView(int privousViewId){
		Button btn = new Button(this);
		btn.setText("确认修改");
		btn.setTextSize(16);
		btn.setOnClickListener(new SureModifyClickOperation());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.BELOW, privousViewId);
		params.addRule(RelativeLayout.ALIGN_LEFT,privousViewId);
		params.topMargin = 40;
		addViewToContainer(btn, params);
	}
	private int addHeadImageArea(int id,int head_img_drawabled_id){
		TextView nameTagView = creatTagView(header_name);
		addNameTagView(nameTagView,id);
		
		int lastPrivousTopViewId = addHeadImages(addClickListener(createHeadImages()),nameTagView.getId(),head_img_drawabled_id);
		return lastPrivousTopViewId;
	}
	private ImageView[] addClickListener(ImageView[] imageViews){
		for(ImageView imageView : imageViews){
			imageView.setOnClickListener(new HeadImageViewClickCallBack());
		}
		return imageViews;
	}
	private int addHeadImages(ImageView[] views,int textViewId,int head_img_drawabled_id){
		int lineTopPriviousId = 0;
		int privousId = textViewId;
		for(int index=0; index<views.length; index++){
			if(index%8==0 && index!=0){
				addHeadImageView(views[index],lineTopPriviousId,true);
			}else{
				addHeadImageView(views[index],privousId,false);
			}
			if(isSelectedView(views[index],head_img_drawabled_id)){
				addSelectedOnView(views[index]);
			}
			privousId = views[index].getId();
			if(index==0 || index%8 == 0){
				lineTopPriviousId = privousId;
			}
		}
		return lineTopPriviousId;
	}
	private void addSelectedOnView(ImageView imageView){
		removePrivousBoxView();
		RelativeLayout.LayoutParams params = (LayoutParams) imageView.getLayoutParams();
		int width = params.width+2;
		int height = params.height+2;		
		addANewBoxView(width,height,imageView.getId(),Integer.parseInt(imageView.getTag().toString()));
	}
	private void addANewBoxView(int width,int height,int id,int tag){
		SelectedBoxView sbview = new SelectedBoxView(this);
		sbview.setTag(tag);
		sbview.setId(selectedBoxViewId);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		params.addRule(RelativeLayout.ALIGN_BOTTOM, id);
		params.addRule(RelativeLayout.ALIGN_LEFT, id);
		params.leftMargin = -1;
		addViewToContainer(sbview, params);
	}
	private void removePrivousBoxView(){
		if(findViewById(selectedBoxViewId)!=null){
			removeViewFromContainer(findViewById(selectedBoxViewId));
		}
	}
	private void addHeadImageView(ImageView imageView,int privousId,boolean isHeadLineTop){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = 60;
		if(isHeadLineTop){
			params.addRule(RelativeLayout.BELOW,privousId);
			params.addRule(RelativeLayout.ALIGN_LEFT, privousId);
			params.topMargin = leftmargin;
		}else{
			params.addRule(RelativeLayout.ALIGN_BOTTOM,privousId);
			params.addRule(RelativeLayout.RIGHT_OF, privousId);
			params.leftMargin = leftmargin;
		}
		addViewToContainer(imageView, params);
	}
	/**
	 * this method controls all head images,ImageView is added to this method, ImageView is added to optional choice views.
	 * @return ImageView[] ImageViews that to be shown
	 */
	private ImageView[] createHeadImages(){
		ImageView[] profileAllHeadImages =  new ImageView[Property.HEADIMGS_DRAWABLEID.length];
		if(HeadImageViewBitmaps==null){
			HeadImageViewBitmaps = new Bitmap[Property.HEADIMGS_DRAWABLEID.length];
			for(int i = 0; i < HeadImageViewBitmaps.length; i++){
				HeadImageViewBitmaps[i] = BitmapFactory.decodeResource(getResources(), Property.HEADIMGS_DRAWABLEID[i]);
			}
		}
		for(int i=0;i < HeadImageViewBitmaps.length;i++){
			profileAllHeadImages[i] = buildImageView(HeadImageViewBitmaps[i]);
			profileAllHeadImages[i].setId(Util.getUniqueViewId());
			profileAllHeadImages[i].setTag(Property.HEADIMGS_DRAWABLEID[i]);
		}
		return profileAllHeadImages;
	}
	private boolean isSelectedView(ImageView view,int head_img_drawabled_id){
		return Integer.parseInt(view.getTag().toString()) == head_img_drawabled_id;
	}
//	private ImageView setImageViewIdAndTag(int tagVal_drawableId){
//		ImageView imageView = buildImageView(tagVal_drawableId);
//		imageView.setId(Util.getUniqueViewId());
//		imageView.setTag(tagVal_drawableId);
//		return imageView;
//	}
	private int addGenderArea(int gender,int nameStringNameId){
		TextView nameTagView = creatTagView(gender_name);
		addNameTagView(nameTagView,nameStringNameId);
		addGenderRadio(createGenderRadio(gender),nameTagView.getId());
		return nameTagView.getId();
	}
	private RadioGroup createGenderRadio(int gender){
		RadioGroup gp = new RadioGroup(this);
		gp.setId(genderRadioGroupId);
		gp.setOrientation(RadioGroup.HORIZONTAL);
		RadioButton btn1 = new RadioButton(this);
		btn1.setText("男");
		btn1.setId(genderRadioButtonNanId);
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SoundEffectPlayer.getInstance().playClick();
			}
		});
		gp.addView(btn1);
		RadioButton btn2 = new RadioButton(this);
		btn2.setText("女");
		btn2.setId(genderRadioButtonNvId);
		btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SoundEffectPlayer.getInstance().playClick();
			}
		});
		gp.addView(btn2);
		if(gender==Property.GIRL){
			btn2.setChecked(true);
		}else if(gender==Property.BOY){
			btn1.setChecked(true);
		}
		return gp;
	}
	private void addGenderRadio(RadioGroup view,int id){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_BOTTOM, id);
		params.addRule(RelativeLayout.RIGHT_OF,id);
		params.leftMargin = leftmargin;
		params.bottomMargin = -20;
		addViewToContainer(view, params);
	}
	private int addNameArea(String name){
		TextView nameTagView = creatTagView(name_name);
		addNameTagView(nameTagView,-1);
		addNameInputView(createNameInputView(name),nameTagView.getId());
		return nameTagView.getId();
	}
	private void addNameInputView(EditText text,int id){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.RIGHT_OF, id);
		params.addRule(RelativeLayout.ALIGN_BOTTOM, id);
		params.leftMargin = leftmargin;
		addViewToContainer(text, params);
	}
	private EditText createNameInputView(String text){
		EditText editText = new EditText(this);
		editText.setText(text);
		editText.setId(nameEditTextId);
		editText.setBackgroundResource(android.R.drawable.editbox_dropdown_light_frame);
		return editText;
	}
	private TextView creatTagView(String text){
		TextView textView = new TextView(this);
		textView.setText(text);
		textView.setTextSize(24);
		textView.setTextColor(Color.WHITE);
		textView.setId(Util.getUniqueViewId());
		return textView;
	}
	private void addNameTagView(TextView textView,int topId){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
		params.width = params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		if(topId == -1)
			params.topMargin = params.leftMargin = getScreenHeight()/8;
		else
		{
			params.addRule(RelativeLayout.BELOW, topId);
			params.addRule(RelativeLayout.ALIGN_RIGHT, topId);
			params.topMargin = 20;
		}
		addViewToContainer(textView, params);
	}
	@Override
	protected void clickedBackcall(View v, Map<?, ?> map) {
	}
	public class HeadImageViewClickCallBack implements OnClickListener{
		@Override
		public void onClick(View paramView) {
			SoundEffectPlayer.getInstance().playClick();
			addSelectedOnView((ImageView)paramView);
		}
	}
	public class SureModifyClickOperation implements OnClickListener{
		@Override
		public void onClick(View view) {
			view.setClickable(false);
			String name = valid(findViewById(nameEditTextId,EditText.class).getText().toString());
			if(name.length()==0){
				showMessage("修改失败, 名称不能为空！");
			}
			else if(name.length()>8){
				showMessage("修改失败, 名称不能超过8个字符！");
			}else{
				int checkedBtnId = findViewById(genderRadioGroupId,RadioGroup.class).getCheckedRadioButtonId();
				int gender = checkedBtnId == genderRadioButtonNanId?Property.BOY:Property.GIRL;
				int head_img_drawable_id = Integer.parseInt(findViewById(selectedBoxViewId, SelectedBoxView.class).getTag().toString());
				Player player = getPlayer();
				player.setName(name);
				player.setGender(gender);
				player.setHead_img(head_img_drawable_id);
				savePlayer(player);
				showMessage("修改成功");
			}
			SoundEffectPlayer.getInstance().playClick();
			view.setClickable(true);
		}
		public String valid(String name){
			return name.trim().replaceAll("\t", "").replaceAll("\n","");
		}
	}
}
