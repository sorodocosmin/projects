# 📐 K-means Clustering on 2D Points
## Description

The application clusters 2D points using the K-means algorithm. It offers flexibility in specifying distance functions, visualization options, and the maximum number of iterations.\
## **You can see a short demo** [**here**](https://www.youtube.com/watch?v=Vth2lBu7cVw).

Screenshot took from the demo:
![image](https://github.com/sorodocosmin/projects/assets/61987774/6efed1b4-b223-4de8-9a9d-7d74d5327f26)


## Usage

```bash
python main.py -csv1 <path/to/file_points.csv> -csv2 <path/to/file_centroids.csv> [-d {e,m,c}] [-pb {y,n}] [-n N]
```

## Arguments
1. ``csv1 <path/to/file_points.csv>``: Path to the CSV file containing 2D points.
2. ``csv2 <path/to/file_centroids.csv>``: Path to the CSV file containing centroids.
> 3. ``d {e,m,c}``: Distance function (default: euclidean). "e" for euclidean, "m" for manhattan, "c" for chebyshev. (Optional)
> 4. ``pb {y,n}``: Draw perpendicular bisectors (default: yes). (Optional)
> 5. ``n N``: Maximum number of iterations (default: 10). (Optional)

## Examples
```bash
python main.py -csv1="data.csv" -csv2="centroids.csv"
```
```bash
python main.py -csv1="data.csv" -csv2="centroids.csv" -d=m -pb=n
```
```bash
python main.py -csv1 path/to/points.csv -csv2="path/to/centroids.csv" -d=m -pb=n
```
```bash
python main.py -csv1="data.csv" -csv2 centroids.csv -n=20
```

## Notes
- For distances other than euclidean, perpendicular bisectors are not helpful.
- The algorithm only works for 2D points.
- in the ``.csv`` files, on the first line, the column names should be (lowercase) ``x`` and ``y`` (not ``x1`` and ``y1`` or anything else).
- the algorithm will create a new window for each iteration.
- the algorithm will stop when 2 consecutive iterations have the same points in the clusters (it converges).
- the algorithm will stop when the maximum number of iterations is reached **or** when it converges.
- as an unit from the **X** axis is the same as an unit from the **Y** axis (such that the perpendicular bisectors are drawn correctly), when
 the points are too far from each other, the plots will not look good, in this case you can [try to zoom in or zoom out (kinda difficult)] only check what are the points in the final clusters (see next note)
- the final clusters will be printed in the console

