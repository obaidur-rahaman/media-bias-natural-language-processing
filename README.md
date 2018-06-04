# Media-bias-natural-language-processing

## Overview



## Getting started

You can make a clone of the repository from Github on your local machine using the following command (prerequisite: you need git installed on your system):

$ git clone https://github.com/data-doctors/kaggle-house-prices-advanced-regression-techniques

## Data

`data` folder contains original data

## Repository structure

01-eda: Exploratory data analysis

Plot distribution of the numerical features examine the skewness
Plot correlation matrix between the features

02-cleaning: Cleaning and preprocessing of data

remove skewenes of target features
handle missing values in categorical features
handle missing values in numerical features
feature selection

03-feature_engineering: Engineering new features 

Some examples:

A total area was created as a new feature by adding the basement area and living area.
The number of bathrooms were added together to create a new feature.
For numerical features with significant skewness, logariths were taken to create new features.
Some features were dropped that did not contribute significantly in predicting the SalePrice.

04-modelling: Fitting different models on the cleaned data and predict the house price on test set


## Training

### Training all models (bulk training)

The hyperparameters of all the single models were optimized by maximizing the cross validation score using the training set
In order to train all the models (kept in models/tuning folder) in series the following shell script can be executed:

'''
$ ./run_all.sh
RMSE-xgb-CV(7)=0.15017262592+-0.0403780999289
RMSE-lgb-CV(7)=0.230416102431+-0.0982360336472
RMSE-rf-CV(7)=0.178752572944+-0.0495588133233
RMSE-et-CV(7)=0.177138296419+-0.0523244324721
RMSE-lasso-CV(7)=0.167043833535+-0.0590946122368
RMSE-ridge-CV(7)=0.16305872566+-0.0592719750453
RMSE-elasticnet-CV(7)=0.166431639245+-0.0591651827043
'''

Then the optimized parameters were plugged in the single models that are kept in models/single folder.

### Saving submision of single model

```
$ python models/single/model_xgb.py save
```

### Scores

Best single models:

| Model                      | CV               | LB      |
| :------------------------- |:----------------:| :-------|
| DecisionTreeRegressor      | 0.19013+-0.01304 | 0.18804 |
| RandomForestRegressor      | 0.14744+-0.00871 | 0.14623 |
| ExtraTreesRegressor        | 0.13888+-0.01208 | 0.15194 |
| XGBoost                    | 0.12137+-0.01128 | 0.12317 |
| LightGBM                   | 0.20030+-0.01182 | 0.21416 |
| Lasso                      | 0.11525+-0.01191 | 0.12091 |
| Ridge                      | 0.11748+-0.01170 | 0.12263 |
| ElasticNet                 | 0.11364+-0.01677 | 0.11976 |
| SVM                        | 0.19752+-0.01386 | 0.20416 |

## Acknowledgments

The Ames Housing dataset was compiled by Dean De Cock for use in data science education. It's an incredible alternative for data scientists looking for a modernized and expanded version of the often cited Boston Housing dataset. 

## Team
- [Obaidur Rahaman](https://github.com/obaidur-rahaman)
- [Marco Di Vivo](https://github.com/divivoma)
- [Benjamin Melloul]()
- [Ayush Kumar](https://github.com/swifty1)
- [Robert Jonczy](https://github.com/rjonczy)

