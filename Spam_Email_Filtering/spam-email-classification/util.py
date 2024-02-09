import os


def extract_train_test_data(file_path, word_dictionary) -> (list[list], list, list[list], list):
    x_train = list()
    x_test = list()

    y_train = list()
    y_test = list()

    list_keys = list(word_dictionary.keys())
    dict_index = {key: i for i, key in enumerate(list_keys)}

    for (root, directories, files) in os.walk(file_path):
        for file_name in files:
            is_spam = 0
            is_test = 0

            if file_name.startswith("spmsg"):
                is_spam = 1

            if 'part10' in root:
                is_test = 1
                y_test.append(is_spam)
            else:
                y_train.append(is_spam)

            full_file_name = os.path.join(root, file_name)
            # print(f"File name: {full_file_name}")

            words_visited = [0] * len(list_keys)
            with open(full_file_name, 'r') as f:
                read_file = f.read()
                list_words = read_file.split()
                for word in list_words:
                    if word in word_dictionary:
                        words_visited[dict_index[word]] += 1

            if is_test == 0:
                x_train.append(words_visited)
            else:
                x_test.append(words_visited)

    return x_train, y_train, x_test, y_test
