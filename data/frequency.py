import re
from collections import Counter

def load_file(path):
    with open(path, 'r') as f:
        return f.read().lower()

def clean(text):
    text = re.sub(r'[^\w]', ' ', text)
    return text

if __name__ == "__main__":
    path = 'dish_names.txt'
    text = load_file(path)
    words = Counter(clean(load_file(path)).split())
    N = float(sum(words.values()))

    with open("dish_freq.csv", 'w') as f:
        for word in words.keys():
            f.write(word + ':' + str(words[word]) + ':\n')
