package fr.salvadordiaz.gwt.tortuescript.client.app;

import java.util.Map;

import javax.inject.Singleton;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import com.google.gwt.storage.client.Storage;
import com.google.inject.Provides;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

import fr.salvadordiaz.gwt.tortuescript.client.editor.ProgramScheduler;
import fr.salvadordiaz.gwt.tortuescript.client.editor.WorkspaceActivity;
import fr.salvadordiaz.gwt.tortuescript.client.editor.WorkspaceDisplay;
import fr.salvadordiaz.gwt.tortuescript.client.editor.WorkspacePlace;
import fr.salvadordiaz.gwt.tortuescript.client.editor.ui.Workspace;
import fr.salvadordiaz.gwt.tortuescript.client.layout.FluidContainer;
import fr.salvadordiaz.gwt.tortuescript.client.layout.Navbar;
import fr.salvadordiaz.gwt.tortuescript.client.model.ProgramStorage;
import fr.salvadordiaz.gwt.tortuescript.client.search.SearchActivity;
import fr.salvadordiaz.gwt.tortuescript.client.search.SearchActivity.SearchDisplay;
import fr.salvadordiaz.gwt.tortuescript.client.search.SearchPlace;
import fr.salvadordiaz.gwt.tortuescript.client.search.ui.DesktopSearchDisplay;
import fr.salvadordiaz.gwt.tortuescript.client.sidebar.SidebarActivity;
import fr.salvadordiaz.gwt.tortuescript.client.sidebar.SidebarActivity.SidebarDisplay;
import fr.salvadordiaz.gwt.tortuescript.client.sidebar.SidebarActivity.SidebarDisplay.GistActionsDisplay;
import fr.salvadordiaz.gwt.tortuescript.client.sidebar.ui.GistActionsPopup;
import fr.salvadordiaz.gwt.tortuescript.client.sidebar.ui.Sidebar;

public class ApplicationModule extends AbstractGinModule {

	@WithTokenizers({ WorkspacePlace.Tokenizer.class, SearchPlace.Tokenizer.class })
	public static interface WorkspaceHistoryMapper extends PlaceHistoryMapper {
	}

	@Override
	protected void configure() {
		bind(PlaceHistoryMapper.class).to(WorkspaceHistoryMapper.class).in(Singleton.class);
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(FluidContainer.class).in(Singleton.class);
		bind(Navbar.class).in(Singleton.class);

		bind(GistActionsDisplay.class).to(GistActionsPopup.class).in(Singleton.class);
		bind(SidebarDisplay.class).to(Sidebar.class).in(Singleton.class);
		bind(SidebarActivity.class).in(Singleton.class);

		bind(WorkspaceDisplay.class).to(Workspace.class).in(Singleton.class);
		bind(ProgramScheduler.class).in(Singleton.class);
		bind(WorkspaceActivity.class).in(Singleton.class);

		bind(SearchActivity.class).in(Singleton.class);
		bind(SearchDisplay.class).to(DesktopSearchDisplay.class).in(Singleton.class);
		//this is the configuration of the default place
		bind(Place.class).to(WorkspacePlace.class).in(Singleton.class);
	}

	@Provides
	@Singleton
	ProgramStorage provideLocalStorage() {
		return new ProgramStorage(Storage.getLocalStorageIfSupported());
	}

	@Provides
	@Singleton
	Scheduler provideScheduler() {
		return Scheduler.get();
	}

	@Provides
	@Singleton
	ActivityMapper provideActivityMapper(final WorkspaceActivity workspaceActivity, final SearchActivity searchActivity) {
		final Map<Class<? extends Place>, PlaceAwareActivity> mappings = ImmutableMap.<Class<? extends Place>, PlaceAwareActivity> of(//
				WorkspacePlace.class, workspaceActivity,// 
				SearchPlace.class, searchActivity);
		return new ActivityMapper() {
			@Override
			public Activity getActivity(Place place) {
				final PlaceAwareActivity activity = mappings.get(place.getClass());
				activity.setPlace(place);
				return activity;
			}
		};
	}

	@Provides
	@Singleton
	PlaceController providePlaceController(EventBus eventBus) {
		return new PlaceController(eventBus);
	}

	@Provides
	@Singleton
	PlaceHistoryHandler provideHistoryHandler(//
			EventBus eventBus,//
			PlaceHistoryMapper mapper,//
			PlaceController placeController,//
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
		historyHandler.register(placeController, eventBus, place);
		return historyHandler;
	}

}
