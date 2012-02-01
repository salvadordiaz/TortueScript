package fr.salvadordiaz.gwt.tortuescript.client.sidebar;

import javax.inject.Inject;

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
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageEvent;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;

import fr.salvadordiaz.gwt.tortuescript.client.i18n.LocalizedCommands;
import fr.salvadordiaz.gwt.tortuescript.client.i18n.Messages;
import fr.salvadordiaz.gwt.tortuescript.client.model.JsonGist;
import fr.salvadordiaz.gwt.tortuescript.client.model.ProgramStorage;
import fr.salvadordiaz.gwt.tortuescript.client.sidebar.SidebarActivity.SidebarDisplay.GistActionsDisplay;

public class SidebarActivity extends AbstractActivity {

	public interface SidebarDisplay extends IsWidget {
		void clearUserItems();

		void addItem(JsonGist gist);

		HasClickHandlers getLoadProgramsButton();

		GistActionsDisplay getActionsDisplay();

		public interface GistActionsDisplay extends TakesValue<JsonGist> {
			HasClickHandlers getDeleteButton();

			HasClickHandlers getShareButton();

			HasValue<String> getEmailBox();
		}
	}

	private final RequestBuilder shareGistBuilder = new RequestBuilder(RequestBuilder.POST, "https://api.github.com/gists");
	private final RequestBuilder getGistsBuilder = new RequestBuilder(RequestBuilder.GET, "https://api.github.com/users/tortuescript/gists");

	private final SidebarDisplay sidebarDisplay;
	private final GistActionsDisplay gistActionsDisplay;
	private final LocalizedCommands commands;
	private final Messages messages;
	private final ProgramStorage programStorage;

	@Inject
	public SidebarActivity(SidebarDisplay sidebarDisplay, LocalizedCommands commands, Messages messages, ProgramStorage programStorage) {
		this.sidebarDisplay = sidebarDisplay;
		this.gistActionsDisplay = sidebarDisplay.getActionsDisplay();
		this.commands = commands;
		this.messages = messages;
		this.programStorage = programStorage;
		shareGistBuilder.setHeader("Authorization", "Basic " + encode(""));
		shareGistBuilder.setHeader("Content-Type", "text/plain");
	}

	private void bind() {
		Storage.addStorageEventHandler(new StorageEvent.Handler() {
			@Override
			public void onStorageChange(StorageEvent event) {
				sidebarDisplay.clearUserItems();
				displaySavedPrograms();
			}
		});
		gistActionsDisplay.getShareButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				programStorage.setEmail(gistActionsDisplay.getEmailBox().getValue());
				shareAndSave(gistActionsDisplay.getValue());
			}
		});
		gistActionsDisplay.getDeleteButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				programStorage.delete(gistActionsDisplay.getValue());
			}
		});
	}

	private void loadStaticExamples() {
		Examples examples = new Examples(commands);
		programStorage.saveProgram(JsonGist.createExample(messages.boxExample(), examples.getBox()));
		programStorage.saveProgram(JsonGist.createExample(messages.flowerExample(), examples.getFlower()));
		programStorage.saveProgram(JsonGist.createExample(messages.bigFlowerExample(), examples.getOtherFlower()));
	}

	private void displaySavedPrograms() {
		for (JsonGist gist : programStorage.getSavedPrograms().asIterable()) {
			sidebarDisplay.addItem(gist);
		}
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(sidebarDisplay);
		if (programStorage.getSavedPrograms().length() == 0) {
			loadStaticExamples();
		}
		displaySavedPrograms();
		gistActionsDisplay.getEmailBox().setValue(programStorage.getEmail());
		bind();
	}

	private void shareAndSave(JsonGist gist) {
		try {
			shareGistBuilder.sendRequest(new JSONObject(gist).toString(), new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					Window.alert(messages.programWasShared());
					programStorage.parseAndSave(response.getText());
				}

				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert(messages.sharingError());
				}
			});
		} catch (RequestException e) {
			//motherfucking checked exception
			Window.alert(messages.sharingError());
		}
	}

	private native String encode(String text)/*-{
		return $wnd.btoa(text);
	}-*/;
}
