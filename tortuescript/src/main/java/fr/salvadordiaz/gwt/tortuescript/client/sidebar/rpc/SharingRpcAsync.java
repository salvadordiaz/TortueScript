package fr.salvadordiaz.gwt.tortuescript.client.sidebar.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SharingRpcAsync {

	void share(String jsonString, AsyncCallback<String> callback);

}
