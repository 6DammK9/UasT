package hk.ust.comp4521.UasT.data;

import hk.ust.comp4521.UasT.BaseFragment;
import hk.ust.comp4521.UasT.ViewPostFragment;

public abstract class ThreadPostItem extends ThreadItem {
	public String getAuthorId() {
		return authorId;
	}

	protected String authorId;
	
	public BaseFragment[] getFragment() {
		BaseFragment[] fragments = new BaseFragment[1];
		ViewPostFragment post = new ViewPostFragment();
		fragments[0] = post;
		post.setCode(this);
		return fragments;
	}
}
