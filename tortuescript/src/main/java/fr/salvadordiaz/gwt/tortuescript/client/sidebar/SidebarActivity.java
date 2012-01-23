package fr.salvadordiaz.gwt.tortuescript.client.sidebar;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.ImmutableList;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import fr.salvadordiaz.gwt.tortuescript.client.i18n.LocalizedCommands;

public class SidebarActivity extends AbstractActivity {

	public interface SidebarDisplay extends IsWidget {
		void clearUserItems();

		void addItem(String key);
		
		HasClickHandlers getClearButton();
	}

	private final List<String> exampleNames = ImmutableList.of("Box", "Flower", "OtherFlower");

	private final SidebarDisplay sidebarDisplay;
	private final LocalizedCommands commands;

	@Inject
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
		Storage.addStorageEventHandler(new StorageEvent.Handler() {
			@Override
			public void onStorageChange(StorageEvent event) {
				sidebarDisplay.clearUserItems();
				displaySavedPrograms(localStorage);
			}
		});
		sidebarDisplay.getClearButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				localStorage.clear();
				loadExamples(localStorage);
			}
		});
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

	private void call() throws RequestException {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, "https://api.github.com/gists");
		final String authValue = "Basic " + encode("tortuescript:7or7uescr1p7");
		builder.setHeader("Authorization", authValue);
		builder.setHeader("Content-Type", "text/plain");
		builder.sendRequest(getGist().toString(), new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
			}

			@Override
			public void onError(Request request, Throwable exception) {
			}
		});
	}

	private JSONObject getGist() {
		final JSONObject content = new JSONObject();
		content.put("content", new JSONString("new\nrepeat 4\nforward 100\nleft 90\nend repeat"));
		final JSONObject file = new JSONObject();
		file.put("test.logo", content);
		final JSONObject gist = new JSONObject();
		gist.put("description", new JSONString("test gist"));
		gist.put("public", JSONBoolean.getInstance(true));
		gist.put("files", file);
		return gist;
	}

	private native String encode(String text)/*-{
		return $wnd.btoa(text);
	}-*/;
}
