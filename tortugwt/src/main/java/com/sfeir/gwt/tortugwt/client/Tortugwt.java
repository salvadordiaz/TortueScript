package com.sfeir.gwt.tortugwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.sfeir.gwt.tortugwt.client.app.ApplicationGinjector;

public class Tortugwt implements EntryPoint {


	public void onModuleLoad() {
		ApplicationGinjector ginjector = GWT.create(ApplicationGinjector.class);
		RootPanel.get().add(ginjector.display());
		ginjector.getHistoryHandler().handleCurrentHistory();
	}

}
