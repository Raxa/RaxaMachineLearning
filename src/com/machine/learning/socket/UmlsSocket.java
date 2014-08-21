package com.machine.learning.socket;

import java.io.IOException;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.learningmodule.association.conceptdrug.PredictionResult;
import com.machine.learning.LearningModulesPool;
import com.machine.learning.request.Request;
import com.machine.learning.request.SearchQueryRequest;
import com.umls.search.DiseaseToDrugPrediction;

/*
 * class that implement the socket interface for umls Learning Module
 */

@ServerEndpoint(value = "/ml/umls")
public class UmlsSocket {

	private Gson gson = new Gson();
	private static Logger log = Logger.getLogger(UmlsSocket.class);
	private Session session;

	@OnOpen
	public void start(Session session) {
		this.session = session;
	}

	@OnClose
	public void end() {

	}

	/*
	 * Method that is invoked when there is msg from client input from client
	 * for querying should be of form "query:<search string>"
	 */
	@OnMessage
	public void onMsg(String query) {
		System.out.println(query);
		Request req = gson.fromJson(query, Request.class);

		if (req.getSearchRequest() != null) {

			// send the results to client for search string
			sendResults(req.getSearchRequest());

		}
	}

	public void sendResults(SearchQueryRequest query) {

		try {
			// get the predicitions, convert the results into Json String and
			// send to the client
			session.getBasicRemote()
					.sendText(
							getJson(DiseaseToDrugPrediction.predict(query.getQuery(),
									query.getFeatures())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@OnError
	public void onError(Throwable t) throws Throwable {
		t.printStackTrace();
	}

	// method to convert the results into Json string;
	private String getJson(Object list) {
		String result = gson.toJson(list);

		System.out.println(result);
		log.debug(result);
		return result;
	}
}
