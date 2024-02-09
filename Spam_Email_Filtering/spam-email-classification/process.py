import os
from preprocess_data import Preprocess
import json


def compute_new_file(path, list_text_words):
    """
    Create a new file (in the specified path) with the preprocessed text
    :param path: a string with the path to the new file
    :param list_text_words: a list of string with the words to be written in the new file
    :return: None
    """
    os.makedirs(os.path.dirname(path), exist_ok=True)

    # create new_files with the preprocessed text
    with open(path, 'w') as new_file:
        new_file.write(" ".join(list_text_words))


def get_dictionary_from_file(path):
    """
    :param path: path to a json file
    :return: a dictionary with the datas from the file
    """
    with open(path, 'r') as file:
        return json.load(file)


def get_list_tests(path):
    """
    :param path: path to a folder with the test datas
    :return: a list of the tests
    """
    list_tests = []
    for (root, directories, files) in os.walk(path):
        for file_name in files:
            is_spam_mail = False
            if file_name.startswith("spmsg"):
                is_spam_mail = True

            with open(os.path.join(root, file_name), 'r') as file:
                read_file = file.read()
                list_words = read_file.split()
                list_tests.append((list_words, is_spam_mail))

    return list_tests


class Process:
    def __init__(self, path_to_folder, name_folder_for_test,
                 create_a_new_folder_with_processed_files=False, new_folder_path="new_datas_processed"):
        """
        Initialize the class with the path to the folder with the datas
        :param path_to_folder: a string to the path of the folder which contains the datas
        :param name_folder_for_test: the name of the folder which contains the test datas
        :param create_a_new_folder_with_processed_files: True if you want to create a new folder with the preprocessed
        datas, False otherwise
        :param new_folder_path: a string with the name of the folder that you want to be created in which will be
        the new datas (after preprocessing)
        """
        self.__path_to_folder = path_to_folder
        self.__name_folder_for_test = name_folder_for_test
        self.__create_a_new_folder_with_processed_files = create_a_new_folder_with_processed_files
        self.__new_folder_path = new_folder_path

        # for every folder : bare, lemm, lemm_stop, stop
        self.__list_tests_datas = [list(), list(), list(), list()]

        # every element of the list of the list (i.e. list_tests_datas[i]) will look like this:
        # ([list_words_1, list_words_2, list_words_3, ...], True/False)
        # True if the file is spam, False otherwise
        self.__list_dictionary = [dict(), dict(), dict(), dict()]  # for every folder
        # the format of a dictionary
        # "word_1" : [nr_1, nr_2]
        # "word_2" : [nr_3, nr_4]
        # "word_3" : [nr_appears_in_ham, nr_app_in_spam]

    def process(self):
        """
        Read the datas from the folder and preprocess them, create the dictionary(train datas), the test datas and
        if True, create a new folder with the preprocessed datas
        :return: None
        """

        for (root, directories, files) in os.walk(self.__path_to_folder):
            for file_name in files:
                index = None
                is_spam = False
                is_test = False

                if file_name.startswith("spmsg"):
                    is_spam = True

                if 'part10' in root:
                    is_test = True

                if "bare" in root:
                    index = 0
                elif "lemm_stop" in root:
                    index = 2
                elif "lemm" in root:
                    index = 1
                elif "stop" in root:
                    index = 3

                full_file_name = os.path.join(root, file_name)

                print(f"Processing file {full_file_name}")

                # read from the file
                with open(full_file_name, 'r') as file:
                    read_file = file.read()
                    # preprocess the text
                    preprocess = Preprocess(read_file)
                    # preprocess.set_all_flags_to_true()
                    preprocess.make_lowercase()
                    preprocess.remove_punctuation()
                    preprocess.remove_non_alpha_numeric_words()

                    list_words = preprocess.get_list_of_words_after_apply_specified_rules()

                if self.__create_a_new_folder_with_processed_files:
                    new_file_name = full_file_name.replace(self.__path_to_folder, self.__new_folder_path)
                    compute_new_file(new_file_name, list_words)

                self.__compute_dictionary(list_words, index, is_spam, is_test)

    def __compute_dictionary(self, list_words, index, is_spam, is_test):
        """
        Compute the dictionary for the given list of words
        :param list_words: a list of strings, the words
        :param index: an integer, the index of the dictionary ( ex index 0 --> for dict corresponding to bare folder)
        :param is_spam: True if the file is spam, False otherwise
        :param is_test: True if the file is from the test datas, False otherwise
        :return: None
        """
        curr_dict = self.__list_dictionary[index]
        if not is_test:
            for word in list_words:
                if word not in curr_dict:
                    if is_spam:
                        curr_dict[word] = [0, 1]
                    else:
                        curr_dict[word] = [1, 0]
                else:
                    if is_spam:
                        curr_dict[word][1] += 1
                    else:
                        curr_dict[word][0] += 1
        else:
            # add to test list -- part10
            self.__list_tests_datas[index].append((list_words, is_spam))

    def get_dictionary(self, index):
        """
        Get the dictionary for the given index
        :param index: an integer, the index of the dictionary - representing the folder(ex: 2 --> lemm_stop folder)
        :return: a dictionary, the dictionary for the given index or None, if the index is not valid
        """
        if index < 0 or index >= len(self.__list_dictionary):
            return None

        return self.__list_dictionary[index]

    def get_test_datas(self, index):
        """
        Get the test datas for the given index
        :param index: an integer, the index of the test datas - representing the folder(ex: 2 --> lemm_stop folder)
        :return: a list of lists of strings, the test datas for the given index or None, if the index is not valid
        """
        if index < 0 or index >= len(self.__list_tests_datas):
            return None

        return self.__list_tests_datas[index]

    def write_dictionary_in_json_file(self, index_dictionary, name_file):
        """
        Write the dictionary in a json file
        :param index_dictionary: an integer, the index of the dictionary-representing the folder(ex: 3 --> stop folder)
        :param name_file: the name of the file in which the dictionary will be written
        :return: None
        """
        if index_dictionary < 0 or index_dictionary >= len(self.__list_dictionary):
            return None

        with open(name_file, 'w') as file:
            json.dump(self.__list_dictionary[index_dictionary], file)


# if __name__ == "__main__":
#     # process the datas
#     process = Process("lingspam_public", "part10", create_a_new_folder_with_processed_files=True,
#                       new_folder_path="preprocessed_simple_lingspam")
#     process.process()
#
#     # write the dictionary in a json file
#     process.write_dictionary_in_json_file(0, "dict_bare_1.json")
#     process.write_dictionary_in_json_file(1, "dict_lemm_1.json")
#     process.write_dictionary_in_json_file(2, "dict_lemm_stop_1.json")
#     process.write_dictionary_in_json_file(3, "dict_stop_1.json")
