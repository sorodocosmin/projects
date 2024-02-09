import matplotlib.pyplot as plt
import pandas as pd
import sys
import copy
from drawing import draw_line_and_perpendicular_bisector
from parse_arguments import parse_arguments


"""
run the program with the following command for a demo:
python main.py -csv1 points.csv -csv2 centroids.csv
(Ex 41, page 904, Exercitii de invatare automata)
"""


def get_data(file):
    try:
        df = pd.read_csv(file)
        x_coords = df['x'].values.astype(float)
        y_coords = df['y'].values.astype(float)
    except Exception as e:
        print(f"Error : {e}")
        sys.exit(1)

    return x_coords, y_coords


def get_arguments(args):

    function_d = euclidian_distance
    if args.d == "m":
        function_d = manhattan_distance
    elif args.d == "c":
        function_d = chebyshev_distance

    draw_p_b = True
    if args.pb == "n":
        draw_p_b = False

    if args.d != "e" and args.pb == "y":
        print("Warning : the perpendicular bisectors will not help you if the distance is not euclidian")

    return args.csv1, args.csv2, function_d, draw_p_b, args.n


def euclidian_distance(x1, y1, x2, y2):
    return ((x1 - x2)**2 + (y1 - y2)**2)**0.5


def manhattan_distance(x1, y1, x2, y2):
    return abs(x1 - x2) + abs(y1 - y2)


def chebyshev_distance(x1, y1, x2, y2):
    return max(abs(x1 - x2), abs(y1 - y2))


def plot_points(clusters, centroids):
    """
    plot the points from each cluster, with a different color for each cluster
    :param centroids:
    :param clusters:
    """
    for cluster, points in clusters.items():
        x_coords = [point[0] for point in points]
        y_coords = [point[1] for point in points]
        plt.scatter(x_coords, y_coords, marker='o', c=f'C{cluster}')
        plt.scatter(centroids[cluster][0], centroids[cluster][1], marker='s', c=f'C{cluster}', edgecolors='red')


def plot_perpendicular_bisectors(centroids, min_x, max_x, min_y, max_y):
    """
    plot the perpendicular bisectors for each pair of centroids
    :param centroids:
    :return:
    """
    nr_centroids = len(centroids)
    for i in range(nr_centroids):
        for j in range(i+1, nr_centroids):
            draw_line_and_perpendicular_bisector(centroids[i], centroids[j], min_x, max_x, min_y, max_y)


def main():
    # check line arguments
    args = parse_arguments()
    path_points, path_centroids, function_d, draw_p_b, max_nr_iterations = get_arguments(args)
    compute_clusters(path_points, path_centroids, function_d, draw_p_b, max_nr_iterations)


def compute_clusters(path_points, path_centroids, distance_function, draw_p_b, max_nr_iterations):
    x_coords, y_coords = get_data(path_points)
    x_centroids, y_centroids = get_data(path_centroids)
    min_x_coord = min(x_coords) - 0.1
    max_x_coord = max(x_coords) + 0.1
    min_y_coord = min(y_coords) - 0.1
    max_y_coord = max(y_coords) + 0.1
    clusters = {}
    for i in range(len(x_centroids)):
        clusters[i] = set()

    points = [(x_coord, y_coord) for x_coord, y_coord in zip(x_coords, y_coords)]
    centroid_points = [(x_centroid, y_centroid) for x_centroid, y_centroid in zip(x_centroids, y_centroids)]
    previous_cluster = None
    iterations = 0

    while (iterations == 0 or previous_cluster != clusters) and iterations < max_nr_iterations:
        previous_cluster = copy.deepcopy(clusters)
        for point in points:
            # compute the distance between the point and cluster centroids
            # add the point to the cluster with the smallest distance
            min_distance = None
            closest_centroid = None
            for i, centroid in enumerate(centroid_points):
                if min_distance is None:
                    min_distance = distance_function(point[0], point[1], centroid[0], centroid[1])
                    closest_centroid = i
                elif min_distance > distance_function(point[0], point[1], centroid[0], centroid[1]):
                    min_distance = distance_function(point[0], point[1], centroid[0], centroid[1])
                    closest_centroid = i

            if point in clusters[closest_centroid]:
                continue
            else:
                # remove the point from the previous cluster
                for cluster, points_in_cluster in clusters.items():
                    if point in points_in_cluster:
                        clusters[cluster].remove(point)
                        break
                # add the point to the closest cluster
                clusters[closest_centroid].add(point)

        # make the adjustements such that the plot is ok
        plt.figure(iterations+1)
        plt.gca().set_aspect('equal', adjustable='box')
        plt.xlim(min_x_coord, max_x_coord)
        plt.ylim(min_y_coord, max_y_coord)
        plot_points(clusters, centroid_points)
        if draw_p_b:
            plot_perpendicular_bisectors(centroid_points, min_x_coord, max_x_coord, min_y_coord, max_y_coord)
        plt.title(f'Iteration {iterations+1}')

        update_centroids(clusters, centroid_points)
        iterations += 1

    plt.show()
    print(f"After {iterations} iterations, the clusters are:")
    for cluster, points in clusters.items():
        print(f"Cluster {cluster} : {points}")


def update_centroids(clusters, centroid_points):
    """
    update the centroids based on the points in the cluster
    :param clusters:
    :param centroid_points:
    :return:
    """
    for cluster, points in clusters.items():
        if len(points) == 0:
            continue
        x_coords = [point[0] for point in points]
        y_coords = [point[1] for point in points]
        centroid_points[cluster] = (sum(x_coords) / len(x_coords), sum(y_coords) / len(y_coords))


if __name__ == '__main__':
    main()
