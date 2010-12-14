/*
 * Copyright (C) 2007-2010 Geometer Plus <contact@geometerplus.com>
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

package org.geometerplus.zlibrary.ui.android.application;

import java.util.*;

import android.view.Menu;
import android.view.MenuItem;

import org.geometerplus.zlibrary.core.application.ZLApplication;
import org.geometerplus.zlibrary.core.application.ZLApplicationWindow;

import org.geometerplus.zlibrary.text.view.ZLRect;
import org.geometerplus.zlibrary.ui.android.view.ZLAndroidWidget;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidLibrary;

import org.geometerplus.zlibrary.ui.android.R;

public final class ZLAndroidApplicationWindow extends ZLApplicationWindow {
	private final HashMap<MenuItem,ZLApplication.Menubar.PlainItem> myMenuItemMap =
		new HashMap<MenuItem,ZLApplication.Menubar.PlainItem>();

	private class MenuBuilder extends ZLApplication.MenuVisitor {
		private int myItemCount = Menu.FIRST;
		private final Stack<Menu> myMenuStack = new Stack<Menu>();

		private MenuBuilder(Menu menu) {
			myMenuStack.push(menu);
		}
		protected void processSubmenuBeforeItems(ZLApplication.Menubar.Submenu submenu) {
			myMenuStack.push(myMenuStack.peek().addSubMenu(0, myItemCount++, Menu.NONE, submenu.getMenuName()));	
		}
		protected void processSubmenuAfterItems(ZLApplication.Menubar.Submenu submenu) {
			myMenuStack.pop();
		}
		protected void processItem(ZLApplication.Menubar.PlainItem item) {
			MenuItem menuItem = myMenuStack.peek().add(0, myItemCount++, Menu.NONE, item.getTitle());
			try {
				final String fieldName = "ic_menu_" + item.getActionId().toLowerCase();
				menuItem.setIcon(R.drawable.class.getField(fieldName).getInt(null));
			} catch (NoSuchFieldException e) {
			} catch (IllegalAccessException e) {
			}
			menuItem.setOnMenuItemClickListener(myMenuListener);
			myMenuItemMap.put(menuItem, item);
		}
	}

	private final MenuItem.OnMenuItemClickListener myMenuListener =
		new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				getApplication().doAction(myMenuItemMap.get(item).getActionId());
				return true;
			}
		};

	public ZLAndroidApplicationWindow(ZLApplication application) {
		super(application);
	}

	public void buildMenu(Menu menu) {
		new MenuBuilder(menu).processMenu(getApplication());
		refreshMenu();
	}

	@Override
	protected void refreshMenu() {
		for (Map.Entry<MenuItem,ZLApplication.Menubar.PlainItem> entry : myMenuItemMap.entrySet()) {
			final String actionId = entry.getValue().getActionId();
			final ZLApplication application = getApplication();
			entry.getKey().setVisible(application.isActionVisible(actionId) && application.isActionEnabled(actionId));
		}
	}

	public void initMenu() {
		// TODO: implement
	}

	/*private ZLAndroidViewWidget myViewWidget;
	protected ZLAndroidViewWidget getViewWidget() {
		if (myViewWidget == null) {
			myViewWidget = new ZLAndroidViewWidget();
		}
		return myViewWidget;
	}*/

	protected void repaintView() {
		final ZLAndroidWidget widget = 
			((ZLAndroidLibrary)ZLAndroidLibrary.Instance()).getWidget();
		// I'm not sure about threads, so postInvalidate() is used instead of invalidate()
		widget.postInvalidate();
	}

	/*protected void startViewAutoScrolling(int viewPage) {
		getViewWidget().startAutoScrolling(viewPage);
	}*/

	public void onEpdRepaintFinished() {
		((ZLAndroidLibrary)ZLAndroidLibrary.Instance()).onEpdRepaintFinished();
	}

	public void close() {
		((ZLAndroidLibrary)ZLAndroidLibrary.Instance()).finish();
	}

	public void notifyApplicationChanges(boolean singleChange) {
		((ZLAndroidLibrary)ZLAndroidLibrary.Instance()).notifyApplicationChanges(singleChange);
	}

	public void notifyApplicationChanges(ZLRect rect) {
		((ZLAndroidLibrary)ZLAndroidLibrary.Instance()).notifyApplicationChanges(rect);
	}
}
