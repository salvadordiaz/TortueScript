package fr.salvadordiaz.gwt.tortuescript.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONParser;

public class JsonGist extends JavaScriptObject {

	public static class GistDescription extends JavaScriptObject {
		protected GistDescription() {
		}

		public final native String getLocale()/*-{
			return this.locale;
		}-*/;

		public final native String getUser()/*-{
			return this.user;
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

	public static native JsonGist create(String filename, String content)/*-{
		return {
			"files" : {
				filename : {
					"filename" : filename,
					"content" : content
				}
			}
		};
	}-*/;

	public static final JsonGist create(String jsonString){
		return JSONParser.parseStrict(jsonString).isObject().getJavaScriptObject().cast();
	}
	
	public final GistDescription getDescription() {
		return JSONParser.parseStrict(nativeDescription()).isObject().getJavaScriptObject().cast();
	}

	private native String nativeDescription()/*-{
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
