import argparse


def positive_int(value):
    try:
        int_value = int(value)
    except ValueError:
        raise argparse.ArgumentTypeError(f"{value} is not an integer.")

    if int_value <= 0:
        raise argparse.ArgumentTypeError(f"{value} is not a positive integer.")

    return int_value


def parse_arguments():
    description = """
    K-means clustering on 2D points.
    
    Available arguments for this app:
        Necessary:
        1. "-csv1 <options-1>"
        2. "-csv2 <options-2>"
        Optional:
        3. "-d <options-3>" which comes from distance
        4. "-pb <options-4>" which comes from perpendicular bisector
        5. "-n <options-5>" which comes from number of iterations

    <options-1>: the path to the csv file containing the points, it needs to be a csv file with the following format:
    x,y
    1,2
    3,4
    5,6
    ...

    <options-2>: the path to the csv file containing the centroids, it needs to be a csv file respecting the above format

    <options-3>: the distance function to be used, it can be one of the following:
        1. "e" - euclidian distance
        2. "m" - manhattan distance
        3. "c" - chebyshev distance

    <options-4>: whether to draw the perpendicular bisectors or not, it can be one of the following:
        1. "y" - yes
        2. "n" - no
    
    <options-5>: the maximum number of iterations, it needs to be a positive integer

    If no optional arguments are given, the default values are:
        3. "-d=e"
        4. "-pb=y"
        5. "-n=10"

    Note1 : for other distances, apart from euclidian, it does not help you if the perpendicular bisectors are drawn 
    Note2 : it will not work for points that are not 2D
    Note3 : in the csv files, on the first line, the column names should be (lowercase) "x" and "y" (not "x1" and "y1" or something else)
    Note4 : the algorithm will create a new window for each iteration
    Note5 : the algorithm will stop when 2 consecutive iterations have the same points in the clusters
    Note6 : the algorithm will stop when the maximum number of iterations is reached or when it converges
    Note7 : as an unit from the X axis is the same as an unit from the Y axis (such that the perpendicular bisectors are drawn correctly), when the points are too far from each other, the plots will not look good, in this case you can [try to zoom in or zoom out (kinda difficult)] only check what are the points in the final clusters (see Note8)
    Note8 : the final clusters will be printed in the console
    """

    examples_running = """
    python main.py -csv1="data.csv" -csv2="centroids.csv"
    python main.py -csv1="data.csv" -csv2="centroids.csv" -d=m -pb=n
    python main.py -csv1 path/to/points.csv -csv2="path/to/centroids.csv" -d=m -pb=n
    python main.py -csv1="data.csv" -csv2 centroids.csv -n=20
    """

    parser = argparse.ArgumentParser(description=description,
                                     epilog=examples_running,
                                     formatter_class=argparse.RawDescriptionHelpFormatter)

    # Necessary arguments
    parser.add_argument("-csv1", required=True,
                        metavar="<path/to/file_points.csv>",
                        help="Path to the CSV file containing 2D points.")
    parser.add_argument("-csv2", required=True,
                        metavar="<path/to/file_centroids.csv>",
                        help="Path to the CSV file containing centroids.")

    # Optional arguments
    parser.add_argument("-d", choices=["e", "m", "c"], default="e",
                        help="Distance function (default: euclidean). \"e\" stands for euclidean, \"m\" for manhattan, \"c\" for chebyshev.")
    parser.add_argument("-pb", choices=["y", "n"], default="y", help="Draw perpendicular bisectors (default: yes).")
    parser.add_argument("-n", type=positive_int, default=10,
                        help="A positive integer representing the maximum number of iterations (default: 10).")

    return parser.parse_args()
