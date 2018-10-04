# import libraries

import pandas as pd
import numpy as np


import matplotlib.pyplot as plt
import seaborn as sns


path='https://ibm.box.com/shared/static/q6iiqb1pd7wo8r3q28jvgsrprzezjqk3.csv'

df = pd.read_csv(path)
df.head()

pd.read_csv(
### How to choose the right visualization method:
#When visualizing individual variables, it is important to first understand what type of variable you are dealing with. 
# This will help us find the right visualisation method for that variable.

# list the data types for each column
df.dtypes
df["peak-rpm"].dtypes

#For example, we can calculate the correlation between variables  of type "int64" or "float64" using the method "corr":
df.corr()

#Let's find the scatterplot of "engine-size" and "price":
# Engine size as potential predictor variable of price
sns.regplot(x="engine-size", y="price", data=df)
plt.ylim(0,)
plt.show()

# As the engine-size goes up, the price goes up: this indicates a positive direct correlation between these two variables.
# Engine size seems like a pretty good predictor of price since the regression line is almost a perfect diagonal line. E
# We can examine the correlation between 'engine-size' and 'price' and see it's approximately 0.87:

df[["engine-size", "price"]].corr()

#Negative correlation
# Highway mpg is a potential predictor variable of price: 
sns.regplot(x="highway-mpg", y="price", data=df)
plt.ylim(0,)
plt.show()




# Categorical variables
# These are variables that describe a 'characteristic' of a data unit, and are selected from a small group of categories.
# The categorical variables can have the type "object" or "int64". A good way to visualize categorical variables is by using boxplots.
# Let's look at the relationship between "body-style" and "price":
sns.boxplot(x="body-style", y="price", data=df)
plt.show()
# We see that the distributions of price between the different body-style categories have a significant overlap,
#  and so body-style would not be a good predictor of price.
 
#   Let's examine engine "engine-location" and "price" :
sns.boxplot(x="engine-location", y="price", data=df)
plt.show()

# Here we see that the distribution of price between these two engine-location categories, front and rear,
#  are distinct enough to take engine-location as a potential good predictor of price. 
sns.boxplot(x="drive-wheels", y="price", data=df)
plt.show()

# Here we see that the distribution of price between the different drive-wheels categories differs.
#  As such, drive-wheels could potentially be a predictor of price.



### ANOVA: Analysis of Variance
# The Analysis of Variance  (ANOVA) is a statistical method used to test whether there are significant differences between the means of two or more groups. 
# ANOVA returns two parameters:
#     **F-test score**: ANOVA assumes the means of all groups are the same, calculates how much the actual means deviate from the assumption, and reports it as the F-test score. A larger score means there is a larger difference between the means.
#     **P-value**:  P-value tells us the statistical significance of our calculated score value.
# If our price variable is strongly correlated with the variable we are analyzing, expect ANOVA to return a sizeable F-test score and a small p-value.

grouped_test2=df[['drive-wheels','price']].groupby(['drive-wheels'])
grouped_test2.head(2)

grouped_test2.get_group('4wd')['price']

# ANOVA
# We can use the function 'f_oneway' in the module 'stats'  to obtain the **F-test score** and **P-value**:
f_val, p_val = stats.f_oneway(grouped_test2.get_group('fwd')['price'], grouped_test2.get_group('rwd')['price'], grouped_test2.get_group('4wd')['price'])  
print( "ANOVA results: F=", f_val, ", P =", p_val) 

#This is a great result, with a large F test score showing a strong correlation and a P value of almost 0, implying almost certain statistical significance. But does this mean all three tested groups are all this highly correlated?


