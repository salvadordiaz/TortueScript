package fr.salvadordiaz.gwt.tortuescript.client.sidebar;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import fr.salvadordiaz.gwt.tortuescript.client.i18n.Messages;
import fr.salvadordiaz.gwt.tortuescript.client.sidebar.SidebarActivity.SidebarDisplay;

public class Sidebar extends Composite implements SidebarDisplay {

	private static SidebarUiBinder uiBinder = GWT.create(SidebarUiBinder.class);

	interface SidebarUiBinder extends UiBinder<Widget, Sidebar> {
	}

	@UiField
	Element examplesHeader;
	@UiField
	Element savedItemsHeader;
	@UiField
	Element savedItems;
	@UiField
	Element defaultItem;
	@UiField
	Anchor clearProgramsLabel;
	@UiField
	Anchor loadProgramsLabel;

	@Inject
	public Sidebar(Messages messages) {
		initWidget(uiBinder.createAndBindUi(this));
		examplesHeader.setInnerText(messages.examples());
		savedItemsHeader.setInnerText(messages.savedItems());
		defaultItem.setInnerText(messages.noSavedItems());
		clearProgramsLabel.setText(messages.clearSavedItems());
		loadProgramsLabel.setText(messages.loadPrograms());
	}

	@Override
	public void clearUserItems() {
		final NodeList<Node> childNodes = savedItems.getChildNodes();
		for (int childIndex = 0; childIndex < childNodes.getLength(); childIndex++) {
			savedItems.removeChild(savedItems.getChild(childIndex));
		}
		savedItems.appendChild(savedItemsHeader);
	}

	@Override
	public void addItem(String key) {
		if (defaultItem.hasParentElement()) {
			savedItems.removeChild(defaultItem);
		}
		AnchorElement anchorElement = Document.get().createAnchorElement();
		anchorElement.setHref("#editor:" + URL.encode(key));
		anchorElement.setInnerText(key);
		LIElement newItem = Document.get().createLIElement();
		newItem.appendChild(anchorElement);
		savedItems.appendChild(newItem);
	}

	@Override
	public HasClickHandlers getClearButton() {
		return clearProgramsLabel;
	}

	@Override
	public HasClickHandlers getLoadProgramsButton() {
		return loadProgramsLabel;
	}
}
