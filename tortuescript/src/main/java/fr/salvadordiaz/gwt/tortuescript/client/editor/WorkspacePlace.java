package fr.salvadordiaz.gwt.tortuescript.client.editor;

import static com.google.common.base.Objects.*;
import static com.google.common.base.Strings.*;

import com.google.common.base.Objects;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class WorkspacePlace extends Place {

	private final String programName;

	public WorkspacePlace() {
		programName = null;
	}

	public WorkspacePlace(String programName) {
		this.programName = programName;
	}

	public String getProgramName() {
		return programName;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(programName);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WorkspacePlace) {
			return equal(programName, ((WorkspacePlace) obj).programName);
		}
		return false;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("programName", programName).toString();
	}

	@Prefix("editor")
	public static class Tokenizer implements PlaceTokenizer<WorkspacePlace> {

		@Override
		public WorkspacePlace getPlace(String token) {
			return new WorkspacePlace(token);
		}

		@Override
		public String getToken(WorkspacePlace place) {
			return nullToEmpty(place.getProgramName());
		}

	}

}
