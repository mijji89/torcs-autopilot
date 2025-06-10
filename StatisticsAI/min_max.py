import pandas as pd


# Carica i file CSV con separatore ';'
df = pd.read_csv("ProgettoIA/Torc/classes/dataset.csv", delimiter=';')

# Converte tutte le colonne numeriche da stringa a float (se necessario)
df = df.apply(pd.to_numeric, errors='coerce')

# Calcola min e max per ogni variabile
min_values = df.min()
max_values= df.max()

print(min_values)
print(max_values)

"""
# Unisce i risultati suddividendoli in minimi e massimi 
print('Minimi:')
all_mins = pd.DataFrame({
    'MinimoBet': min_valuesBet
})

print(all_mins)

print('Massimi:')
all_maxs = pd.DataFrame({
    'MinimoBet': max_valuesBet
})

print(all_maxs)

#Calcolo del minimo tra i minimi e del massimo tra i massimi per ciascuna categoria
final_min=all_mins.min(axis=1)
final_max=all_maxs.max(axis=1)

print('Massimo');
print(final_max);
print('Minimo');
print(final_min);  """