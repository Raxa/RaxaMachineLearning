package com.umls.search;

import java.util.LinkedList;

import com.umls.database.MRCONSETableOperation;
import com.umls.database.MRSTYTableOperation;
import com.umls.database.MRXWTableOperation;
import com.umls.models.ConceptModel;
import com.umls.models.SemanticType;

public class PredictionMethod {
	public static LinkedList<ConceptModel> predict(String query) {
		LinkedList<String> cuis = MRXWTableOperation.searchCUI(query);
		LinkedList<ConceptModel> concepts = MRCONSETableOperation.getConceptByCUI(cuis);
		System.out.println(concepts.size());
		LinkedList<SemanticType> types = MRSTYTableOperation.getSemanticTypeByCUI(cuis);
		concepts = addSemanticTypes(concepts, types);
		
		return concepts;
	}

	public static LinkedList<ConceptModel> addSemanticTypes(LinkedList<ConceptModel> concepts,
			LinkedList<SemanticType> types) {
		for(SemanticType type: types) {
			for(ConceptModel concept: concepts) {
				if(concept.getCUI().equals(type.getCUI())) {
					concept.setSemanticTypes(type);
				}
			}
		}
		return concepts;
	}

	public static void main(String[] args) {
		LinkedList<ConceptModel> concepts = predict("fever");
		for(ConceptModel concept: concepts) {
			System.out.println(concept.getName() + ": " + concept.getSemanticTypes().getSTY());
		}
	}
}
