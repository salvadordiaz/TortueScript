package fr.salvadordiaz.gwt.tortuescript.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

import fr.salvadordiaz.gwt.tortuescript.client.app.ApplicationGinjector;
import fr.salvadordiaz.gwt.tortuescript.client.layout.Navbar;

public class TortueScript implements EntryPoint {

	public void onModuleLoad() {
		ApplicationGinjector ginjector = GWT.create(ApplicationGinjector.class);
		RootPanel.get().add(ginjector.display());
		ginjector.getHistoryHandler().handleCurrentHistory();
	}

}
