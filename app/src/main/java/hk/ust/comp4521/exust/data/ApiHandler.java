package hk.ust.comp4521.exust.data;

import hk.ust.comp4521.exust.json.ApiResponseBase;


public abstract class ApiHandler<T extends ApiResponseBase> {
	public abstract void onSuccess(T response);
	public abstract void onFailure(String message);
}
