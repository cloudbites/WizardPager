/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tech.freak.wizardpager.model;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.res.Resources;
import android.text.TextUtils;

import com.tech.freak.wizardpager.ui.SingleChoiceFragment;

/**
 * A page representing a branching point in the wizard. Depending on which choice is selected, the
 * next set of steps in the wizard may change.
 */
public class BranchPage extends SingleFixedChoicePage
{
	private List<Branch> _branches = new ArrayList<Branch>();

	public BranchPage(ModelCallbacks callbacks, Resources resources, String name, String title)
	{
		super(callbacks, resources, name, title);
	}

	@Override
	public Page findByKey(String key)
	{
		if (getKey().equals(key)) {
			return this;
		}

		for (Branch branch : _branches) {
			Page found = branch.childPageList.findByKey(key);
			if (found != null) {
				return found;
			}
		}

		return null;
	}

	@Override
	public void flattenCurrentPageSequence(ArrayList<Page> destination)
	{
		super.flattenCurrentPageSequence(destination);
		for (Branch branch : _branches) {
			if (branch.choice.equals(_data.getString(Page.DK_STRING))) {
				branch.childPageList.flattenCurrentPageSequence(destination);
				break;
			}
		}
	}

	public BranchPage addBranch(String choice, Page... childPages)
	{
		PageList childPageList = new PageList(childPages);
		for (Page page : childPageList) {
			page.setParentKey(choice);
		}
		_branches.add(new Branch(choice, childPageList));
		return this;
	}

	@Override
	public Fragment createFragment()
	{
		return SingleChoiceFragment.create(getKey());
	}

	public String getOptionAt(int position)
	{
		return _branches.get(position).choice;
	}

	public int getOptionCount()
	{
		return _branches.size();
	}

	@Override
	public void getReviewItems(ArrayList<ReviewItem> dest)
	{
		dest.add(new ReviewItem(getTitle(), _data.getString(DK_STRING), getKey()));
	}

	@Override
	public boolean isCompleted()
	{
		return !TextUtils.isEmpty(_data.getString(DK_STRING));
	}

	@Override
	public void notifyDataChanged()
	{
		_callbacks.onPageTreeChanged();
		super.notifyDataChanged();
	}

	public BranchPage setValue(String value)
	{
		_data.putString(DK_STRING, value);
		return this;
	}

	private static class Branch
	{
		public String choice;
		public PageList childPageList;

		private Branch(String choice, PageList childPageList)
		{
			this.choice = choice;
			this.childPageList = childPageList;
		}
	}
}
