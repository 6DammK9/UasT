/**
 * COMP4521 Project
 *      Cheung Wai Yip
 *      Lau Tsz Hei
 *      Ho Kam Ming
 */

package hk.ust.comp4521.UasT.data;

import hk.ust.comp4521.UasT.json.ApiResponseBase;


public abstract class ApiHandler<T extends ApiResponseBase> {
	public abstract void onSuccess(T response);
	public abstract void onFailure(String message);
}
