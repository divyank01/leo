package com.auth;

import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.AuthConfig;

import org.leo.rest.auth.Authenticator;

public class SampleAuthenticator implements Authenticator {

	@Override
	public boolean validate(String key) {
		System.out.println(key);
		return true;
	}

}
