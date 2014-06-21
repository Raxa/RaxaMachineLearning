package com.machine.learning.socket;

import java.io.IOException;
import java.util.LinkedList;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.learningmodule.association.conceptdrug.learning.ConceptDrugLearning;
import com.learningmodule.association.conceptdrug.model.PredictionResults;
import com.learningmodule.association.conceptdrug.predictionmodule.PredictionMethod;

/*
 * Class implementing the WebSocket Interface
 */

@ServerEndpoint(value = "/ml/search")
public class SearchSocket {

	private Gson gson = new Gson();
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
	 * for querying should be of form "query:<search string>" input from client
	 * to command for server to learn should be of form "learn:"
	 */
	@OnMessage
	public void onMsg(String query) {
		// split the msg around ':' character
		final String[] strings = query.split(":");
		// System.out.println(query);

		// if the string has query before ':'
		if (strings[0].equals("query") && strings.length > 1) {
			
			// send the results to client for search string
			sendResults(strings[1]);

		} else if (strings[0].equals("learn")) {
			// if client commands to learn, start a new thread to run the learning algo.
			new Thread(new Runnable() {

				@Override
				public void run() {
					ConceptDrugLearning.learn();

				}
			}).run();
		}
	}

	public void sendResults(String query) {

		try {
			// get the predicitions, convert the results into Json String and send to the client 
			session.getBasicRemote().sendText(getJson(PredictionMethod.predict(query)));
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
	private String getJson(LinkedList<PredictionResults> list) {
		String result = "{\"results\":";
		result = result + gson.toJson(list);

		result = result + "}";
		System.out.println(result);
		return result;
	}
}
