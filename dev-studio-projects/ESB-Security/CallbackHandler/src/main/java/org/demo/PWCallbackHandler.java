package org.demo;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.ws.security.WSPasswordCallback;
import java.io.IOException;

public class PWCallbackHandler implements CallbackHandler {
	
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {

		for (int i = 0; i < callbacks.length; i++) {
			WSPasswordCallback pwcb = (WSPasswordCallback) callbacks[i];
			int usage = pwcb.getUsage();
			String id = pwcb.getIdentifier();

			if (usage == WSPasswordCallback.USERNAME_TOKEN) {
				// Getting password
				if ("admin".equals(id)) {
					pwcb.setPassword("admin");
				
				} else {
					pwcb.setPassword("");
				} 
			}
		}
	}
}
