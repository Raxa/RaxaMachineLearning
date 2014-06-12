package com.machine.learning.socket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.learningmodule.association.conceptdrug.learning.ConceptDrugLearning;
import com.learningmodule.association.conceptdrug.model.DrugModel;
import com.learningmodule.association.conceptdrug.predictionmodule.PredictionMethod;

@ServerEndpoint(value = "/ml/search")
public class SearchSocket {

	private Session session;

	@OnOpen
	public void start(Session session) {
		this.session = session;
	}

	@OnClose
	public void end() {

	}

	@OnMessage
	public void onMsg(String query) {
		final String[] strings = query.split(":");
		System.out.println(query);
		if (strings[0].equals("query") && strings.length > 1) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					sendResults(strings[1]);
				}
			}).run();

		} else if (strings[0].equals("learn")) {
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

	private String getJson(DrugModel[] list) {
		String result = "";
		for (int i = 0; i < list.length; i++) {
			result = result + "<div>" + list[i].toString() + "</div>";
		}
		System.out.println(result);
		return result;
	}
}
