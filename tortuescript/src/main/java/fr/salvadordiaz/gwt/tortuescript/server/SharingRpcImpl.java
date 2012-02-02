package fr.salvadordiaz.gwt.tortuescript.server;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

import fr.salvadordiaz.gwt.tortuescript.client.sidebar.rpc.SharingRpc;

public class SharingRpcImpl extends RemoteServiceServlet implements SharingRpc {
	private static final long serialVersionUID = -3340953095411182268L;

	private final Logger log = Logger.getLogger(SharingRpcImpl.class.getSimpleName());

	private final URLFetchService service = URLFetchServiceFactory.getURLFetchService();

	private final HTTPHeader contentHeader = new HTTPHeader("Content-Type", "text/plain");
	private HTTPHeader authorizationHeader;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		authorizationHeader = new HTTPHeader("Authorization", "Basic " + config.getServletContext().getInitParameter("tortuescript.credentials"));
	}

	@Override
	public String share(String jsonString) throws IOException {
		final HTTPRequest request = new HTTPRequest(new URL("https://api.github.com/gists"), HTTPMethod.POST);
		request.setHeader(authorizationHeader);
		request.setHeader(contentHeader);
		request.setPayload(jsonString.getBytes());
		log.info("POSTing gist to GitHub...");
		final HTTPResponse httpResponse = service.fetch(request);
		String response = new String(httpResponse.getContent());
		log.info("GitHub answered : " + response);
		if (reponseIsSuccess(response)) {
			return response;
		}
		throw new InvocationException("Unexpected GitHub response");
	}

	private boolean reponseIsSuccess(String jsonString) {
		try {
			final JsonObject jsonResponse = JsonParser.object().from(jsonString);
			return jsonResponse.containsKey("url");
		} catch (JsonParserException e) {
			throw new RuntimeException(e);
		}

	}
}
