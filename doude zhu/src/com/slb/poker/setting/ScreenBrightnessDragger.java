package com.slb.poker.setting;

import com.slb.poker.listener.DragSetListener;
import com.slb.poker.utils.ScreenBrightnessController;

public class ScreenBrightnessDragger implements DragSetListener{
	@Override
	public void invoke(double percent) {
		ScreenBrightnessController.getInstance().setBrightnessPercent((float)percent);
	}
}
