package com.slb.poker.activity;

public abstract class NavigationMainActivity extends BaseActivity{
   @Override    
    public void onBackPressed() {
	   startToAnotherActivity(BoardActivity.class);
    }
}
