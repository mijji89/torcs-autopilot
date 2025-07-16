import pandas as pd

import os # Aggiungi questo import

print(f"Current working directory: {os.getcwd()}") 
# Carica i file CSV con separatore ';'
df = pd.read_csv(r"ProgettoIAfinal\ProgettoIA\Torc\classes\datasetLap.csv", delimiter=';')

# Converte tutte le colonne numeriche da stringa a float (se necessario)
df = df.apply(pd.to_numeric, errors='coerce')

# Calcola min e max per ogni variabile
min_values1= df.min()
max_values1 = df.max()

# Unisce i risultati suddividendoli in minimi e massimi 
print('Minimi:')
all_mins = pd.DataFrame({
    'Minimo1': min_values1

})
print(all_mins)

print('Massimi:')
all_maxs = pd.DataFrame({
    'Massimo1': max_values1
})
print(all_maxs)
 