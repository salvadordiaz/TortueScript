package fr.salvadordiaz.gwt.tortuescript.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public class JsonGist extends JavaScriptObject {

	public static class GistDescription extends JavaScriptObject {
		protected GistDescription() {
		}

		public final native String getLocale()/*-{
			return this.locale;
		}-*/;

		public final native void setLocale(String locale)/*-{
			this.locale = locale;
		}-*/;

		public final native String getUser()/*-{
			return this.user;
		}-*/;

		private final native void setUser(String user)/*-{
			this.user = user;
		}-*/;

	}

	public static class GistFile extends JavaScriptObject {
		protected GistFile() {
		}

		public final native String getFilename()/*-{
			return this.filename;
		}-*/;

		public final native String getContent()/*-{
			return this.content;
		}-*/;

		public final native void setContent(String content)/*-{
			this.content = content;
		}-*/;

	}

	protected JsonGist() {
	}

	public static JsonGist create(String filename, String content, String locale, String username) {
		final JsonGist result = create(filename, content);
		final GistDescription description = result.getDescription();
		description.setUser(username);
		description.setLocale(locale);
		result.setDescription(description);
		return result;
	}

	private static native JsonGist create(String filename, String content)/*-{
		return {
			"description" : "{}",
			"public" : true,
			"files" : {
				filename : {
					"filename" : filename,
					"content" : content
				}
			}
		};
	}-*/;

	public static native JsonGist createExample(String filename, String content)/*-{
		return {
			"description" : "{\"user\":\"tortuescript@sfeir.com\"}",
			"files" : {
				filename : {
					"filename" : filename,
					"content" : content
				}
			},
			"html_url" : "javascript:;"
		};
	}-*/;

	public final void setUser(String user) {
		final GistDescription description = getDescription();
		description.setUser(user);
		setDescription(description);
	}

	public final GistDescription getDescription() {
		return JSONParser.parseStrict(nativeGetDescription()).isObject().getJavaScriptObject().cast();
	}

	public final void setDescription(GistDescription description) {
		nativeSetDescription(new JSONObject(description).toString());
	}

	private native void nativeSetDescription(String description)/*-{
		this.description = description;
	}-*/;

	private native String nativeGetDescription()/*-{
		return this.description;
	}-*/;

	public native final String getUrl()/*-{
		return this.url;
	}-*/;

	public native final GistFile getFile()/*-{
		for ( var key in this.files) {
			if (this.files.hasOwnProperty(key)) {
				return this.files[key];
			}
		}
		return null;
	}-*/;

	public native final String getHtmlUrl()/*-{
		return this.html_url;
	}-*/;
}
