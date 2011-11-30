package com.sfeir.gwt.tortugwt.client.app;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.sfeir.gwt.tortugwt.client.layout.FluidContainer;

@GinModules({ ApplicationModule.class })
public interface ApplicationGinjector extends Ginjector {
	PlaceHistoryHandler getHistoryHandler();

	FluidContainer display();
}
