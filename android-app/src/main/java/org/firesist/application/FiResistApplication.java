package org.firesist.application;


import android.app.Application;
import org.firesist.sockethandler.FiSocketHandler;
import android.content.Context;

public class FiResistApplication  extends Application {

	/**
	  * Creates singleton objects
	  */
	@Override
	public void onCreate() {
		super.onCreate();
		createSingletons();
	}

	/**
	 * Constructs all Singleton Objects
	 */
	protected void createSingletons() {
		FiSocketHandler.initInstance();
	}
}


