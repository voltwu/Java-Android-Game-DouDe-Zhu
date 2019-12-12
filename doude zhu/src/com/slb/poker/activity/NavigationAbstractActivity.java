package com.slb.poker.activity;

public abstract class NavigationAbstractActivity extends BaseActivity{
   @Override    
    public void onBackPressed() {    
	   finishedActivity();
    }
}
