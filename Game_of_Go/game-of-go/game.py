import copy
import random
from board import Board


class Game:
    """
    A class that represents a game of GO
    """

    def __init__(self, board_size=9, play_with_computer=False):
        """
        Initialize a new Game instance.
        :param board_size: The size of the board, by default it is 9.
        """
        self.__PLAYER_1 = 1
        self.__PLAYER_2 = 2
        self.__is_player_1_turn = True
        self.__board = Board(board_size, self.__PLAYER_1, self.__PLAYER_2)
        self.__nr_captured_stones_by_player_1 = 0
        self.__nr_captured_stones_by_player_2 = 0
        self.__last_2_boards = []
        self.__game_finished = False
        self.__previous_player_passed = False

        self.__play_with_computer = play_with_computer
        self.__PLAYER_COMPUTER = None

        if self.__play_with_computer:
            self.__PLAYER_COMPUTER = self.__PLAYER_1
            nr_rand = random.randint(1, 2)
            if nr_rand == 2:
                self.__PLAYER_COMPUTER = self.__PLAYER_2

    def make_move(self, row, col):
        """
        Place a stone on the board at the intersection of row and col.
        :param row: a number representing the row
        :param col: a number representing the column
        :return: True if the move is a legal one, False otherwise
        """
        if self.__game_finished:
            return False

        if self.__board.is_legal_move(row, col, self.__PLAYER_1 if self.__is_player_1_turn else self.__PLAYER_2, self.__last_2_boards):
            if self.__is_player_1_turn:
                self.__board.set_cell(row, col, self.__PLAYER_1)
                self.__nr_captured_stones_by_player_1 += self.__board.remove_captured_stones(self.__PLAYER_1)
            else:
                self.__board.set_cell(row, col, self.__PLAYER_2)
                self.__nr_captured_stones_by_player_2 += self.__board.remove_captured_stones(self.__PLAYER_2)

            if len(self.__last_2_boards) < 2:
                self.__last_2_boards.append(copy.deepcopy(self.__board))
            else:
                self.__last_2_boards.pop(0)
                self.__last_2_boards.append(copy.deepcopy(self.__board))

            self.__change_turn()

            if self.__previous_player_passed:
                self.__previous_player_passed = False

            return True

        return False

    def computer_makes_move(self):
        """
        Similar to the make_move method, except that the computer will randomly make a decision
        The computer will pass its turn if after a certain number of times
        (which will be related to the size of the board) still couldn't find a legal move
        """
        size_board = self.__board.get_size()
        max_nr_tries = int(0.2 * size_board * size_board)
        while max_nr_tries > 0:
            random_row = random.randint(0, size_board - 1)
            random_col = random.randint(0, size_board - 1)
            if self.make_move(random_row, random_col):
                break

            max_nr_tries -= 1

        if max_nr_tries == 0:
            self.player_passes()

    def player_passes(self):
        """
        Pass the turn of the player.
        """
        if self.__previous_player_passed:
            self.__game_finished = True
        self.__previous_player_passed = True
        self.__change_turn()

    def previous_player_passed(self):
        """
        :return: True if the previous player passed, Falst otherwise
        """
        return self.__previous_player_passed

    def get_computer_player(self):
        """
        :return: the number of the player for the computer, if the game is not played with the computer
        i.e. play_with_computer=False, then this method will return None
        """
        return self.__PLAYER_COMPUTER

    def is_game_finished(self):
        """
        :return: True if the game is finished, False otherwise
        """
        return self.__game_finished

    def is_played_with_computer(self):
        """
        :return: True if the game is played with the computer, False otherwise
        """
        return self.__play_with_computer

    def __change_turn(self):
        """
        Change the turn of the player. If it is player 1's turn, change it to player 2's turn
        and vice versa.
        """
        self.__is_player_1_turn = not self.__is_player_1_turn

    def get_board_size(self):
        """
        :return: the size of the board
        """
        return self.__board.get_size()

    def get_board(self):
        """
        :return: the board, a list of lists
        """
        return self.__board.get_board()

    def get_current_player(self):
        """
        :return: the current player
        """
        return self.__PLAYER_1 if self.__is_player_1_turn else self.__PLAYER_2

    def get_stones_by_player_1(self):
        """
        :return: the number of stones captured by player 1
        """
        return self.__nr_captured_stones_by_player_1

    def get_stones_by_player_2(self):
        """
        :return: the number of stones captured by player 2
        """
        return self.__nr_captured_stones_by_player_2

    def get_points(self):
        """
        :return: a tuple containing the territory points of the two players and a matrix
        in which the territories for each player are marked with their number
        (player1_p, player2_p, matrix_territories)
        """
        visited = set()
        matrix_territories = [[0 for _ in range(self.__board.get_size())] for _ in range(self.__board.get_size())]

        for nr_row, row in enumerate(self.__board.get_board()):
            for nr_col, value in enumerate(row):
                if (nr_row, nr_col) not in visited:
                    if value != 0:
                        visited.add((nr_row, nr_col))
                        matrix_territories[nr_row][nr_col] = value
                    else:
                        set_boundary, set_group = self.get_boundary_and_group(nr_row, nr_col)
                        visited |= set_group
                        if len(set_boundary) == 1:  # if the territory is surrounded by only one player
                            val_boundary = set_boundary.pop()
                            for cell_group in set_group:
                                matrix_territories[cell_group[0]][cell_group[1]] = val_boundary

        player1_p = 0
        player2_p = 0
        for row in matrix_territories:
            for cell in row:
                if cell == self.__PLAYER_1:
                    player1_p += 1
                elif cell == self.__PLAYER_2:
                    player2_p += 1

        return player1_p, player2_p, matrix_territories

    def get_boundary_and_group(self, row, col):
        """
        This function will implement something similar to the flood fill algorithm.
        :param row: a number representing the row
        :param col: a number representing the column
        :return: a tuple containing a set of the numbers that compute the boundary
        and the group of the empty cell which it is adjacent to the given cell
        (set_boundary, set_group)
        """
        set_boundary = set()
        set_group = {(row, col)}
        queue = [(row, col)]
        while len(queue) != 0:
            current_row, current_col = queue.pop(0)
            for neighbour in self.__board.get_all_neighbours(current_row, current_col):
                value_cell = self.__board.get_value_in_cell(neighbour[0], neighbour[1])
                if value_cell == 0:
                    if neighbour not in set_group:
                        set_group.add(neighbour)
                        queue.append(neighbour)
                else:
                    set_boundary.add(value_cell)

        return set_boundary, set_group



    def __str__(self):
        """
        :return: a string representation of the board
        """
        string = ""
        for row in self.__board.get_board():
            string += str(row) + "\n"
        string += f"Point Player-1 : {self.__nr_captured_stones_by_player_1}\n"
        string += f"Point Player-2 : {self.__nr_captured_stones_by_player_2}\n"

        return string
