package com.sfeir.gwt.tortugwt.client.sidebar;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sfeir.gwt.tortugwt.client.Messages;
import com.sfeir.gwt.tortugwt.client.sidebar.SidebarActivity.SidebarDisplay;

public class Sidebar extends Composite implements SidebarDisplay {

	private static SidebarUiBinder uiBinder = GWT.create(SidebarUiBinder.class);

	interface SidebarUiBinder extends UiBinder<Widget, Sidebar> {
	}

	@UiField
	Element examplesHeader;
	@UiField
	Element savedItemsHeader;
	@UiField
	UListElement savedItems;
	@UiField
	LIElement defaultItem;
	@UiField
	Anchor syntaxLabel;
	@UiField
	TortueScriptReference syntaxPopup;
	@UiField
	Anchor clearProgramsLabel;

	private final Messages messages;

	@Inject
	public Sidebar(Messages messages) {
		this.messages = messages;
		initWidget(uiBinder.createAndBindUi(this));
		syntaxLabel.setText(messages.showSyntax());
		examplesHeader.setInnerText(messages.examples());
		savedItemsHeader.setInnerText(messages.savedItems());
		defaultItem.setInnerText(messages.noSavedItems());
		clearProgramsLabel.setText(messages.clearSavedItems());
	}

	@Override
	public void clearUserItems() {
		final NodeList<Node> childNodes = savedItems.getChildNodes();
		for (int childIndex = 0; childIndex < childNodes.getLength(); childIndex++) {
			savedItems.removeChild(savedItems.getChild(childIndex));
		}
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

	@UiFactory
	TortueScriptReference makeReference() {
		return new TortueScriptReference(messages);
	}

	@UiHandler("syntaxLabel")
	void showSyntaxPopup(ClickEvent event) {
		if (!syntaxPopup.isVisible()) {
			syntaxPopup.setVisible(true);
			syntaxLabel.setText(messages.hideSyntax());
		} else {
			syntaxPopup.setVisible(false);
			syntaxLabel.setText(messages.showSyntax());
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		syntaxPopup.setPosition(syntaxLabel);
	}
}
