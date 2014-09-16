Raxa Machine Learning
===================

   This project includes set of tools to develop Machine Learning System for Electronic Medical Records (EMR). It can be configured to work across different types of databases & schema designs based on mapping provided during the configuration.

   At current stage the project consists of drug suggestion system which uses UMLS Metathesaurus Knowledge Source and OpenMRS Database to find drugs that can be prescribed for a disease/syndrom/symptom with some other search attributes like age, location etc. of patient. The algorithm uses UMLS and datamining techniques on medical records to find the confidence between diseases, search attribute and drugs prescribed and suggest prescriptions.

Dependancies
====================

   * [UMLS Metathesaurus Database](http://www.nlm.nih.gov/research/umls/knowledge_sources/metathesaurus/)
   * MySQL JDBC connector 5.1.30
   * Tomcat 7.0.53 or greater
   * Gson
   * Medical Record Databases


Setup Procedure
====================

   1. Import the project as Web application.
   2. Change file RaxaMachineLearning/src/config/database.properties with following format. File should contain credentials for medical record database.

      ~~~~~~~~~~
      databaseURL="database URL"
      dbUser="database username"
      dbPassword="database password"
      ~~~~~~~~~~
   3. Install UMLS database on MySQL. [UMLS Installation guide](http://www.ncbi.nlm.nih.gov/books/NBK9683/), [guide for using UMLS Rich Release Format MySQL load script](http://www.nlm.nih.gov/research/umls/implementation_resources/scripts/README_RRF_MySQL_Output_Stream.html)
   4. Change file RaxaMachineLearning/src/config/umlsdatabase.properties with same as above format. File shoud contains credentials for umls database.

Machine Learning Modules
====================

###1. Learning Modules Pool

   **Java Class**: [LearningModulesPool](/src/com/machine/learning/LearningModulesPool.java)

   **Discription**: The class contain static list of learning modules each implementing interface: [LearningModuleInterface](src/com/machine/learning/interfaces/LearningModuleInterface.java). The class provide method to get results for a search query and attributes. It get results from all individual modules and return a combined list fo results. The module all include method to initiat learning procedure for all modules.

   **Socket Interface**: [SearchSocket](/src/com/machine/learning/socket/SearchSocket.java) (ws://{host}/RaxaMachineLearing/ml/search), Sample Json Input String for Socket
   
      `
      {
         searchRequest: { 
            query: 'asthma', 
            features:[{
               name: 'age', 
               value: '24'
            }, {
               name: 'state',
               value: 'CA'
            }]
         }
      }
      `

   **HTML Inteface**: [Webcontent/search.html](/WebContent/search.html), (http://{host}/  RaxaMachineLearing/search.html)

###2. Disease-Drug Relations Learning Module:

   **Java Class** : [ConceptDrugLearningMultiFeatureModule](/src/com/learningmodule/association/conceptdrug/multifeature/ConceptDrugLearningMultiFeatureModule.java)

   **Discription** : An instance of this class will provide the method to get list of drugs for a disease as search query and search attributes from the medical records. Constructor for this class requires instance of class implementing interface [ConceptDrugDatabaseInterface](/src/com/learningmodule/association/conceptdrug/ConceptDrugDatabaseInterface.java). The interface is reqired to provide learning module methods to get relevant medical records, concepts dictonary from database

Drug Recommendation System:
==========================

   **Java Class**: [DiseaseToDrugPrediction](/src/com/umls/search/DiseaseToDrugPrediction.java)

   **Discription**: The class implements the methods to get list of drugs for disease as search query and attributes. It uses UMLS to get list of concepts that are disease and their related drugs from database. The class uses machine learning modules to decide the relative ordering in the drugs and return back the list of drugs.
   
   **WebSocket Interface**: [UmlsSocket](/src/com/machine/learning/socket/UmlsSocket.java) (ws://{host}/RaxaMachineLearing/ml/umls)
      Sample Json Input String for Socket: Same as that of SearchSocket
      `

   **Html Interface**: [WebContent/umlsSenchaApp.html](/WebContent/umlsSenchaApp.html) (http://{host}/RaxaMachineLearning/umlsSenchaApp.html)

###HOW TO:

##### Add Disease-Drug Relation Learning Module for any database

   1) Create a class implementing [ConceptDrugDatabaseInterface](/src/com/learningmodule/association/conceptdrug/ConceptDrugDatabaseInterface.java) for the medical record database. **Sample**: [PaceMakeConceptDrugDatabaseInput](/src/com/pacemaker/association/PaceMakeConceptDrugDatabaseInput.java)
   2) Add following lines in class [ContextListner](/src/com/machine/learning/ContextListener.java)
   
      `
      @Override
      public void contextInitialized(ServletContextEvent arg0) {
        
         // add the ConceptDrugLearningMultiFeatureModule with your database interface
         LearningModulesPool.addLearningModule(new ConceptDrugLearningMultiFeatureModule(new YourDatabaseInterface()));
        
         LearningSchedular.startSchedular();
         log.debug("modules added");
      }
      `
