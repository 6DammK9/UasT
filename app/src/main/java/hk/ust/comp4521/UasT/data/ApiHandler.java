/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT.data;

import hk.ust.comp4521.UasT.json.ApiResponseBase;


public abstract class ApiHandler<T extends ApiResponseBase> {
	public abstract void onSuccess(T response);
	public abstract void onFailure(String message);
}
