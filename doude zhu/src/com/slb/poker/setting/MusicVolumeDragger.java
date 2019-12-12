package com.slb.poker.setting;

import com.slb.poker.listener.DragSetListener;
import com.slb.poker.utils.MusicPlayer;

public class MusicVolumeDragger implements DragSetListener {
	@Override
	public void invoke(double percent) {
		MusicPlayer.getInstance().setStreamVolume((float)percent);
	}
}
