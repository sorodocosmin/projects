import re
from string import punctuation
from nltk.corpus import stopwords
from nltk.stem.snowball import SnowballStemmer
from nltk.stem import WordNetLemmatizer


regex_remove_spaces_for_emails = re.compile(r'\s*([@._])\s*')
regex_replace_specific_email = re.compile('[^\s]+@[^\s]+')

regex_remove_spaces_for_links = re.compile(r'\s*([/:\-~])\s*')
regex_replace_specific_link = re.compile('((https?://)|(www\.))\S+')


regex_replace_currency_symbols = re.compile('[$€£¥₹]+')

regex_remove_non_alphanumeric = re.compile('[^a-zA-Z0-9]')

set_stopwords = set(stopwords.words('english'))


class Preprocess:
    def __init__(self, text):
        """
        Initialize the class with the text to be preprocessed
        :param text: a string
        """
        self.__text = text
        self.__list_words = []

        self.__stemmer = SnowballStemmer("english")
        self.__lemmatizer = WordNetLemmatizer()
        self.__make_lowercase = False
        self.__replace_numbers = False
        self.__replace_emails = False
        self.__replace_links = False
        self.__replace_currency_symbols = False
        self.__remove_punctuation = False
        self.__remove_stopwords = False
        self.__remove_non_alphanumeric_words = False
        self.__use_snowball_stemmer = False
        self.__use_lemmatizer = False

    def set_all_flags_to_true(self):
        """
        Set all the flags to True, except the lemmatizer one (it will be set to False)
        :return: None
        """
        self.__make_lowercase = True
        self.__replace_numbers = True
        self.__replace_emails = True
        self.__replace_links = True
        self.__replace_currency_symbols = True
        self.__remove_punctuation = True
        self.__remove_stopwords = True
        self.__remove_non_alphanumeric_words = True
        self.__use_snowball_stemmer = True
        self.__use_lemmatizer = False

    def make_lowercase(self):
        """
        Set the flag for making the text lowercase
        :return: None
        """
        self.__make_lowercase = True

    def replace_numbers(self):
        """
        Set the flag for replacing the numbers from the text
        :return: None
        """
        self.__replace_numbers = True

    def replace_emails(self):
        """
        Set the flag for replacing the emails from the text
        :return: None
        """
        self.__replace_emails = True

    def replace_links(self):
        """
        Set the flag for replacing the links from the text
        :return: None
        """
        self.__replace_links = True

    def replace_currency_symbols(self):
        """
        Set the flag for replacing the currency symbols from the text
        :return: None
        """
        self.__replace_currency_symbols = True

    def remove_punctuation(self):
        """
        Set the flag for removing the punctuation from the text
        :return: None
        """
        self.__remove_punctuation = True

    def remove_stopwords(self):
        """
        Set the flag for removing the stopwords from the text
        :return: None
        """
        self.__remove_stopwords = True

    def remove_non_alpha_numeric_words(self):
        """
        Set the flag for removing the non alpha numeric words from the text
        :return: None
        """
        self.__remove_non_alphanumeric_words = True

    def use_snow_stemmer(self):
        """
        Set the flag for using the snowball stemmer, and the flag for using the lemmatizer to False
        :return:
        """
        self.__use_snowball_stemmer = True
        self.__use_lemmatizer = False

    def use_lemmatizer(self):
        """
        Set the flag for using the WordNet lemmatizer, and the flag for using the snowball stemmer to False
        :return:
        """
        self.__use_lemmatizer = True
        self.__use_snowball_stemmer = False

    def __apply_make_all_lowercase(self):
        """
        Make all the text lowercase
        :return: None
        """
        if self.__make_lowercase:
            self.__text = self.__text.lower()

    def __apply_replace_numbers(self):
        """
        Replace all the numbers with the string 'intnumber'
        :return: None
        """
        if self.__replace_numbers:
            self.__text = re.sub('[0-9]+', 'intnumber', self.__text)

    def __apply_replace_emails(self):
        """
        Remove specific emails from the text and replace them with the string 'emailaddr'
        :return: None
        """
        if self.__replace_emails:
            # Replace spaces around punctuation specific to email
            self.__text = regex_remove_spaces_for_emails.sub(r'\1', self.__text)
            # replace specific emails with emailaddr
            self.__text = regex_replace_specific_email.sub('emailaddr', self.__text)

    def __apply_replace_links(self):
        """
        Remove specific urls from the text and replace them with the string 'httpaddr'
        :return: None
        """
        if self.__replace_links:
            self.__text = regex_remove_spaces_for_links.sub(r'\1', self.__text)
            self.__text = regex_replace_specific_link.sub('httpaddr', self.__text)

    def __apply_replace_currency_symbols(self):
        """
        Remove specific currency symbols from the text and replace them with the string 'currencysymbol'
        :return: None
        """
        if self.__replace_currency_symbols:
            self.__text = regex_replace_currency_symbols.sub('currencysymbol', self.__text)

    def __apply_remove_punctuation(self):
        """
        Remove all the punctuation from the text
        :return: None
        """
        if self.__remove_punctuation:
            self.__text = self.__text.translate(str.maketrans(punctuation, ' '*len(punctuation)))

    def __apply_remove_stopwords(self):
        """
        Remove all the stopwords from the list of words
        :return: None
        """
        if self.__remove_stopwords:
            new_list = []
            for word in self.__list_words:
                if word not in set_stopwords:
                    new_list.append(word)

            self.__list_words = new_list

    def __apply_remove_non_alphanumeric_words(self):
        """
        Remove all words which are not alphanumeric from the list of words
        :return: None
        """
        if self.__remove_non_alphanumeric_words:
            new_list = []
            for word in self.__list_words:
                word = regex_remove_non_alphanumeric.sub('', word)

                # eliminate the stop words
                if word != '':
                    new_list.append(word)

    def __apply_use_snowball_stemmer(self):
        """
        Apply the snowball stemmer to the list of words
        :return: None
        """
        if self.__use_snowball_stemmer:

            for i, word in enumerate(self.__list_words):
                self.__list_words[i] = self.__stemmer.stem(word)

    def __apply_use_lemmatizer(self):
        """
        Apply the WordNet lemmatizer to the list of words
        :return: None
        """
        if self.__use_lemmatizer:
            for i, word in enumerate(self.__list_words):
                self.__list_words[i] = self.__lemmatizer.lemmatize(word)

    def get_list_of_words_after_apply_specified_rules(self):
        """
        Get the list of words after applying the specified rules
        :return: a list of strings, strings which represent the words after all the rules which flag
        was set to True were applied
        """

        self.__apply_make_all_lowercase()
        self.__apply_replace_numbers()
        self.__apply_replace_emails()
        self.__apply_replace_links()
        self.__apply_replace_currency_symbols()
        self.__apply_remove_punctuation()

        self.__list_words = self.__text.split()

        self.__apply_remove_stopwords()
        self.__apply_remove_non_alphanumeric_words()
        self.__apply_use_snowball_stemmer()
        self.__apply_use_lemmatizer()

        return self.__list_words

