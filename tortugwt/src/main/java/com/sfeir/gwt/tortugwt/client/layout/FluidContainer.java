package com.sfeir.gwt.tortugwt.client.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class FluidContainer extends Composite {

	private static FluidContainerUiBinder uiBinder = GWT.create(FluidContainerUiBinder.class);

	interface FluidContainerUiBinder extends UiBinder<Widget, FluidContainer> {
	}

	@UiField
	Sidebar sidebar;
	@UiField
	HTMLPanel container;

	public FluidContainer() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setCenterWidget(IsWidget widget) {
		container.add(widget);
	}
}
