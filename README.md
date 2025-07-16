# TORCS Autonomous Driver - JavaScript Implementation

## About

This project implements an autonomous driver for TORCS (The Open Racing Car Simulator) using JavaScript. The driver leverages a **K-Nearest Neighbors (KNN)** algorithm with **k=3** to make driving decisions based on sensor input data.

## Features

- **KNN Algorithm**: Uses k=3 to classify driving actions based on nearest training examples.
- **Datasets**: Multiple CSV files containing driving data used for training and testing.
- **Statistical Analysis**: Calculation of minimum and maximum values for each dataset feature to understand data distribution.
- **Normalization (Min-Max Scaling)**: Dataset features are scaled to a fixed range [0,1] using min-max scaling to improve KNN performance and accuracy.

## Dataset

The datasets are provided as CSV files in the `/classes` directory. Each CSV contains sensor readings and corresponding driving commands or labels.

## How it works

1. Load CSV datasets.
2. Perform statistical calculations to find min and max values for each feature.
3. Apply **Min-Max Scaling** normalization to scale features to the [0,1] range.
4. Use the normalized data to train the KNN model.
5. Run the KNN classifier during the simulation to decide the car’s next action.

