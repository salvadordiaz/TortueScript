package fr.salvadordiaz.gwt.tortuescript.client.app;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceHistoryHandler;

import fr.salvadordiaz.gwt.tortuescript.client.layout.FluidContainer;

@GinModules({ ApplicationModule.class })
public interface ApplicationGinjector extends Ginjector {
	PlaceHistoryHandler getHistoryHandler();

	FluidContainer display();
}
