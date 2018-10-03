
Login in the Community Edition of Databricks:
• http://community.cloud.databricks.com/

Create a cluster ( python & scala )  ( we can create only 1 cluster in the community edition )

Wait until the cluster status move from "pending" to "running"

Go to workspace --> create --> notebook --import and select the notebook 

Attach the notebook to the cluster ( open the notebook , click on "attached" then select the cluster )
• Introduction to Apache Spark on Databricks
• Quick Start DataFrames



In Spark we have two libraries to train a machine learning model : 
Ml: uses only dataframes
mllib : uses only RDD
