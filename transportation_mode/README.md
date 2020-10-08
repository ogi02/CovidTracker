# Use of the network

A [xgboost](https://xgboost.ai/) (ensamble of decision trees) is used to predict the mode of transportation to aid the process of eliminating unnecessary deviced in the database. It also helps associate devices: if two devices are connected and using the same mode of transportation, chances are that they are together, and that helps us notify them if they contact someone with covid-19.

## Flask server

The server is used to communicate with the backend of out application. It is given processed data from the device sensors and responds with the predicted mode of transportation

[dataset used](https://www.kaggle.com/fschwartzer/tmd-dataset-5-seconds-sliding-window)
