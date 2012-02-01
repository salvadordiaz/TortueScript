package fr.salvadordiaz.gwt.tortuescript.client.search;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

import fr.salvadordiaz.gwt.tortuescript.client.app.PlaceAwareActivity;
import fr.salvadordiaz.gwt.tortuescript.client.editor.WorkspacePlace;
import fr.salvadordiaz.gwt.tortuescript.client.i18n.Messages;
import fr.salvadordiaz.gwt.tortuescript.client.model.JsIterable;
import fr.salvadordiaz.gwt.tortuescript.client.model.JsonGist;
import fr.salvadordiaz.gwt.tortuescript.client.model.ProgramStorage;

public class SearchActivity extends PlaceAwareActivity {

	public interface SearchDisplay extends IsWidget, HasSelectionHandlers<JsonGist> {
		void showError(String errorTitle, String errorText);

		void setSearchResult(Iterable<JsonGist> searchResult);
	}

	private final PlaceController placeController;
	private final SearchDisplay searchDisplay;
	private final Messages messages;
	private final ProgramStorage storage;
	private final RequestBuilder getGistsBuilder = new RequestBuilder(RequestBuilder.GET, "https://api.github.com/users/tortuescript/gists");

	@Inject
	public SearchActivity(SearchDisplay searchDisplay, Messages messages, ProgramStorage storage, PlaceController placeController) {
		this.searchDisplay = searchDisplay;
		this.messages = messages;
		this.storage = storage;
		this.placeController = placeController;
		bind();
	}

	private void bind() {
		searchDisplay.addSelectionHandler(new SelectionHandler<JsonGist>() {
			@Override
			public void onSelection(SelectionEvent<JsonGist> event) {
				loadAndSave(event.getSelectedItem());
			}
		});
	}

	protected void loadAndSave(final JsonGist selectedItem) {
		try {
			new RequestBuilder(RequestBuilder.GET, selectedItem.getUrl()).sendRequest("", new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					placeController.goTo(new WorkspacePlace(storage.parseAndSave(response.getText()).getFile().getFilename()));
				}

				@Override
				public void onError(Request request, Throwable e) {
					searchDisplay.showError(messages.errorLoadingProgram(selectedItem.getFile().getFilename()), e.getLocalizedMessage());
				}
			});
		} catch (RequestException e) {
			searchDisplay.showError(messages.errorLoadingProgram(selectedItem.getFile().getFilename()), e.getLocalizedMessage());
		}
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(searchDisplay);
		try {
			getGistsBuilder.sendRequest("", new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					JsIterable<JsonGist> create = JsIterable.create(response.getText());
					searchDisplay.setSearchResult(create.asIterable());
				}

				@Override
				public void onError(Request request, Throwable e) {
					searchDisplay.showError(messages.errorSearchingPrograms(), e.getLocalizedMessage());//TODO: show user-friendly error message
				}
			});
		} catch (RequestException e) {
			searchDisplay.showError(messages.errorSearchingPrograms(), e.getLocalizedMessage());//TODO: show user-friendly error message
		}
	}

}
