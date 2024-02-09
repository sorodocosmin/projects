import json
import time

import numpy as np
import matplotlib.pyplot as plt
from sklearn.model_selection import LeaveOneOut, cross_val_score

from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import accuracy_score
from util import extract_train_test_data

class KnnClassifier:
    def __init__(self, k):
        self.k = k
        self.knn_classifier = KNeighborsClassifier(n_neighbors=self.k, metric='cosine')

    def fit(self, x_train, y_train):
        self.knn_classifier.fit(x_train, y_train)

    def predict(self, x_test):
        return self.knn_classifier.predict(x_test)

    def cvloo_accuracy(self, x_train, y_train):
        loo = LeaveOneOut()
        # crooss validation leave one out
        y_pred = cross_val_score(self.knn_classifier, x_train, y_train, cv=loo)
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

# knn = KnnClassifier(1)
X_train, Y_train, X_test, Y_test = extract_train_test_data("datas/preprocessed_lingspam/bare",
                                                           dictionary_1)

best_k = 3
# accuracies = []
# k_values = range(1, 50)
# for k in k_values:
#     knn = KnnClassifier(k)
#     knn.fit(X_train, Y_train)
#     Y_predict = knn.predict(X_test)
#     score = knn.accuracy(Y_test, Y_predict)
#     accuracies.append(score)
#     print("k = ", k, " -- Accuracy: ", score)
#
# # Plot the results
# plt.plot(k_values, accuracies, marker='o')
# plt.xlabel('Number of Neighbors (k)')
# plt.ylabel('Accuracy')
# plt.title('Best k-value for k-NN')
# plt.show()
#
# # Choose the k value at the elbow point
# best_k = k_values[np.argmax(accuracies)]
# print("Best k value:", best_k, " -- Accuracy: ", accuracies[best_k - 1])

# knn = KnnClassifier(best_k)
# knn.fit(X_train, Y_train)
# Y_predict = knn.predict(X_test)
# print("Accuracy: ", knn.accuracy(Y_test, Y_predict))
# knn.cvloo_accuracy(X_train, Y_train)
#
# X_train, Y_train, X_test, Y_test = extract_train_test_data("preprocessed_lingspam/lemm",
#                                                            dictionary_2)
# knn = KnnClassifier(best_k)
# knn.fit(X_train, Y_train)
# Y_predict = knn.predict(X_test)
# print("Accuracy: ", knn.accuracy(Y_test, Y_predict))
#
# X_train, Y_train, X_test, Y_test = extract_train_test_data("preprocessed_lingspam/lemm_stop",
#                                                            dictionary_3)
# knn = KnnClassifier(best_k)
# knn.fit(X_train, Y_train)
# Y_predict = knn.predict(X_test)
# print("Accuracy: ", knn.accuracy(Y_test, Y_predict))
#
# X_train, Y_train, X_test, Y_test = extract_train_test_data("preprocessed_lingspam/stop",
#                                                            dictionary_4)
# knn = KnnClassifier(best_k)
# knn.fit(X_train, Y_train)
# Y_predict = knn.predict(X_test)
# print("Accuracy: ", knn.accuracy(Y_test, Y_predict))


knn = KnnClassifier(best_k)
files = ["bare", "lemm", "lemm_stop", "stop"]
dictionaries = [dictionary_1, dictionary_2, dictionary_3, dictionary_4]

for i in range(4):
    X_train, Y_train, X_test, Y_test = extract_train_test_data("preprocessed_lingspam/" + files[i],
                                                               dictionaries[i])
    knn.fit(X_train, Y_train)
    Y_predict = knn.predict(X_train)
    print(f"Train accuracy {files[i]}: ", knn.accuracy(Y_train, Y_predict))

    Y_predict = knn.predict(X_test)
    print(f"Test accuracy {files[i]}: ", knn.accuracy(Y_test, Y_predict))

    cvloo_acc = knn.cvloo_accuracy(X_train, Y_train)
    print(f"CVLOO accuracy {files[i]}: ", cvloo_acc)
