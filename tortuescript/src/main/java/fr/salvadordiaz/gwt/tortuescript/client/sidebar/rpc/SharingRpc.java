package fr.salvadordiaz.gwt.tortuescript.client.sidebar.rpc;

import java.io.IOException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpc/sharing")
public interface SharingRpc extends RemoteService {

	String share(String jsonString) throws IOException;

}
