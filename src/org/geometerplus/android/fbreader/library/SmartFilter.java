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

package org.geometerplus.android.fbreader.library;

import java.util.List;

import org.geometerplus.fbreader.formats.PluginCollection;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.zlibrary.core.resources.ZLResource;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class SmartFilter implements Runnable {
	private Activity myParent;
	private ReturnRes myReturnRes;
	private List<FileItem> myItems;
	private ZLFile myFile;

	public SmartFilter(Activity parent, ReturnRes returnRes) {
		myParent = parent;
		myItems = returnRes.getItems();
		myReturnRes = returnRes;
	}
	
	public void setPreferences(ZLFile file) {
		myFile = file;
		myReturnRes.refresh();
	}

	public void run() {
		try {
			getItems();
		} catch (Exception e) {
			Log.e(FileManager.LOG, e.getMessage());
		}
	}
	
	private void getItems() {
		if (myFile == null)
			myParent.runOnUiThread(myReturnRes);

		try{
			for (ZLFile file : myFile.children()) {
				if (!Thread.currentThread().isInterrupted()) {
					if (file.isDirectory() || file.isArchive())
						myItems.add(new FileItem(file));
					else if (PluginCollection.Instance().getPlugin(file) != null)
						myItems.add(new FileItem(file));
					myParent.runOnUiThread(myReturnRes);
				}
			}
			myParent.runOnUiThread(myReturnRes);
		}catch (Exception e) {
			Log.v(FileManager.LOG, "Exception");
			myParent.runOnUiThread(myReturnRes);
			showPermissionDeniedToast();
			myParent.finish();
		}
	}
	
	protected void showPermissionDeniedToast() {
		Toast.makeText(myParent,
			ZLResource.resource("fmanagerView").getResource("permission_denied").getValue(),
			Toast.LENGTH_SHORT
		).show();
	}
}
