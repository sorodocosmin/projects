from game import Game
from parse_arguments import parse_arguments
from interface import App


def get_arguments(args):
    """
    :param args:
    :return: a tuple of the arguments of the app in this order
    size, play_with_computer
    """
    play_with_computer = True if args.to == 'c' else False
    if args.s != 9 and args.s != 13 and args.s != 19:
        print(f"!Warning : Usually the board of the go is of size 9, 13 or 19 (not {args.s})")
    return args.s, play_with_computer


def main():
    args = parse_arguments()
    size, play_with_computer = get_arguments(args)
    game = Game(board_size=size, play_with_computer=play_with_computer)
    App(game).run()


if __name__ == '__main__':
    main()
