package com.machine.learning;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.learningmodule.association.conceptdrug.ConceptDrugAssociationModule;
import com.learningmodule.association.conceptdrug.multifeature.ConceptDrugLearningMultiFeatureModule;
import com.pacemaker.association.PaceMakeConceptDrugDatabaseInput;
/*
 * class that is used add modules in Learning Modules Pool on the starting of server
 */

public class ContextListener implements ServletContextListener {
	private static Logger log = Logger.getLogger(ContextListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * method that will be called when the context is initialized.
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		// add the ConceptDrugAssociationModule with OpenMRS database Interface
		LearningModulesPool.addLearningModule(new ConceptDrugLearningMultiFeatureModule(
				new PaceMakeConceptDrugDatabaseInput()));
		LearningSchedular.startSchedular();
		log.debug("modules added");
	}

}
