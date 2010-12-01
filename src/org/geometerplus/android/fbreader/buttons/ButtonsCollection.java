/*
 * Copyright (C) 2010 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader.buttons;

import java.util.LinkedList;
import java.util.List;


public class ButtonsCollection {
	private static ButtonsCollection ourInstance;

	private ButtonsCollection() {
	}

	public static ButtonsCollection Instance() {
		if (ourInstance == null) {
			ourInstance = new ButtonsCollection();
		}
		return ourInstance;
	}


	private LinkedList<AbstractButton> myButtons;

	private void createButtons() {
		if (myButtons != null) {
			return;
		}
		myButtons = new LinkedList<AbstractButton>();
		myButtons.add(new GotoPageItem());
	}

	private void collectDefaultButtons(List<AbstractButton> buttons) {
		createButtons();
		buttons.addAll(myButtons);
	}


	public void collectButtons(List<AbstractButton> buttons) {
		buttons.clear();
		// TODO: load buttons from database;
		if (buttons.isEmpty()) {
			collectDefaultButtons(buttons);
		}
	}
}
