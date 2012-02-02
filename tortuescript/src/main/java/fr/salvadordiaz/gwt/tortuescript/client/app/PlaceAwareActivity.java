package fr.salvadordiaz.gwt.tortuescript.client.app;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;

public abstract class PlaceAwareActivity extends AbstractActivity {

	protected Place place;
	
	public void setPlace(Place place) {
		this.place = place;
	}

}
