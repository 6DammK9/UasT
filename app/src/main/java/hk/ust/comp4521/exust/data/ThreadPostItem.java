package hk.ust.comp4521.exust.data;

import hk.ust.comp4521.exust.BaseFragment;
import hk.ust.comp4521.exust.ViewPostFragment;

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
