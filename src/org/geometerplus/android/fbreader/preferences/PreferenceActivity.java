/*
 * Copyright (C) 2009-2010 Geometer Plus <contact@geometerplus.com>
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

package org.geometerplus.android.fbreader.preferences;

import android.preference.Preference;
import android.preference.PreferenceScreen;

import org.geometerplus.zlibrary.core.dialogs.ZLOptionsDialog;

import org.geometerplus.zlibrary.text.view.style.ZLTextStyleCollection;
import org.geometerplus.zlibrary.text.view.style.ZLTextBaseStyle;

import org.geometerplus.zlibrary.ui.android.library.ZLAndroidApplication;

import org.geometerplus.fbreader.optionsDialog.OptionsDialog;
import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.fbreader.Paths;

public class PreferenceActivity extends ZLPreferenceActivity {
	public PreferenceActivity() {
		super("Preferences");
	}

	/*private static final class ColorProfilePreference extends ZLSimplePreference {
		private final FBReaderApp myFBReader;
		private final Screen myScreen;
		private final String myKey;

		static final String createTitle(ZLResource resource, String resourceKey) {
			final ZLResource r = resource.getResource(resourceKey);
			return r.hasValue() ? r.getValue() : resourceKey;
		}

		ColorProfilePreference(Context context, FBReaderApp fbreader, Screen screen, String key, String title) {
			super(context);
			myFBReader = fbreader;
			myScreen = screen;
			myKey = key;
			setTitle(title);
		}

		@Override
		public void onAccept() {
		}

		@Override
		public void onClick() {
			myScreen.setSummary(getTitle());
			myFBReaderApp.setColorProfileName(myKey);
			myScreen.close();
		}
	}*/

	@Override
	protected void init() {
		final Category libraryCategory = createCategory("Library");
		libraryCategory.addPreference(new ZLStringOptionPreference(
			this, Paths.BooksDirectoryOption,
			libraryCategory.Resource, "path"
		));
		libraryCategory.addPreference(new ZLBooleanPreference(
			this, ZLAndroidApplication.Instance().NetworkLibraryEnabled,
			libraryCategory.Resource, "networkLibrary"
		));

		final Category lookNFeelCategory = createCategory("LookNFeel");
		//lookNFeelCategory.addOption(ZLAndroidApplication.Instance().AutoOrientationOption, "autoOrientation");
		//lookNFeelCategory.addOption(ZLAndroidApplication.Instance().ShowStatusBarOption, "showStatusBar");
		//lookNFeelCategory.addOption(ZLAndroidApplication.Instance().DontTurnScreenOffOption, "dontTurnScreenOff");
		//lookNFeelCategory.addPreference(new ScrollbarTypePreference(this, lookNFeelCategory.Resource, "scrollbarType"));

		final FBReaderApp fbReader = (FBReaderApp)FBReaderApp.Instance();

		final Screen marginsScreen = lookNFeelCategory.createPreferenceScreen("margins");
		final Category marginsCategory = marginsScreen.createCategory(null);
		marginsCategory.addPreference(new ZLIntegerRangePreference(
			this, marginsCategory.Resource.getResource("left"),
			fbReader.LeftMarginOption
		));
		marginsCategory.addPreference(new ZLIntegerRangePreference(
			this, marginsCategory.Resource.getResource("right"),
			fbReader.RightMarginOption
		));
		marginsCategory.addPreference(new ZLIntegerRangePreference(
			this, marginsCategory.Resource.getResource("top"),
			fbReader.TopMarginOption
		));
		marginsCategory.addPreference(new ZLIntegerRangePreference(
			this, marginsCategory.Resource.getResource("bottom"),
			fbReader.BottomMarginOption
		));

		final Screen appearanceScreen = lookNFeelCategory.createPreferenceScreen("appearanceSettings");
		final Category appearanceCategory = appearanceScreen.createCategory(null);
		final ZLTextStyleCollection collection = ZLTextStyleCollection.Instance();
		final ZLTextBaseStyle baseStyle = collection.getBaseStyle();
		appearanceCategory.addPreference(new FontOption(
			this, appearanceCategory.Resource, "font",
			baseStyle.FontFamilyOption
		));
		appearanceCategory.addPreference(new ZLIntegerRangePreference(
			this, appearanceCategory.Resource.getResource("fontSize"),
			baseStyle.FontSizeOption
		));
		appearanceCategory.addPreference(new ZLBooleanPreference(
			this, baseStyle.BoldOption,
			appearanceCategory.Resource, "bold"
		));
		appearanceCategory.addPreference(new ZLBooleanPreference(
			this, baseStyle.ItalicOption,
			appearanceCategory.Resource, "italic"
		));
		appearanceCategory.addPreference(new ZLBooleanPreference(
			this, baseStyle.AutoHyphenationOption,
			appearanceCategory.Resource, "autoHyphenations"
		));

		final ZLOptionsDialog dlg = new OptionsDialog(fbReader).getDialog();
		final Screen formatScreen = appearanceCategory.createPreferenceScreen("format");
		final Screen stylesScreen = appearanceCategory.createPreferenceScreen("styles");
		final Screen colorsScreen = appearanceCategory.createPreferenceScreen("colors");
		formatScreen.setOnPreferenceClickListener(
				new PreferenceScreen.OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						dlg.run(0);
						return true;
					}
				}
		);
		stylesScreen.setOnPreferenceClickListener(
				new PreferenceScreen.OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						dlg.run(1);
						return true;
					}
				}
		);
		colorsScreen.setOnPreferenceClickListener(
				new PreferenceScreen.OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						dlg.run(2);
						return true;
					}
				}
		);

		/*
		final Screen statusLineScreen = lookNFeelCategory.createPreferenceScreen("scrollBar");
		final Category statusLineCategory = statusLineScreen.createCategory(null);

		String[] scrollBarTypes = {"hide", "show", "showAsProgress", "showAsFooter"};
		statusLineCategory.addPreference(new ZLChoicePreference(
			this, statusLineCategory.Resource, "scrollbarType",
			fbReader.ScrollbarTypeOption, scrollBarTypes
		));
		*/

		/*
		final Screen colorProfileScreen = lookNFeelCategory.createPreferenceScreen("colorProfile");
		final Category colorProfileCategory = colorProfileScreen.createCategory(null);
		final ZLResource resource = colorProfileCategory.Resource;
		colorProfileScreen.setSummary(ColorProfilePreference.createTitle(resource, fbreader.getColorProfileName()));
		for (String key : ColorProfile.names()) {
			colorProfileCategory.addPreference(new ColorProfilePreference(
				this, fbreader, colorProfileScreen, key, ColorProfilePreference.createTitle(resource, key)
			));
		}
		*/

		/*final Category scrollingCategory = createCategory("Scrolling");
		final ScrollingPreferences scrollingPreferences = ScrollingPreferences.Instance();
		scrollingCategory.addOption(scrollingPreferences.FlickOption, "flick");
		scrollingCategory.addOption(scrollingPreferences.VolumeKeysOption, "volumeKeys");
		scrollingCategory.addOption(scrollingPreferences.HorizontalOption, "horizontal");*/
	}
}
