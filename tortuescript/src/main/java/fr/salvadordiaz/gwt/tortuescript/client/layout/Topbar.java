package fr.salvadordiaz.gwt.tortuescript.client.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Topbar extends Composite {

	private static TopbarUiBinder uiBinder = GWT.create(TopbarUiBinder.class);

	interface TopbarUiBinder extends UiBinder<Widget, Topbar> {
	}

	@UiField
	UListElement navigationList;
	
	public Topbar() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
}
