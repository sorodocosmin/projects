import copy


class Board:
    """
    A class that represents the board of a game-of-go
    """

    def __init__(self, size, player_1, player_2):
        """
        Initialize a new Board instance.
        :param size: a number representing the size of the board size x size
        :param player_1: a number representing the first player
        :param player_2: a number representing the second player
        """
        self.__size = size
        self.__PLAYER_1 = player_1
        self.__PLAYER_2 = player_2

        self.__board = [[0 for _ in range(size)] for _ in range(size)]

        self.__player_1_groups = []  # a list of sets of the form {(row, col)}
        self.__player_2_groups = []

        self.__player_1_liberties = []
        self.__player_2_liberties = []

    def is_legal_move(self, row, col, nr_player, last_2_boards):
        """
        Check if the stone can be placed at the intersection of row and col.
        :param row: a number representing the row
        :param col: a number representing the column
        :param nr_player: a number representing the player
        :param last_2_boards: a list of the previous 2 boards
        :return: True if the move is legal, False otherwise
        """
        if self.__is_out_of_bounds(row, col):
            # print("Index out of bounds")
            return False

        if self.__is_occupied(row, col):
            # print("Cell is already occupied")
            return False

        if self.__is_suicide_or_ko(row, col, nr_player, last_2_boards):
            # print("Suicidal or KO")
            return False

        return True

    def set_cell(self, row, col, nr_player):
        """
        Put the value of nr_player in the cell located at the intersection of
        row and col, and updates the groups for the respective player and the liberties
        :param row: a number representing the row
        :param col: a number representing the column
        :param nr_player: a number representing the player
        """
        self.__board[row][col] = nr_player
        self.__update_groups_and_liberties(row, col, nr_player)

    def get_board(self):
        """
        :return: the board (which is a list of lists i.e. a matrix)
        """
        return self.__board

    def get_value_in_cell(self, row, col):
        """
        :param row: a number representing the row
        :param col: a number representing the col
        :return: the value which is in the cell determined by row and col
        """
        return self.__board[row][col]

    def get_size(self):
        """
        :return: an integer representing the size of the board
        """
        return self.__size

    def __update_groups_and_liberties(self, row, col, nr_player):
        """
        Add a stone to an existing group or create a new one and update the liberties of the group
        :param row: a number representing the row
        :param col: a number representing the column
        :param nr_player: a number representing the player
        """
        same_groups = self.__player_1_groups
        opponent_groups = self.__player_2_groups
        same_liberties = self.__player_1_liberties
        opponent_liberties = self.__player_2_liberties
        same_neighbours, opponent_neighbours = self.__get_neighbours(row, col, nr_player)

        if nr_player == self.__PLAYER_2:
            same_groups = self.__player_2_groups
            opponent_groups = self.__player_1_groups
            same_liberties = self.__player_2_liberties
            opponent_liberties = self.__player_1_liberties

        if len(same_neighbours) == 0:  # create a new group
            same_groups.append({(row, col)})
            same_liberties.append(self.__get_liberties(row, col))
        else:  # connects to an existing group or merge multiple groups
            groups_to_be_merged_index = []
            for neighbour in same_neighbours:
                for i, group in enumerate(same_groups):
                    if neighbour in group:
                        if i not in groups_to_be_merged_index:
                            groups_to_be_merged_index.append(i)
                            group.add((row, col))
                            same_liberties[i] = self.__get_group_liberties(group)
                        break
            # now, if there is more than 1 group, we need to merge them
            if len(groups_to_be_merged_index) > 1:
                first_group = groups_to_be_merged_index[0]
                for i in range(1, len(groups_to_be_merged_index)):
                    same_groups[first_group] |= same_groups[groups_to_be_merged_index[i]]

                same_liberties[first_group] = self.__get_group_liberties(same_groups[first_group])

                # we know for sure that the list will be sorted in ascending order
                # that's why it is safe to delete in this manner
                # having an auxiliary variable indicating how many groups were already deleted
                # from the original list
                already_deleted = 0
                for i in range(1, len(groups_to_be_merged_index)):
                    del same_groups[groups_to_be_merged_index[i] - already_deleted]
                    del same_liberties[groups_to_be_merged_index[i] - already_deleted]
                    already_deleted += 1

        # update liberties for the opponent
        already_visited_opponent_groups = set()
        for neighbour in opponent_neighbours:
            for i, group in enumerate(opponent_groups):
                if neighbour in group and i not in already_visited_opponent_groups:
                    already_visited_opponent_groups.add(i)
                    opponent_liberties[i] -= 1
                    break

    def remove_captured_stones(self, nr_player):
        """
        Remove the opponent's groups that have 0 liberties and update the liberties of
        the groups of the current player i.e. groups which are neighbours of the captured ones
        :param nr_player: a number representing the player
        :return: an integer representing the number of captured stones
        """
        same_groups = self.__player_1_groups
        same_liberties = self.__player_1_liberties
        opponent_groups = self.__player_2_groups
        opponent_liberties = self.__player_2_liberties

        if nr_player == self.__PLAYER_2:
            opponent_groups = self.__player_1_groups
            opponent_liberties = self.__player_1_liberties
            same_groups = self.__player_2_groups
            same_liberties = self.__player_2_liberties

        captured_stones = 0
        indices_to_remove = []

        for i, group in enumerate(opponent_groups):
            if opponent_liberties[i] == 0:
                captured_stones += len(group)
                for cell in group:
                    self.__board[cell[0]][cell[1]] = 0

                indices_to_remove.append(i)

        # delete the groups that have 0 liberties
        already_deleted = 0
        for index in indices_to_remove:
            del opponent_groups[index - already_deleted]
            del opponent_liberties[index - already_deleted]
            already_deleted += 1

        # update the liberties of the current player's groups
        for i, group in enumerate(same_groups):
            same_liberties[i] = self.__get_group_liberties(group)

        return captured_stones

    def __get_liberties(self, row, col):
        """
        Get the liberties of the singleton group located at the intersection of row and col
        :param row: a number representing the row
        :param col: a number representing the column
        :return: an integer representing the number of liberties of the singleton group
        """
        nr_liberties = 0
        if row - 1 >= 0 and self.__board[row - 1][col] == 0:
            nr_liberties += 1
        if row + 1 < self.__size and self.__board[row + 1][col] == 0:
            nr_liberties += 1
        if col - 1 >= 0 and self.__board[row][col - 1] == 0:
            nr_liberties += 1
        if col + 1 < self.__size and self.__board[row][col + 1] == 0:
            nr_liberties += 1

        return nr_liberties

    def __get_group_liberties(self, group):
        """
        computes the number of liberties for a group
        :param group: a set o tuples
        :return: an integer representing the number of liberties of that group
        """
        neighbours = set()
        for cell in group:
            free_neighbours = self.__get_free_neighbours(cell[0], cell[1])
            neighbours |= free_neighbours

        return len(neighbours)

    def __get_neighbours(self, row, col, nr_player):
        """
        Get the neighbours of the cell located at the intersection of row and col
        which have the same value as nr_player and one with opponent neighbours
        :param row: a number representing the row
        :param col: a number representing the column
        :param nr_player: a number representing the player
        :return: two list of tuples representing the neighbours that already are part of a group
        first one for the nr_player, and the second one for the opponent
        """
        list_same_neighbours = []
        list_opponent_neighbours = []
        if row - 1 >= 0 and self.__board[row - 1][col] != 0:
            if self.__board[row - 1][col] == nr_player:
                list_same_neighbours.append((row - 1, col))
            else:
                list_opponent_neighbours.append((row - 1, col))

        if row + 1 < self.__size and self.__board[row + 1][col] != 0:
            if self.__board[row + 1][col] == nr_player:
                list_same_neighbours.append((row + 1, col))
            else:
                list_opponent_neighbours.append((row + 1, col))

        if col - 1 >= 0 and self.__board[row][col - 1] != 0:
            if self.__board[row][col - 1] == nr_player:
                list_same_neighbours.append((row, col - 1))
            else:
                list_opponent_neighbours.append((row, col - 1))

        if col + 1 < self.__size and self.__board[row][col + 1] != 0:
            if self.__board[row][col + 1] == nr_player:
                list_same_neighbours.append((row, col + 1))
            else:
                list_opponent_neighbours.append((row, col + 1))

        return list_same_neighbours, list_opponent_neighbours

    def __get_free_neighbours(self, row, col):
        """
        Get the neighbours of the cell located at the intersection of row and col
        which are free
        :param row: a number representing the row
        :param col: a number representing the column
        :return: a set of tuples representing the neighbours that are free
        """
        free_neighbours = set()
        if row - 1 >= 0 and self.__board[row - 1][col] == 0:
            free_neighbours.add((row - 1, col))
        if row + 1 < self.__size and self.__board[row + 1][col] == 0:
            free_neighbours.add((row + 1, col))
        if col - 1 >= 0 and self.__board[row][col - 1] == 0:
            free_neighbours.add((row, col - 1))
        if col + 1 < self.__size and self.__board[row][col + 1] == 0:
            free_neighbours.add((row, col + 1))

        return free_neighbours

    def get_all_neighbours(self, row, col):
        """
        Get the neighbours of the cell located at the intersection of row and col
        :param row: a number representing the row
        :param col: a number representing the column
        :return: a list of tuples representing the neighbours
        """
        neighbours = []
        if row - 1 >= 0:
            neighbours.append((row - 1, col))
        if row + 1 < self.__size:
            neighbours.append((row + 1, col))
        if col - 1 >= 0:
            neighbours.append((row, col - 1))
        if col + 1 < self.__size:
            neighbours.append((row, col + 1))

        return neighbours

    def __is_out_of_bounds(self, row, col):
        """
        Check if the row and col are not within the board size
        :param row: a number representing the row
        :param col: a number representing the column
        :return: True if the condition is respected (row and col are out o bounds), False otherwise
        """

        if row < 0 or row >= self.__size:
            return True

        if col < 0 or col >= self.__size:
            return True

        return False

    def __is_occupied(self, row, col):
        """
        Check if there is a stone in the cell at the intersection of row and col
        :param row: a number representing the row
        :param col: a number representing the column
        :return: True if the condition is respected (cell is already occupied), False otherwise
        """
        if self.__board[row][col] != 0:
            return True

        return False

    def __is_suicide_or_ko(self, row, col, nr_player, last_2_boards):
        """
        Check if the stone that should be put at the intersection of row and col will have 0 liabilities (suicide)
        OR
        Check if after this move, the board will be the same as in a previous state also called and KO
        :param row: a number representing the row
        :param col: a number representing the column
        :param nr_player: a number representing the player
        :param last_2_boards: a list of the previous 2 boards for checking the ko rule
        :return: True if the conditions are respected, False otherwise
        """
        old_board = copy.deepcopy(self)
        current_player_liberties = self.__player_1_liberties

        if nr_player == self.__PLAYER_2:
            current_player_liberties = self.__player_2_liberties

        self.set_cell(row, col, nr_player)
        self.remove_captured_stones(nr_player)

        # if after this move, there is at least 1 group with 0 liberties for the current player's groups
        # than this is not a valid move -> it's like you would give to the opponent a free point

        found_liberty_0 = False
        for liberty in current_player_liberties:
            if liberty == 0:
                # print("Suicidal")
                found_liberty_0 = True
                break

        # check if the board is the same as in a previous state aka KO fight
        found_same_previous_board = False
        if not found_liberty_0:
            for previous_board in last_2_boards:
                if self == previous_board:
                    found_same_previous_board = True

        self.__board = old_board.__board
        self.__player_1_groups = old_board.__player_1_groups
        self.__player_2_groups = old_board.__player_2_groups
        self.__player_1_liberties = old_board.__player_1_liberties
        self.__player_2_liberties = old_board.__player_2_liberties

        if found_liberty_0 or found_same_previous_board:
            return True

        return False

    def __eq__(self, other):
        """
        2 boards are equal if they have the same size and the same elements in the same positions
        :param other: any object that will be compared with self
        :return: True if the boards are equal, False otherwise
        """
        if not isinstance(other, Board):
            return False

        if self.__size != other.__size:
            return False

        for i in range(self.__size):
            for j in range(self.__size):
                if self.__board[i][j] != other.__board[i][j]:
                    return False

        return True

    def __str__(self):
        """
        :return: a string representation of the board
        """
        string = ""
        for row in self.__board:
            string += str(row) + "\n"
        return string
