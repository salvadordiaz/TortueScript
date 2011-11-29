package com.sfeir.gwt.tortugwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.sfeir.gwt.tortugwt.client.editor.Workspace;
import com.sfeir.gwt.tortugwt.client.editor.WorkspaceActivity;
import com.sfeir.gwt.tortugwt.client.editor.WorkspaceDisplay;
import com.sfeir.gwt.tortugwt.client.layout.FluidContainer;
import com.sfeir.gwt.tortugwt.client.layout.Topbar;
import com.sfeir.gwt.tortugwt.client.sidebar.Sidebar;
import com.sfeir.gwt.tortugwt.client.sidebar.SidebarActivity;
import com.sfeir.gwt.tortugwt.client.sidebar.SidebarActivity.SidebarDisplay;

public class Tortugwt implements EntryPoint {

	private final LocalizedCommands localizedCommands = GWT.create(LocalizedCommands.class);
	private final Messages messages = GWT.create(Messages.class);

	private final WorkspaceDisplay workspace = new Workspace();
	private final WorkspaceActivity workspaceActivity = new WorkspaceActivity(workspace, localizedCommands, messages);

	private final SidebarDisplay sidebarDisplay = new Sidebar();
	private final SidebarActivity sidebarActivity = new SidebarActivity(sidebarDisplay, localizedCommands);

	public void onModuleLoad() {
		FluidContainer container = new FluidContainer();
		RootPanel.get().add(new Topbar());
		RootPanel.get().add(container);
		sidebarActivity.start(container.getSidebarContainer(), null);
		workspaceActivity.start(container.getCenterContainer(), null);
	}

}
