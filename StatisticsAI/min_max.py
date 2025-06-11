import pandas as pd

import os # Aggiungi questo import

print(f"Current working directory: {os.getcwd()}") 
# Carica i file CSV con separatore ';'
df = pd.read_csv("ProgettoIA\Torc\classes\dataset1Lap.csv", delimiter=';')
df1 = pd.read_csv("ProgettoIA\Torc\classes\dataset2Lap.csv", delimiter=';')
df3 = pd.read_csv("ProgettoIA\Torc\classes\dataset3Lap.csv", delimiter=';')

# Converte tutte le colonne numeriche da stringa a float (se necessario)
df = df.apply(pd.to_numeric, errors='coerce')
df1 = df1.apply(pd.to_numeric, errors='coerce')
df3 = df3.apply(pd.to_numeric, errors='coerce')
# Calcola min e max per ogni variabile
min_values2Lap = df.min()
max_values2Lap = df.max()
min_values1Lap = df1.min()
max_values1Lap = df1.max()
min_values3Lap = df3.min()
max_values3Lap = df3.max()

# Unisce i risultati suddividendoli in minimi e massimi 
print('Minimi:')
all_mins = pd.DataFrame({
    'Minimo1Lap': min_values1Lap,
    'Minimo2Laè': min_values2Lap,
    'Minimo3Laè': min_values3Lap, 
})

print(all_mins)

print('Massimi:')
all_maxs = pd.DataFrame({
    'Minimo1Lap': max_values1Lap,
    'Massimo2Lap': max_values2Lap,
    'Massimo2Lap': max_values3Lap,
})


print(all_maxs)

#Calcolo del minimo tra i minimi e del massimo tra i massimi per ciascuna categoria
final_min=all_mins.min(axis=1)
final_max=all_maxs.max(axis=1)

print('Massimo');
print(final_max);
print('Minimo');
print(final_min);  