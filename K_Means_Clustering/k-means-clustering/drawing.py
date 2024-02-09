import matplotlib.pyplot as plt
import numpy as np

"""
Firstly, let's remind some formulas:
    - the equation of a line is y = mx + b, where m is the slope
    - having 2 points A(x1, y1) and B(x2, y2), the slope is m = (y2 - y1) / (x2 - x1)
    - assuming the slope of AB is m --> a line perpendicular on it will have slope -1/m
    - so, the equation drawn from the middle of AB (let's call it point M which will have the coordinates 
    ((x1+x2)/2, (y1+y2)/2) --> y = -1/m * (x - xM) + yM
"""


def draw_line_and_perpendicular_bisector(point1, point2, min_x_coord, max_x_coord, min_y_coord, max_y_coord):

    x1, y1 = point1
    x2, y2 = point2

    midpoint = ((x1 + x2) / 2, (y1 + y2) / 2)

    if x2 == x1:
        # the perpendicular bisector is a vertical line
        x_vals = np.linspace(min_x_coord, max_x_coord, 100)
        y_vals = np.full(100, midpoint[1])
    else:

        slope = (y2 - y1) / (x2 - x1)

        if slope == 0:
            # the perpendicular bisector is a horizontal line
            x_vals = np.full(100, midpoint[0])
            y_vals = np.linspace(min_y_coord, max_y_coord, 100)
        else:
            perpendicular_slope = -1 / slope

            x_vals = np.linspace(min_x_coord, max_x_coord, 100)
            y_vals = perpendicular_slope * (x_vals - midpoint[0]) + midpoint[1]

    plt.plot([x1, x2], [y1, y2], linestyle='--', color='gray')
    plt.plot(x_vals, y_vals, linestyle='-', color='red')
