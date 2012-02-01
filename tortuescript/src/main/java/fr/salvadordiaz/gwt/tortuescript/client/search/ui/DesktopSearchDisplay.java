package fr.salvadordiaz.gwt.tortuescript.client.search.ui;

import static com.google.common.base.Objects.*;
import static com.google.common.collect.Lists.*;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import fr.salvadordiaz.gwt.tortuescript.client.i18n.Messages;
import fr.salvadordiaz.gwt.tortuescript.client.model.JsonGist;
import fr.salvadordiaz.gwt.tortuescript.client.search.SearchActivity.SearchDisplay;

public class DesktopSearchDisplay extends Composite implements SearchDisplay {

	private static DesktopSearchDisplayUiBinder uiBinder = GWT.create(DesktopSearchDisplayUiBinder.class);

	interface DesktopSearchDisplayUiBinder extends UiBinder<Widget, DesktopSearchDisplay> {
	}

	interface CellTemplates extends SafeHtmlTemplates {
		@Template("<a href=\"{1}\" target=\"_blank\">{0}</a>")
		SafeHtml anchor(String name, SafeUri url);

		@Template("<button type=\"button\" tabindex=\"-1\" class=\"btn\" disabled=\"true\">{0}</button>")
		SafeHtml disabledButton(SafeHtml text);

		@Template("<button type=\"button\" tabindex=\"-1\" class=\"btn\">{0}</button>")
		SafeHtml button(SafeHtml text);
	}

	@UiField
	Element header, subheader;
	@UiField
	HasClickHandlers closeError;
	@UiField
	Element errorTitle, errorText;
	@UiField
	CellTable<JsonGist> table;

	private final Messages messages = GWT.create(Messages.class);
	private final CellTemplates templates = GWT.create(CellTemplates.class);
	private final Map<String, String> localeNames;
	private final String currentLocaleName;

	public DesktopSearchDisplay() {
		initWidget(uiBinder.createAndBindUi(this));
		header.setInnerText(messages.searchProgramsHeader());
		subheader.setInnerText(messages.searchProgramsSubheader());
		localeNames = ImmutableMap.of("fr", messages.french(), "en", messages.english());
		currentLocaleName = LocaleInfo.getCurrentLocale().getLocaleName();
		createTable();
	}

	private void createTable() {
		TextColumn<JsonGist> userColumn = new TextColumn<JsonGist>() {
			@Override
			public String getValue(JsonGist object) {
				return object.getDescription().getUser();
			}
		};
		Column<JsonGist, SafeHtml> nameColumn = new Column<JsonGist, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(JsonGist object) {
				return templates.anchor(object.getFile().getFilename(), UriUtils.fromString(object.getHtmlUrl()));
			}
		};
		TextColumn<JsonGist> localeColumn = new TextColumn<JsonGist>() {
			@Override
			public String getValue(JsonGist object) {
				return localeNames.get(object.getDescription().getLocale());
			}
		};
		Column<JsonGist, String> buttonColumn = new Column<JsonGist, String>(new BootstrapBtnCell()) {
			@Override
			public String getValue(JsonGist object) {
				return messages.open();
			}
		};
		buttonColumn.setFieldUpdater(new FieldUpdater<JsonGist, String>() {
			@Override
			public void update(int index, JsonGist object, String value) {
				SelectionEvent.fire(DesktopSearchDisplay.this, object);
			}
		});
		table.addColumn(userColumn, messages.programTableUser());
		table.addColumn(nameColumn, messages.programTableName());
		table.addColumn(localeColumn, messages.programTableLocale());
		table.addColumn(buttonColumn);
		table.setWidth("100%", true);
	}

	@Override
	public void showError(String errorTitle, String errorText) {
		this.errorTitle.setInnerText(errorTitle);
		this.errorText.setInnerText(errorText);
	}

	@Override
	public void setSearchResult(Iterable<JsonGist> searchResult) {
		List<JsonGist> list = newArrayList(searchResult);
		table.setRowCount(list.size(), true);
		table.setRowData(list);
	}

	class BootstrapBtnCell extends ButtonCell {
		@Override
		public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
			final boolean enabled = table.getVisibleItem(context.getIndex()).getDescription().getLocale().equalsIgnoreCase(currentLocaleName);
			final SafeHtml safeData = firstNonNull(data, SafeHtmlUtils.EMPTY_SAFE_HTML);
			if (enabled) {
				sb.append(templates.button(safeData));
			} else {
				sb.append(templates.disabledButton(safeData));
			}
		}
	}

	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<JsonGist> handler) {
		return addHandler(handler, SelectionEvent.getType());
	}
}
