package com.sfeir.gwt.tortugwt.client.sidebar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sfeir.gwt.tortugwt.client.sidebar.SidebarActivity.SidebarDisplay;

public class Sidebar extends Composite implements SidebarDisplay {

	private static SidebarUiBinder uiBinder = GWT.create(SidebarUiBinder.class);

	interface SidebarUiBinder extends UiBinder<Widget, Sidebar> {
	}

	@UiField
	UListElement savedItems;
	@UiField
	LIElement defaultItem;

	public Sidebar() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void addItem(String key) {
		if(defaultItem.hasParentElement()){
			savedItems.removeChild(defaultItem);
		}
		AnchorElement anchorElement = Document.get().createAnchorElement();
		anchorElement.setHref("#editor:" + URL.encode(key));
		anchorElement.setInnerText(key);
		LIElement newItem = Document.get().createLIElement();
		newItem.appendChild(anchorElement);
		savedItems.appendChild(newItem);
	}

}