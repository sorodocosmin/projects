import pygame
import sys


class App:
    def __init__(self, game):
        """
        Initialize the App class, which is responsible for the graphical user interface.
        :param game: an object of type Game
        """
        self.__COLOR_PLAYER_1 = (0, 0, 0)
        self.__COLOR_PLAYER_2 = (255, 255, 255)
        self.__COLOR_BACKGROUND = (255, 178, 102)
        self.__COLOR_GRID = (0, 0, 0)
        self.__MARGIN_PERCENTAGE = 0.1
        self.__STONE_RADIUS_FACTOR = 0.3
        self.__TOP_LEFT_BUTTON = (10, 10)
        self.__TOP_RIGHT_BUTTON = (60, 25)

        self.__game = game
        self.__PLAYER_1 = 1
        self.__PLAYER_2 = 2

        pygame.init()

        screen_info = pygame.display.Info()
        screen_width, screen_height = screen_info.current_w, screen_info.current_h
        min_dimension = min(screen_width - 100, screen_height - 100)

        self.__SIZE_BOARD = self.__game.get_board_size() - 1
        self.__SIZE_MARGIN = int(min_dimension * self.__MARGIN_PERCENTAGE)
        self.__SIZE_GRID = int((min_dimension - 2 * self.__SIZE_MARGIN) / self.__SIZE_BOARD)
        self.__SIZE_STONE_RADIUS = int(self.__SIZE_GRID * self.__STONE_RADIUS_FACTOR)
        self.__SIZE_SCREEN = self.__SIZE_BOARD * self.__SIZE_GRID + 2 * self.__SIZE_MARGIN

        self.__screen = pygame.display.set_mode((self.__SIZE_SCREEN, self.__SIZE_SCREEN))

        self.__app_running = False

    def run(self):
        """
        Run the game.
        """
        pygame.display.set_caption("Game of GO")

        self.__app_running = True

        self.__draw_board()
        while self.__app_running:
            self.__handle_events()
            pygame.display.flip()

        pygame.quit()
        sys.exit()

    def __draw_board(self):
        """
        Draw the board with the specified size.
        """
        self.__screen.fill(self.__COLOR_BACKGROUND)  # Fill the background with the desired color

        # Draw horizontal lines
        for i in range(self.__SIZE_BOARD + 1):
            pygame.draw.line(self.__screen, self.__COLOR_GRID,
                             (self.__SIZE_MARGIN, i * self.__SIZE_GRID + self.__SIZE_MARGIN),
                             (self.__SIZE_SCREEN - self.__SIZE_MARGIN, i * self.__SIZE_GRID + self.__SIZE_MARGIN),
                             2)

        # Draw vertical lines
        for j in range(self.__SIZE_BOARD + 1):
            pygame.draw.line(self.__screen, self.__COLOR_GRID,
                             (j * self.__SIZE_GRID + self.__SIZE_MARGIN, self.__SIZE_MARGIN),
                             (j * self.__SIZE_GRID + self.__SIZE_MARGIN, self.__SIZE_SCREEN - self.__SIZE_MARGIN),
                             2)

        # draw the stones
        board = self.__game.get_board()
        for nr_row, row in enumerate(board):
            for nr_col, cell in enumerate(row):
                if cell == self.__PLAYER_1:
                    self.__draw_stone(nr_row, nr_col, self.__COLOR_PLAYER_1)
                elif cell == self.__PLAYER_2:
                    self.__draw_stone(nr_row, nr_col, self.__COLOR_PLAYER_2)

        self.__draw_button(self.__TOP_LEFT_BUTTON[0], self.__TOP_LEFT_BUTTON[1],
                           self.__TOP_RIGHT_BUTTON[0], self.__TOP_RIGHT_BUTTON[1],
                           "Pass", 'red')

    def __draw_button(self, x1, y1, x2, y2, text, color='gray'):
        """
        Draw a button on the screen.
        :param x1: top-left x coordinate
        :param y1: top-left y coordinate
        :param x2: bottom-right x coordinate
        :param y2: bottom-right y coordinate
        :param text: a string representing the text of the button
        :param color: a string representing the color of the text
        """
        button_text = pygame.font.Font('freesansbold.ttf', 18).render(text, True, color)
        button_rect = pygame.rect.Rect((x1, y1), (x2, y2))
        pygame.draw.rect(self.__screen, 'gray', button_rect, 0, 5)
        pygame.draw.rect(self.__screen, 'black', button_rect, 2, 5)
        self.__screen.blit(button_text, (x1 + 5, y1 + 5))

    def __handle_events(self):
        """
        Handle the events of the game
        """
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                self.__app_running = False
            elif event.type == pygame.MOUSEBUTTONDOWN and event.button == 1:  # left mouse button was clicked
                self.__handle_left_mouse_button_click()

        if (self.__game.is_played_with_computer() and
                (self.__game.get_computer_player() == self.__game.get_current_player())):
            if not self.__game.is_game_finished():
                self.__game.computer_makes_move()
                self.__draw_board()
            # possible that the computer choose to pass
            if self.__game.is_game_finished():
                self.__draw_board()
                self.__draw_button(200, 10, 150, 25, "Game Finished", 'blue')
                self.__draw_score()
            elif self.__game.previous_player_passed():
                self.__draw_button(200, 10, 220, 25, "Previous Player Passed", 'green')

    def __handle_left_mouse_button_click(self):
        """
        When the left mouse button was clicked, place a stone on the board
        for the player whose turn it is or check if the pass button was clicked.
        """
        # if the computer is making a move, the human clicks won t matter
        if ((not self.__game.is_played_with_computer()) or
                (self.__game.is_played_with_computer() and
                 self.__game.get_computer_player() != self.__game.get_current_player())):

            mouse_x, mouse_y = pygame.mouse.get_pos()

            # the pass button was clicked
            if self.__TOP_LEFT_BUTTON[0] <= mouse_x <= self.__TOP_LEFT_BUTTON[0] + self.__TOP_RIGHT_BUTTON[0] and \
                    self.__TOP_LEFT_BUTTON[1] <= mouse_y <= self.__TOP_LEFT_BUTTON[1] + self.__TOP_RIGHT_BUTTON[1]:
                self.__game.player_passes()

                if self.__game.is_game_finished():
                    self.__draw_board()
                    self.__draw_button(200, 10, 150, 25, "Game Finished", 'blue')
                    self.__draw_score()
                else:
                    self.__draw_button(200, 10, 220, 25, "Previous Player Passed", 'green')
            else:
                clicked_row, clicked_col = self.__closest_intersection_point(mouse_x, mouse_y)
                if self.__game.make_move(clicked_row, clicked_col):
                    self.__draw_board()

    def __draw_text(self, text, x, y, font_size, text_color, background_color):
        """
        Draw text on the screen.
        :param text: string representing the text to be drawn
        :param x: x coordinate of the text
        :param y: y coordinate of the text
        :param font_size: size of the font
        :param text_color: color of the text
        :param background_color: background color of the text
        """

        font = pygame.font.Font("freesansbold.ttf", font_size)

        text_surface = font.render(text, True, text_color, background_color)
        text_rect = text_surface.get_rect()
        text_rect.topleft = (x, y)

        self.__screen.blit(text_surface, text_rect)

    def __draw_score(self):
        """
        Draw the final score of the game.
        """

        player_1_points_territory, player_2_points_territory, matrix_territory = self.__game.get_points()

        player_1_stones_captured = self.__game.get_stones_by_player_1()
        player_2_stones_captured = self.__game.get_stones_by_player_2()

        self.__draw_text('Player 1 score : ', 10, self.__SIZE_SCREEN - 45, 14, 'black', 'white')
        self.__draw_text(f'{player_1_stones_captured} stones captured', 10, self.__SIZE_SCREEN - 30, 14, 'black',
                         'white')
        self.__draw_text(f'{player_1_points_territory} points territory', 10, self.__SIZE_SCREEN - 15, 14, 'black',
                         'white')

        self.__draw_text(f'Player 2 score : ', self.__SIZE_SCREEN - 150, self.__SIZE_SCREEN - 45, 14, 'white', 'black')
        self.__draw_text(f'{player_2_stones_captured} stones captured', self.__SIZE_SCREEN - 150,
                         self.__SIZE_SCREEN - 30, 14, 'white', 'black')
        self.__draw_text(f'{player_2_points_territory} points territory', self.__SIZE_SCREEN - 150,
                         self.__SIZE_SCREEN - 15, 14, 'white', 'black')

        if player_1_stones_captured + player_1_points_territory > player_2_stones_captured + player_2_points_territory:
            self.__draw_text('Player 1 won!', self.__SIZE_SCREEN // 2 - 100, self.__SIZE_SCREEN - 35, 28, 'black',
                             'red')
        else:
            self.__draw_text('Player 2 won!', self.__SIZE_SCREEN // 2 - 100, self.__SIZE_SCREEN - 35, 28, 'white',
                             'red')

        for nr_row, row in enumerate(matrix_territory):
            for nr_col, cell in enumerate(row):
                if cell == self.__PLAYER_1:
                    self.__draw_square_territory(nr_row, nr_col, (0, 255, 0))
                elif cell == self.__PLAYER_2:
                    self.__draw_square_territory(nr_row, nr_col, (0, 0, 255))

    def __draw_stone(self, row, col, color):
        """
        Draw a stone at the specified row and column, of the specified color.
        :param row: a number representing the row
        :param col: a number representing the column
        :param color: a tuple representing the color of the stone
        """
        x = col * self.__SIZE_GRID + self.__SIZE_MARGIN
        y = row * self.__SIZE_GRID + self.__SIZE_MARGIN

        # draw the outline
        pygame.draw.circle(self.__screen, (0, 0, 0), (x, y), self.__SIZE_STONE_RADIUS + 2, 0)
        # draw the stone
        pygame.draw.circle(self.__screen, color, (x, y), self.__SIZE_STONE_RADIUS, 0)

    def __draw_square_territory(self, row, col, color):
        """
        Draw a square at the specified row and column, of the specified color.
        :param row: a number representing the row
        :param col: a number representing the column
        :param color: a tuple representing the color of the square
        """
        center_x = col * self.__SIZE_GRID + self.__SIZE_MARGIN
        center_y = row * self.__SIZE_GRID + self.__SIZE_MARGIN

        length_square = self.__SIZE_STONE_RADIUS

        top_left_x = center_x - length_square / 2
        top_left_y = center_y - length_square / 2

        # Create a Rect object representing the square
        square_rect = pygame.Rect(top_left_x, top_left_y, length_square, length_square)

        # Draw the square on the screen
        pygame.draw.rect(self.__screen, color, square_rect, 2)

    def __closest_intersection_point(self, x, y):
        """
        Find the closest intersection points to the given coordinates.
        :param x: the x coordinate
        :param y: the y coordinate
        :return: a tuple containing the row and column of the closest intersection point
        """
        closest_row = round((y - self.__SIZE_MARGIN) / self.__SIZE_GRID)
        closest_col = round((x - self.__SIZE_MARGIN) / self.__SIZE_GRID)
        return closest_row, closest_col
