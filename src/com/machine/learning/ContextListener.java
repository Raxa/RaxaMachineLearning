package com.machine.learning;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.learningmodule.association.conceptdrug.ConceptDrugAssociationModule;
import com.raxa.association.ConceptDrugDatabaseInput;

public class ContextListener implements ServletContextListener{
	private static Logger log = Logger.getLogger(ContextListener.class);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		LearningModulesPool.addLearningModule(new ConceptDrugAssociationModule(
				new ConceptDrugDatabaseInput()));
		log.debug("modules added");
	}

}
