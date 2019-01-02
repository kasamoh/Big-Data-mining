# Time Usage Analysis #
## Spark - Scala - Sparksql - DataFrames - DataSets ##

Our goal is to identify three groups of activities:

   - primary needs (sleeping and eating),
   - work,
   - other (leisure).

And then to observe how do people allocate their time between these three kinds of activities, 
and if we can see differences between men and women, employed and unemployed people, and young (less than 22 years old), 
active (between 22 and 55 years old) and elder people.

At the end of the project ,  we will be able to answer the following questions based on the dataset:

    how much time do we spend on primary needs compared to other activities?
    do women and men spend the same amount of time in working?
    does the time spent on primary needs change when people get older?
    how much time do employed people spend on leisure compared to unemployed people?

To achieve this, we will first read the dataset with Spark, transform it into an intermediate dataset which will be easier to work with
for our use case, and finally compute information that will answer the above questions.


The dataset is provided by Kaggle and is documented here:

https://www.kaggle.com/bls/american-time-use-survey

The file uses the comma-separated values format: 
the first line is a header defining the field names of each column, and every following line contains an information record, 
which is itself made of several columns. It contains information about how people spend their time (e.g., sleeping, eating, 
working, etc.).
