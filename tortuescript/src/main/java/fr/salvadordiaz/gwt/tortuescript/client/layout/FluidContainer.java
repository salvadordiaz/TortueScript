package fr.salvadordiaz.gwt.tortuescript.client.layout;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class FluidContainer extends Composite {

	private static FluidContainerUiBinder uiBinder = GWT.create(FluidContainerUiBinder.class);

	interface FluidContainerUiBinder extends UiBinder<Widget, FluidContainer> {
	}

	@UiField(provided = true)
	Navbar navbar;
	@UiField
	SimplePanel sidebarContainer;
	@UiField
	SimplePanel centerContainer;

	@Inject
	public FluidContainer(Navbar navbar) {
		this.navbar = navbar;
		initWidget(uiBinder.createAndBindUi(this));
	}

	public AcceptsOneWidget getSidebarContainer() {
		return sidebarContainer;
	}

	public AcceptsOneWidget getCenterContainer() {
		return centerContainer;
	}

}
