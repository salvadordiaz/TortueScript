package com.sfeir.gwt.tortugwt.client.sidebar;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.sfeir.gwt.tortugwt.client.LocalizedCommands;

public class SidebarActivity extends AbstractActivity {

	public interface SidebarDisplay extends IsWidget {
		void addItem(String key);
	}

	private final List<String> exampleNames = ImmutableList.of("Box", "Flower", "OtherFlower");

	private final SidebarDisplay sidebarDisplay;
	private final LocalizedCommands commands;

	public SidebarActivity(SidebarDisplay sidebarDisplay, LocalizedCommands commands) {
		this.sidebarDisplay = sidebarDisplay;
		this.commands = commands;
		init();
	}

	private void init() {
		final Storage localStorage = Storage.getLocalStorageIfSupported();
		if (localStorage.getLength() == 0) {
			loadExamples(localStorage);
		} else {
			displaySavedPrograms(localStorage);
		}
		sidebarDisplay.addItem("test");
		sidebarDisplay.addItem("test2");
	}

	private void loadExamples(final Storage localStorage) {
		Examples examples = new Examples(commands);
		localStorage.setItem(exampleNames.get(0), examples.getBox());
		localStorage.setItem(exampleNames.get(1), examples.getFlower());
		localStorage.setItem(exampleNames.get(2), examples.getOtherFlower());
	}

	private void displaySavedPrograms(final Storage localStorage) {
		for (int index = 0; index < localStorage.getLength(); index++) {
			final String key = localStorage.key(index);
			if (!exampleNames.contains(key)) {
				sidebarDisplay.addItem(key);
			}
		}
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(sidebarDisplay);
	}

}