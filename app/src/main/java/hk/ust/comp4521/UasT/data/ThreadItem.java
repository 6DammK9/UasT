/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
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

