/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

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
