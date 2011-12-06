package fr.salvadordiaz.gwt.tortuescript.client.app;

import javax.inject.Singleton;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import com.google.inject.Provides;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

import fr.salvadordiaz.gwt.tortuescript.client.editor.Workspace;
import fr.salvadordiaz.gwt.tortuescript.client.editor.WorkspaceActivity;
import fr.salvadordiaz.gwt.tortuescript.client.editor.WorkspaceDisplay;
import fr.salvadordiaz.gwt.tortuescript.client.editor.WorkspacePlace;
import fr.salvadordiaz.gwt.tortuescript.client.layout.FluidContainer;
import fr.salvadordiaz.gwt.tortuescript.client.sidebar.Sidebar;
import fr.salvadordiaz.gwt.tortuescript.client.sidebar.SidebarActivity;
import fr.salvadordiaz.gwt.tortuescript.client.sidebar.SidebarActivity.SidebarDisplay;

public class ApplicationModule extends AbstractGinModule {

	@WithTokenizers({ WorkspacePlace.Tokenizer.class })
	public static interface WorkspaceHistoryMapper extends PlaceHistoryMapper {
	}

	@Override
	protected void configure() {
		bind(PlaceHistoryMapper.class).to(WorkspaceHistoryMapper.class).in(Singleton.class);
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(FluidContainer.class).in(Singleton.class);

		bind(SidebarDisplay.class).to(Sidebar.class).in(Singleton.class);
		bind(SidebarActivity.class).in(Singleton.class);

		bind(WorkspaceDisplay.class).to(Workspace.class).in(Singleton.class);
		bind(WorkspaceActivity.class).in(Singleton.class);
		//this is the configuration of the default place
		bind(Place.class).to(WorkspacePlace.class).in(Singleton.class);
	}

	@Provides
	@Singleton
	ActivityMapper provideActivityMapper(final WorkspaceActivity workspaceActivity) {
		//there is only one activity, the ActivityMapper will always return the workspaceActivity, setting the new place before doing so
		return new ActivityMapper() {
			@Override
			public Activity getActivity(Place place) {
				workspaceActivity.setPlace(place);
				return workspaceActivity;
			}
		};
	}

	@Provides
	@Singleton
	PlaceHistoryHandler provideHistoryHandler(//
			EventBus eventBus,//
			PlaceHistoryMapper mapper,//
			Place place,// default place
			ActivityMapper activityMapper,//
			FluidContainer display,//
			SidebarActivity sidebarActivity) {
		// this is a good place to create the activity manager(s): just before the placeHistoryHandler is created 
		// ( because that's the moment when the application is started, through a call to placeHistoryHandler.handleCurrentHistory() )
		final ActivityManager workspaceActivityManager = new ActivityManager(activityMapper, eventBus);
		workspaceActivityManager.setDisplay(display.getCenterContainer());
		// sidebar activity is the only activity in its display region so it doesn't need to be managed by an ActivityManager,
		// it can be started directly from the beginning of the application lifecycle
		sidebarActivity.start(display.getSidebarContainer(), null);
		// 
		final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(mapper);
		historyHandler.register(new PlaceController(eventBus), eventBus, place);
		return historyHandler;
	}

}
