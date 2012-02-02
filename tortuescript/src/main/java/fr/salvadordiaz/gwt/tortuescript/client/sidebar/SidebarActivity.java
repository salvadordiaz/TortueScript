package fr.salvadordiaz.gwt.tortuescript.client.sidebar;

import static com.google.common.base.Strings.*;

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
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;

import fr.salvadordiaz.gwt.tortuescript.client.i18n.LocalizedCommands;
import fr.salvadordiaz.gwt.tortuescript.client.i18n.Messages;
import fr.salvadordiaz.gwt.tortuescript.client.model.JsonGist;
import fr.salvadordiaz.gwt.tortuescript.client.model.ProgramStorage;

public class SidebarActivity extends AbstractActivity {

	public interface SidebarDisplay extends IsWidget {
		void clearUserItems();

		void addItem(String key);

		HasClickHandlers getClearButton();

		HasClickHandlers getLoadProgramsButton();
	}

	public interface EmailPromptDisplay {
		HasClickHandlers getSaveButton();

		HasValue<String> getTextbox();

		void center();
	}

	private static final String EMAIL = "email";

	private final RequestBuilder shareGistBuilder = new RequestBuilder(RequestBuilder.POST, "https://api.github.com/gists");
	private final RequestBuilder getGistsBuilder = new RequestBuilder(RequestBuilder.GET, "https://api.github.com/users/tortuescript/gists");

	private final SidebarDisplay sidebarDisplay;
	private final EmailPromptDisplay emailPromptDisplay;
	private final LocalizedCommands commands;
	private final Messages messages;
	private final Storage storage;
	private final ProgramStorage programStorage;

	private Command awaitingCommand;

	@Inject
	public SidebarActivity(SidebarDisplay sidebarDisplay, EmailPromptDisplay emailPromptDisplay, LocalizedCommands commands, Messages messages, ProgramStorage programStorage) {
		this.sidebarDisplay = sidebarDisplay;
		this.emailPromptDisplay = emailPromptDisplay;
		this.commands = commands;
		this.messages = messages;
		this.storage = Storage.getLocalStorageIfSupported();
		this.programStorage = programStorage;
		shareGistBuilder.setHeader("Authorization", "Basic " + encode(""));
		shareGistBuilder.setHeader("Content-Type", "text/plain");
		init();
	}

	private void init() {
		if (programStorage.getSavedPrograms().length() == 0) {
			loadStaticExamples();
		}
		displaySavedPrograms();
		Storage.addStorageEventHandler(new StorageEvent.Handler() {
			@Override
			public void onStorageChange(StorageEvent event) {
				sidebarDisplay.clearUserItems();
				displaySavedPrograms();
			}
		});
		sidebarDisplay.getClearButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				storage.clear();
				loadStaticExamples();
			}
		});
		emailPromptDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				storage.setItem(EMAIL, emailPromptDisplay.getTextbox().getValue());
				if (awaitingCommand != null) {
					awaitingCommand.execute();
				}
			}
		});
	}

	private void loadStaticExamples() {
		Examples examples = new Examples(commands);
		programStorage.saveProgram(JsonGist.create(messages.boxExample(), examples.getBox()));
		programStorage.saveProgram(JsonGist.create(messages.flowerExample(), examples.getFlower()));
		programStorage.saveProgram(JsonGist.create(messages.bigFlowerExample(), examples.getOtherFlower()));
	}

	private void displaySavedPrograms() {
		for (JsonGist gist : programStorage.getSavedPrograms().asIterable()) {
			sidebarDisplay.addItem(gist.getFile().getFilename());
		}
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(sidebarDisplay);
	}

	private void save(String programKey) {
		final String username = nullToEmpty(storage.getItem(EMAIL));
		if (username.isEmpty()) {
			awaitingCommand = createAwaitingSaveCommand(programKey);
			emailPromptDisplay.center();
			return;
		}

		try {
			shareGistBuilder.sendRequest(getGist(programKey, username).toString(), new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					Window.alert(messages.programWasShared());
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

	private Command createAwaitingSaveCommand(final String key) {
		return new Command() {
			@Override
			public void execute() {
				save(key);
			}
		};
	}

	private JSONObject getGist(String key, String username) {
		final JSONObject content = new JSONObject();
		content.put("content", new JSONString(storage.getItem(key)));
		final JSONObject file = new JSONObject();
		file.put(key + ".logo", content);
		final JSONObject gist = new JSONObject();
		final String description = getDescription(username);
		gist.put("description", new JSONString(description));
		gist.put("public", JSONBoolean.getInstance(true));
		gist.put("files", file);
		return gist;
	}

	private String getDescription(String user) {
		return new StringBuilder("{ \"locale\" : \"").append(LocaleInfo.getCurrentLocale().getLocaleName()).append("\", \"user\":\"").append(user)
				.append("\"}").toString();
	}

	private native String encode(String text)/*-{
		return $wnd.btoa(text);
	}-*/;
}
