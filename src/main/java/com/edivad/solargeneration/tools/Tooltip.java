package com.edivad.solargeneration.tools;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;

public class Tooltip {

	private static final Pattern COMPILE = Pattern.compile("@", Pattern.LITERAL);

	public static void showInfoShift(SolarPanelLevel solarPanelLevel, List<String> tooltip) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			int generation = (int) Math.pow(8, solarPanelLevel.ordinal());
			int transfer = ((int) Math.pow(8, solarPanelLevel.ordinal())) * 2;
			int capacity = ((int) Math.pow(8, solarPanelLevel.ordinal())) * 1000;

			addInformationLocalized(tooltip, "message.solargeneration.shift_info", generation, transfer, capacity);
		} 
		else
			addInformationLocalized(tooltip, "message.solargeneration.hold_shift");
	}

	public static void showInfoCtrl(int energy, List<String> tooltip) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
			addInformationLocalized(tooltip, "message.solargeneration.ctrl_info", energy);
		else
			addInformationLocalized(tooltip, "message.solargeneration.hold_ctrl");
	}

	private static void addInformationLocalized(List<String> tooltip, String key, Object... parameters) {
		String translated = I18n.format(key, parameters);
		translated = COMPILE.matcher(translated).replaceAll("\u00a7");
		Collections.addAll(tooltip, StringUtils.split(translated, "\n"));
	}
}
