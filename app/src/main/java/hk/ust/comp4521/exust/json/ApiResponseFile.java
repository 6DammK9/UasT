package hk.ust.comp4521.exust.json;

public class ApiResponseFile {
	int statusCode;
	org.apache.http.Header[] headers;
	byte[] responseBytes;

	public void load(int a, org.apache.http.Header[] b, byte[] c) {
		statusCode = a;
		headers = b;
		responseBytes = c;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public org.apache.http.Header[] getHeaders() {
		return headers;
	}

	public byte[] GetResponseBytes () {
		return responseBytes;
	}
}
