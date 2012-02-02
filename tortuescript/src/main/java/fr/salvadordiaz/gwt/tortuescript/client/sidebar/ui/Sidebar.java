package fr.salvadordiaz.gwt.tortuescript.client.sidebar.ui;

import static com.google.common.collect.Maps.*;

import java.util.Map;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.Widget;

import fr.salvadordiaz.gwt.tortuescript.client.i18n.Messages;
import fr.salvadordiaz.gwt.tortuescript.client.model.JsonGist;
import fr.salvadordiaz.gwt.tortuescript.client.sidebar.SidebarActivity.SidebarDisplay;

public class Sidebar extends Composite implements SidebarDisplay {

	private static final String ACTIVE = "active";

	private static SidebarUiBinder uiBinder = GWT.create(SidebarUiBinder.class);

	interface SidebarUiBinder extends UiBinder<Widget, Sidebar> {
	}

	interface Ids extends CssResource {
		String id();
	}

	@UiField
	HTMLPanel container;
	@UiField
	Element savedItemsHeader;
	@UiField
	Anchor loadProgramsLabel;
	@UiField
	Ids ids;

	private final Map<Anchor, JsonGist> gistsForAnchors = newHashMap();
	private final GistActionsPopup popup;

	private Element activeElement;

	@Inject
	public Sidebar(Messages messages, GistActionsPopup popup) {
		initWidget(uiBinder.createAndBindUi(this));
		this.popup = popup;
		savedItemsHeader.setInnerText(messages.savedItems());
		loadProgramsLabel.setText(messages.loadPrograms());
	}

	@Override
	public void clearUserItems() {
		final Element savedItems = container.getElementById(ids.id());
		savedItems.setInnerHTML("");
		savedItems.appendChild(savedItemsHeader);
		gistsForAnchors.clear();
	}

	@Override
	public void addItem(JsonGist item) {
		final HTMLPanel panel = new HTMLPanel("li", "");
		final Anchor anchor = new Anchor(item.getFile().getFilename());
		anchor.addClickHandler(itemClickHandler);
		panel.add(anchor);
		gistsForAnchors.put(anchor, item);
		container.add(panel, ids.id());
	}

	private final ClickHandler itemClickHandler = new ClickHandler() {
		@Override
		public void onClick(final ClickEvent event) {
			final Anchor source = (Anchor) event.getSource();
			if(activeElement != null){
				activeElement.removeClassName(ACTIVE);
			}
			activeElement = source.getElement().getParentElement();
			activeElement.addClassName(ACTIVE);
			popup.setValue(gistsForAnchors.get(source));
			popup.setPopupPositionAndShow(new PositionCallback() {
				@Override
				public void setPosition(int offsetWidth, int offsetHeight) {
					final int left = source.getAbsoluteLeft() + source.getOffsetWidth();
					final int top = source.getAbsoluteTop() + source.getOffsetHeight() / 2 - offsetHeight / 2;
					popup.setPopupPosition(left, top);
				}
			});
		}
	};

	@Override
	public HasClickHandlers getLoadProgramsButton() {
		return loadProgramsLabel;
	}

	@Override
	public GistActionsDisplay getActionsDisplay() {
		return popup;
	}
}
