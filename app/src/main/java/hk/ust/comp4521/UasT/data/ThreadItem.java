/**
 * COMP4521 Project
 *      Cheung Wai Yip
 *      Lau Tsz Hei
 *      Ho Kam Ming
 */

package hk.ust.comp4521.UasT.data;

import android.support.annotation.NonNull;

import java.util.Locale;

import hk.ust.comp4521.UasT.BaseFragment;


public abstract class ThreadItem extends DataType implements Comparable<ThreadItem> {
	protected String title, sub, key;

	public String getTitle() { return title; }
	public String getSub() { return sub; }
	public String getKey() { return key; }
	
	@Override
	public int compareTo(@NonNull ThreadItem another) {
		return 0;
	}
	
	public boolean filter(String filter) {
		return title.toLowerCase(Locale.US).contains(filter)
				|| sub.toLowerCase(Locale.US).contains(filter);
	}
	
	public BaseFragment[] getFragment() {
		return null;
	}
}

