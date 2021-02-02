package com.springcloud;

import java.net.SocketTimeoutException;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import com.netflix.hystrix.exception.HystrixTimeoutException;

@Configuration
public class FallbackConfiguration implements FallbackProvider {

	private static final String DELAY_RESPONSE = "is very slow.";
	private static final String NOT_AVAILABLE = "is not available.";
	
	/**
	 * The route this fallback will be used for.
	 * 
	 * @return The route the fallback will be used for.
	 */
	@Override // fallback을 등록할 route return
	public String getRoute() {
		//return "consumer";
		return "*";
	}

	/**
	 * Provides a fallback response based on the cause of the failed execution.
	 *
	 * @param route The route the fallback is for
	 * @param cause cause of the main method failure, may be <code>null</code>
	 * @return the fallback response
	 */
	@Override // fallback 발생 시 호출되는 method
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		System.out.println("##### zuul to "+route+": "+cause.toString());
		if (cause instanceof SocketTimeoutException) {
			return new GatewayClientResponse(HttpStatus.GATEWAY_TIMEOUT, route+" "+DELAY_RESPONSE);
		} else {			
			return new GatewayClientResponse(HttpStatus.INTERNAL_SERVER_ERROR, route+" "+NOT_AVAILABLE);
		}
	}
}
