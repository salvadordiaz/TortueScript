package fr.salvadordiaz.gwt.tortuescript.client.app;

import javax.inject.Singleton;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import com.google.inject.Provides;

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

	}

	@Provides
	@Singleton
	com.google.web.bindery.event.shared.EventBus provideNewEventBus(EventBus eventBus){
		return eventBus;
	}

	@Provides
	@Singleton
	Place provideDefaultPlace() {
		return new WorkspacePlace("");
	}

	@Provides
	@Singleton
	ActivityMapper provideActivityMapper(final WorkspaceActivity workspaceActivity) {
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
	PlaceController providePlaceController(//
			com.google.web.bindery.event.shared.EventBus eventBus,//
			ActivityMapper activityMapper,//
			FluidContainer display,//
			SidebarActivity sidebarActivity) {
		final ActivityManager workspaceActivityManager = new ActivityManager(activityMapper, eventBus);
		workspaceActivityManager.setDisplay(display.getCenterContainer());
		sidebarActivity.start(display.getSidebarContainer(), null);

		final PlaceController placeController = new PlaceController(eventBus);
		return placeController;
	}

	@Provides
	@Singleton
	PlaceHistoryHandler provideHistoryHandler(//
			com.google.web.bindery.event.shared.EventBus eventBus,//
			PlaceHistoryMapper mapper,//
			Place place,//
			ActivityMapper activityMapper,//
			PlaceController placeController) {
		final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(mapper);
		historyHandler.register(placeController, eventBus, place);
		return historyHandler;
	}

}
