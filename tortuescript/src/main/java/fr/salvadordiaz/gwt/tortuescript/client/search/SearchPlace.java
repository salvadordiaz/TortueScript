package fr.salvadordiaz.gwt.tortuescript.client.search;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class SearchPlace extends Place {

	private final String value;

	public SearchPlace(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Prefix("search")
	public static class Tokenizer implements PlaceTokenizer<SearchPlace> {

		@Override
		public SearchPlace getPlace(String token) {
			return new SearchPlace(token);
		}

		@Override
		public String getToken(SearchPlace place) {
			return place.getValue();
		}

	}
}
