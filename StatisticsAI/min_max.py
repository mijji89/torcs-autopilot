import pandas as pd

import os # Aggiungi questo import

print(f"Current working directory: {os.getcwd()}") 
# Carica i file CSV con separatore ';'
df = pd.read_csv("Torc\classes\datasetAndre.csv", delimiter=';')
df1 = pd.read_csv("Torc\classes\datasetMic.csv", delimiter=';')
df3 = pd.read_csv("Torc\classes\datasetBet.csv", delimiter=';')
df4 = pd.read_csv("Torc\classes\datasetReb.csv", delimiter=';')
df5 = pd.read_csv("Torc\classes\datasetManovre.csv", delimiter=';')

# Converte tutte le colonne numeriche da stringa a float (se necessario)
df = df.apply(pd.to_numeric, errors='coerce')
df1 = df1.apply(pd.to_numeric, errors='coerce')
df3 = df3.apply(pd.to_numeric, errors='coerce')
df4 = df4.apply(pd.to_numeric, errors='coerce')
df5 = df5.apply(pd.to_numeric, errors='coerce')

# Calcola min e max per ogni variabile
min_valuesAndre= df.min()
max_valuesAndre = df.max()
min_valuesMic = df1.min()
max_valuesMic = df1.max()
min_valuesBet = df3.min()
max_valuesBet = df3.max()
min_valuesReb = df4.min()
max_valuesReb = df4.max()
min_valuesManovre = df5.min()
max_valuesManovre = df5.max()
# Unisce i risultati suddividendoli in minimi e massimi 
print('Minimi:')
all_mins = pd.DataFrame({
    'Minimo1Andre': min_valuesAndre,
    'Minimo2Mic': min_valuesMic,
    'Minimo3Bet': min_valuesBet, 
    'Minimo4Reb': min_valuesReb, 
    'Minimo5Manovre': min_valuesManovre,
})

print(all_mins)

print('Massimi:')
all_maxs = pd.DataFrame({
    'Massimo1Andre': max_valuesAndre,
    'Massimo2Mic': max_valuesMic,
    'Massimo3Bet': max_valuesBet,
    'Massimo4Reb': max_valuesReb,
    'Massimo5Manovre': max_valuesManovre,
})


print(all_maxs)

#Calcolo del minimo tra i minimi e del massimo tra i massimi per ciascuna categoria
final_min=all_mins.min(axis=1)
final_max=all_maxs.max(axis=1)

print('Massimo');
print(final_max);
print('Minimo');
print(final_min);  