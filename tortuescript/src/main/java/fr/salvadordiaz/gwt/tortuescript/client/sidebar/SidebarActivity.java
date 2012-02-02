package fr.salvadordiaz.gwt.tortuescript.client.sidebar;

import javax.inject.Inject;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageEvent;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;

import fr.salvadordiaz.gwt.tortuescript.client.i18n.LocalizedCommands;
import fr.salvadordiaz.gwt.tortuescript.client.i18n.Messages;
import fr.salvadordiaz.gwt.tortuescript.client.model.JsonGist;
import fr.salvadordiaz.gwt.tortuescript.client.model.ProgramStorage;
import fr.salvadordiaz.gwt.tortuescript.client.sidebar.SidebarActivity.SidebarDisplay.GistActionsDisplay;
import fr.salvadordiaz.gwt.tortuescript.client.sidebar.rpc.SharingRpcAsync;

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

	private final SidebarDisplay sidebarDisplay;
	private final GistActionsDisplay gistActionsDisplay;
	private final LocalizedCommands commands;
	private final Messages messages;
	private final ProgramStorage programStorage;
	private final SharingRpcAsync sharingRpc;

	@Inject
	public SidebarActivity(SidebarDisplay sidebarDisplay, LocalizedCommands commands, Messages messages, ProgramStorage programStorage,
			SharingRpcAsync sharingRpc) {
		this.sidebarDisplay = sidebarDisplay;
		this.gistActionsDisplay = sidebarDisplay.getActionsDisplay();
		this.commands = commands;
		this.messages = messages;
		this.programStorage = programStorage;
		this.sharingRpc = sharingRpc;
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
		sharingRpc.share(new JSONObject(gist).toString(), new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Window.alert(messages.programWasShared());
				programStorage.parseAndSave(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(messages.sharingError());
			}
		});
	}

}
