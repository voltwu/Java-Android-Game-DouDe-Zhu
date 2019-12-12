package com.slb.poker.setting;

import com.slb.poker.listener.DragSetListener;
import com.slb.poker.utils.SoundEffectPlayer;

public class SoundEffectVolumeDragger implements DragSetListener{
	@Override
	public void invoke(double percent) {
		SoundEffectPlayer.getInstance().setSoundEffectVolumePercent((float)percent);
	}
}
