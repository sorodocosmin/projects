import json
import time

import numpy as np
from sklearn.model_selection import LeaveOneOut, cross_val_score, cross_val_predict
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import accuracy_score
from util import extract_train_test_data

class Id3Classifier:
    def __init__(self):
        # maximum information gain criterion
        self.clf = DecisionTreeClassifier(criterion='entropy', max_depth=25000)

    def fit(self, x_train, y_train):
        self.clf.fit(x_train, y_train)

    def predict(self, x_test):
        return self.clf.predict(x_test)

    def cvloo_accuracy(self, x_train, y_train):
        loo = LeaveOneOut()

        y_pred = cross_val_predict(self.clf, x_train, y_train, cv=loo)
        # print(y_pred)
        acc = accuracy_score(y_train, y_pred)
        return acc

    @staticmethod
    def accuracy(y_test, y_predict):
        return accuracy_score(y_test, y_predict)


with open("dict_1.json", 'r') as file:
    dictionary_1 = json.load(file)

with open("dict_2.json", 'r') as file:
    dictionary_2 = json.load(file)

with open("dict_3.json", 'r') as file:
    dictionary_3 = json.load(file)

with open("dict_4.json", 'r') as file:
    dictionary_4 = json.load(file)

id3 = Id3Classifier()
files = ["bare", "lemm", "lemm_stop", "stop"]
dictionaries = [dictionary_1, dictionary_2, dictionary_3, dictionary_4]

for i in range(4):
    X_train, Y_train, X_test, Y_test = extract_train_test_data("preprocessed_lingspam/" + files[i],
                                                               dictionaries[i])
    id3.fit(X_train, Y_train)
    Y_predict = id3.predict(X_train)
    print(f"Train accuracy {files[i]}: ", id3.accuracy(Y_train, Y_predict))

    Y_predict = id3.predict(X_test)
    print(f"Test accuracy {files[i]}: ", id3.accuracy(Y_test, Y_predict))

    cvloo_acc = id3.cvloo_accuracy(X_train, Y_train)
    print(f"CVLOO accuracy {files[i]}: ", cvloo_acc)
