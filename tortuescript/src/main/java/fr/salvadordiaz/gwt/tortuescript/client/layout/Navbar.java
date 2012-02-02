package fr.salvadordiaz.gwt.tortuescript.client.layout;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import fr.salvadordiaz.gwt.tortuescript.client.i18n.Messages;
import fr.salvadordiaz.gwt.tortuescript.client.search.SearchPlace;

public class Navbar extends Composite {

	private static TopbarUiBinder uiBinder = GWT.create(TopbarUiBinder.class);

	interface TopbarUiBinder extends UiBinder<Widget, Navbar> {
	}

	private static final String ACTIVE = "active";

	@UiField
	UListElement navigationList;
	@UiField
	AnchorElement editorLink, searchLink;

	private final Messages messages = GWT.create(Messages.class);

	@Inject
	public Navbar(EventBus eventBus) {
		initWidget(uiBinder.createAndBindUi(this));
		editorLink.setInnerText(messages.workspaceHeader());
		searchLink.setInnerText(messages.searchProgramsHeader());
		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			@Override
			public void onPlaceChange(PlaceChangeEvent event) {
				if (event.getNewPlace() instanceof SearchPlace) {
					editorLink.getParentElement().removeClassName(ACTIVE);
					searchLink.getParentElement().addClassName(ACTIVE);
				} else {
					editorLink.getParentElement().addClassName(ACTIVE);
					searchLink.getParentElement().removeClassName(ACTIVE);
				}
			}
		});
	}

}
