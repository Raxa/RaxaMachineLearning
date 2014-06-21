Raxa Machine Learning
===================

This project includes set of tools to develop Machine Learning System for Electronic Medical Records (EMR). It can be configured to work across different types of databases & schema designs based on mapping provided during the configuration. During intial phase, project will support database schema of Raxa EMR which has evolved from OpenMRS 1.9


Dependancies
====================

* Weka - Machine learning library used.
* MySQL JDBC connector 5.1.30
* Tomcat 7.0.53 or greater
* Gson


Setup Procedure
====================

1. Import the project as Web application.
2. Create file database.properties in root folder(RaxaMachineLearning/database.properties) with following format.

<code>

databaseURL="database URL"
dbUser="database username"
dbPassword="database password"

</code>

Learning from database
===================

Use the method learn() in class com.learningmodule.association.conceptdrug.learning.ConceptDrugLearning;


Prediction Method
===================

Use the method predict(String searchQuery) in class com.learningmodule.association.conceptdrug.predictionmodule.predictionMethod
