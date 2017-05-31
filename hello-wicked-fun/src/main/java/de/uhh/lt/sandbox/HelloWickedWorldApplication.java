package de.uhh.lt.sandbox;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class HelloWickedWorldApplication extends WebApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		return HelloWickedWorld.class;
	}

}
