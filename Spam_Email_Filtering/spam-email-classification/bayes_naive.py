import random
import numpy as np
import os
from process import get_dictionary_from_file
from process import get_list_tests
import time
from plot import print_plot
# use numpy for log2 function
# as it is faster than math.log2 because numpy was written in C


class Bayes_Naive:
    def __init__(self, dictionary):
        """
        Initialize the class with the dictionary, which will contain the datas for the spam and ham
        :param dictionary: a dictionary with the following structure:
                            "word_1" : [nr_1, nr_2]
                            "word_2" : [nr_3, nr_4]
                            ........................
                            "word_n" : [nr_appears_in_ham, nr_appears_in_spam]
        """
        self.__dictionary = dictionary

        self.__spam_count = 0
        self.__ham_count = 0

    def train(self):
        """
        For Bayes Naive, the training part is to compute the probability of spam and ham
        """

        for list_nr_ham_spam in self.__dictionary.values():
            self.__spam_count += list_nr_ham_spam[1]
            self.__ham_count += list_nr_ham_spam[0]

    def test_single_instance(self, list_words):
        """
        Test the given list of words
        :param list_words: a list of words
        :return: True if the text is spam, False otherwise
        """
        probability_spam = np.log2(self.__spam_count / (self.__spam_count + self.__ham_count))
        probability_ham = np.log2(self.__ham_count / (self.__spam_count + self.__ham_count))
        # argmax P(hypotesis | data) = argmax P(data | hypotesis) * P(hypotesis)
        # P(data | hypotesis) = P(word_1 | hypotesis) * P(word_2 | hypotesis) * ... * P(word_n | hypotesis)

        need_rule_of_laplace = False

        for word in list_words:
            if word in self.__dictionary:
                if self.__dictionary[word][0] == 0 or self.__dictionary[word][1] == 0:
                    # if the probability of a word is 0, we need to apply the rule of Laplace
                    need_rule_of_laplace = True
                    break

                probability_spam += np.log2(self.__dictionary[word][1] / self.__spam_count)
                probability_ham += np.log2(self.__dictionary[word][0] / self.__ham_count)

        if need_rule_of_laplace:
            probability_spam = np.log2(self.__spam_count / (self.__spam_count + self.__ham_count))
            probability_ham = np.log2(self.__ham_count / (self.__spam_count + self.__ham_count))
            for word in list_words:
                if word in self.__dictionary:
                    probability_spam += np.log2((self.__dictionary[word][1] + 1) / (self.__spam_count + 2))
                    probability_ham += np.log2((self.__dictionary[word][0] + 1) / (self.__ham_count + 2))
                # if a word is not found in the dictionary, i.e. it is not in the training set
                # we will skip it

        if probability_spam > probability_ham:
            return 1
        elif probability_spam < probability_ham:
            return 0
        else:
            # if probabilities are equal, return a random number
            random_nr = random.randint(0, 1)
            return random_nr

    def test(self, list_tests):
        """
        Test the given list of tests and returns the accuracy
        :param list_tests: a list of tuples of list of words and True/False
        ex : [([list_words_1, list_words_2, list_words_3, ...], True/False), ...]
        True -> that test is spam
        False -> that test is ham
        :return: a float number representing the accuracy
        """
        correct_classified = 0
        total_classified = 0
        for test in list_tests:
            list_words = test[0]
            is_spam = test[1]
            result = self.test_single_instance(list_words)

            if result == 1 and is_spam:
                correct_classified += 1
            elif result == 0 and not is_spam:
                correct_classified += 1

            total_classified += 1

        if total_classified == 0:
            return 0

        return correct_classified / total_classified

    def __update_dictionary(self, list_words, is_spam, to_original):
        """
        When doing CVLOO, for each instance, we need to update the dictionary
        we remove the instance from the dictionary when we test it
        :param list_words:
        :param is_spam:
        :param to_original: if True, we add the instance to the dictionary, otherwise we remove it
        """
        for word in list_words:
            if is_spam:
                if to_original:
                    self.__dictionary[word][1] += 1
                else:  # we remove this instance from the dictionary
                    self.__dictionary[word][1] -= 1

            else:
                if to_original:
                    self.__dictionary[word][0] += 1
                else:
                    self.__dictionary[word][0] -= 1

    def CVLOO(self, path_to_folder):
        """
        Perform the Cross Validation Leave One Out
        it will go through all the processed data and test each of them
        :param path_to_folder: a string, the path to the folder containing the processed data
        :return: a float number representing the accuracy
        """
        correct_classified = 0
        total_classified = 0
        for (root, directories, files) in os.walk(path_to_folder):
            for file_name in files:

                if 'part10' in root:
                    continue

                is_spam_mail = False
                if file_name.startswith("spmsg"):
                    is_spam_mail = True

                total_classified += 1

                with open(os.path.join(root, file_name), 'r') as file:
                    read_file = file.read()
                    list_words = read_file.split()
                    self.__update_dictionary(list_words, is_spam_mail, False)
                    self.train()
                    result = self.test_single_instance(list_words)
                    if result == 1 and is_spam_mail:
                        # print(f"Correctly classified file {file_name}")
                        correct_classified += 1
                    elif result == 0 and not is_spam_mail:
                        # print(f"Correctly classified file {file_name}")
                        correct_classified += 1
                    # else:
                    #     print(f"Wrong classification for file {file_name}")
                    self.__update_dictionary(list_words, is_spam_mail, True)

        if total_classified == 0:
            return 0

        return correct_classified / total_classified


if __name__ == '__main__':
    cvloo_folders = []
    test_folders = []
    list_accuracy_training = []
    list_parts = [f"part{i}" for i in range(1, 10)]

    list_folders = ["bare", "lemm", "lemm_stop", "stop"]
    for folder in list_folders:
        start_time = time.time()
        dictionary_folder = get_dictionary_from_file(f'dict_{folder}_1.json')
        print(f"Length for {folder} dictionary : {len(dictionary_folder)}")

        BN = Bayes_Naive(dictionary_folder)
        BN.train()
        tests = get_list_tests(f'preprocessed_simple_lingspam/{folder}/part10')
        accuracy_test = BN.test(tests)
        print(f"Accuracy for testing Bayes-Naive - {accuracy_test}")
        test_folders.append(accuracy_test)
        path_training = [f"preprocessed_simple_lingspam/{folder}/part{i}" for i in range(1, 10)]
        accuracy_training = 0
        list_accuracy_training_part_i = list()
        for i in range(1, 10):
            tests = get_list_tests(path_training[i - 1])
            accuracy_training_part_i = BN.test(tests)
            accuracy_training += accuracy_training_part_i
            print(f"Accuracy training for part {i} - {accuracy_training_part_i}")

            list_accuracy_training_part_i.append(accuracy_training_part_i)

        list_accuracy_training.append(list_accuracy_training_part_i)

        cvloo_accuracy = BN.CVLOO(f'preprocessed_simple_lingspam/{folder}')
        cvloo_folders.append(cvloo_accuracy)
        print(f"Accuracy training for all parts - {accuracy_training / 9}")
        print(f"Accuracy for CVLOO : {cvloo_accuracy}")

        print(f"Time taken : {time.time() - start_time}")

    for i in range(4):
        print_plot(list_accuracy_training[i], list_parts, f'Accuracy Training-folder {list_folders[i]} for Each Part', 'Parts', 'Accuracy', color='skyblue',
                   horizontal_line_value=1)

    print_plot(cvloo_folders, list_folders, f"Accuracy CVLOO for each folder", "Name folder", "Accuracy", color="steelblue", horizontal_line_value=1)
    print_plot(test_folders, list_folders, f"Accuracy testing for each folder", "Name folder", "Accuracy", color="forestgreen", horizontal_line_value=1)

