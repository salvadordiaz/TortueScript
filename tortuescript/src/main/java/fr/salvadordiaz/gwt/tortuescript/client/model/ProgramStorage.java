package fr.salvadordiaz.gwt.tortuescript.client.model;

import static com.google.common.base.Objects.*;
import static com.google.common.base.Predicates.*;
import static com.google.common.base.Strings.*;
import static com.google.common.collect.Iterables.*;

import javax.inject.Inject;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.storage.client.Storage;

public class ProgramStorage {

	private static final String PROGRAMS = "programs";
	private final Storage storage;

	@Inject
	public ProgramStorage(Storage storage) {
		this.storage = storage;
	}

	public JsIterable<JsonGist> getSavedPrograms() {
		return getPrograms().getJavaScriptObject().cast();
	}

	public void saveProgram(JsonGist gist) {
		final JSONArray savedPrograms = getPrograms();
		final int indexOf = indexOf(gist.getFile().getFilename(), savedPrograms);
		savedPrograms.set(indexOf != -1 ? indexOf : savedPrograms.size(), new JSONObject(gist));
		storage.setItem(PROGRAMS, savedPrograms.toString());
	}

	public void saveProgram(String name, String content) {
		saveProgram(JsonGist.create(name, content));
	}

	public JsonGist getProgram(String name) {
		JsIterable<JsonGist> it = getPrograms().getJavaScriptObject().cast();
		return find(it.asIterable(), compose(equalTo(name), asFilename), null);
	}

	private JSONArray getPrograms() {
		final String programs = nullToEmpty(storage.getItem(PROGRAMS)).trim();
		if (programs.isEmpty()) {
			return new JSONArray();
		}
		return firstNonNull(JSONParser.parseStrict(programs).isArray(), new JSONArray());
	}

	private int indexOf(String name, JSONArray programs) {
		JsIterable<JsonGist> it = programs.getJavaScriptObject().cast();
		return Iterables.indexOf(transform(it.asIterable(), asFilename), equalTo(name));
	}

	private final Function<JsonGist, String> asFilename = new Function<JsonGist, String>() {
		@Override
		public String apply(JsonGist input) {
			return input.getFile().getFilename();
		}
	};
}
