import json
import time

import numpy as np
import matplotlib.pyplot as plt
from sklearn.model_selection import cross_val_score, cross_val_predict, LeaveOneOut

from sklearn.neighbors import KNeighborsClassifier
from sklearn.ensemble import AdaBoostClassifier
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import accuracy_score
from util import extract_train_test_data

class AdaboostClassifier:
    def __init__(self, iterations):
        # decision stumps as weak learners
        self.weak_clf = DecisionTreeClassifier(criterion='entropy', max_depth=1)
        self.adaboost_clf = AdaBoostClassifier(estimator=self.weak_clf, n_estimators=iterations)

    def fit(self, x_train, y_train):
        self.adaboost_clf.fit(x_train, y_train)

    def predict(self, x_test):
        return self.adaboost_clf.predict(x_test)

    def cvloo_accuracy(self, x_train, y_train):
        loo = LeaveOneOut()

        y_pred = cross_val_predict(self.adaboost_clf, x_train, y_train, cv=loo)
        # print(y_pred)
        acc = accuracy_score(y_train, y_pred)
        # print("Cross validation leave one out accuracy: ", acc)
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

X_train, Y_train, X_test, Y_test = extract_train_test_data("datas/preprocessed_lingspam/bare",
                                                           dictionary_1)

best_T = 4
# accuracies = []
# T_values = range(2, 50)
# for T in T_values:
#     ab = AdaboostClassifier(T)
#     ab.fit(X_train, Y_train)
#     Y_predict = ab.predict(X_test)
#     score = ab.accuracy(Y_test, Y_predict)
#     accuracies.append(score)
#     print("T = ", T, " -- Accuracy: ", score)
#
# # Plot the results
# plt.plot(T_values, accuracies, marker='o')
# plt.xlabel('Number of Iterations (T)')
# plt.ylabel('Accuracy')
# plt.title('Best T-value for AdaBoost')
# plt.show()
#
# # Choose the T value at the elbow point
# best_T = T_values[np.argmax(accuracies)]
# print("Best T value:", best_T, " -- Accuracy: ", accuracies[best_T - 1])

# ab = AdaboostClassifier(best_T)

# ab.fit(X_train, Y_train)
# start_time = time.time()
# Y_predict = ab.predict(X_test)
# print("Accuracy test: ", ab.accuracy(Y_test, Y_predict))

# X_train, Y_train, X_test, Y_test = extract_train_test_data("preprocessed_lingspam/lemm",
#                                                            dictionary_2)
# ab.fit(X_train, Y_train)
# Y_predict = ab.predict(X_test)
# print("Accuracy: ", ab.accuracy(Y_test, Y_predict))
#
# X_train, Y_train, X_test, Y_test = extract_train_test_data("preprocessed_lingspam/lemm_stop",
#                                                            dictionary_3)
# ab.fit(X_train, Y_train)
# Y_predict = ab.predict(X_test)
# print("Accuracy: ", ab.accuracy(Y_test, Y_predict))
#
# X_train, Y_train, X_test, Y_test = extract_train_test_data("preprocessed_lingspam/stop",
#                                                            dictionary_4)
# ab.fit(X_train, Y_train)
# Y_predict = ab.predict(X_test)
# print("Accuracy: ", ab.accuracy(Y_test, Y_predict))

ab = AdaboostClassifier(best_T)
files = ["bare", "lemm", "lemm_stop", "stop"]
dictionaries = [dictionary_1, dictionary_2, dictionary_3, dictionary_4]

for i in range(4):
    X_train, Y_train, X_test, Y_test = extract_train_test_data("preprocessed_lingspam/" + files[i],
                                                               dictionaries[i])
    ab.fit(X_train, Y_train)
    Y_predict = ab.predict(X_train)
    print(f"Train accuracy {files[i]}: ", ab.accuracy(Y_train, Y_predict))

    Y_predict = ab.predict(X_test)
    print(f"Test accuracy {files[i]}: ", ab.accuracy(Y_test, Y_predict))

    cvloo_acc = ab.cvloo_accuracy(X_train, Y_train)
    print(f"CVLOO accuracy {files[i]}: ", cvloo_acc)
