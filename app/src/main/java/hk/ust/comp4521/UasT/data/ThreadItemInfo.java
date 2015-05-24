/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadItemInfo {
	boolean filter() default false;
	boolean sort() default false;
	boolean favorite() default false;
	boolean add() default false;
	int layout();
	int header() default -1;
	String title() default "List";
	String hint() default "";
	String type();
	String typeName();
}
