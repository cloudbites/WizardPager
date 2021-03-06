package com.tech.freak.wizardpager.model;

import android.app.Fragment;
import android.content.res.Resources;

import com.tech.freak.wizardpager.ui.ImageFragment;

public class ImagePage extends TextPage
{
	public ImagePage(ModelCallbacks callbacks, Resources resources, String name, String title)
	{
		super(callbacks, resources, name, title);
	}

	@Override
	public Fragment createFragment()
	{
		return ImageFragment.create(getKey());
	}

	public ImagePage setValue(String value)
	{
		_data.putString(DK_STRING, value);
		return this;
	}
}
