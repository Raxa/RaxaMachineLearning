package com.machine.learning.socket;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

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
		String[] strings = query.split(":");
		System.out.println(query);
		if(strings[0].equals("query") && strings.length > 1) {
		
		sendResults(strings[1]);
		} else if(strings[0].equals("learn")) {
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

	private String getJson(LinkedList<DrugModel> list) {
		Iterator<DrugModel> itr = list.iterator();
		String result = "";
		while (itr.hasNext()) {
			result = result + "<div>" + itr.next().toString() + "</div>";
		}
		System.out.println(result);
		return result;
	}
}
