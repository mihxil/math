set terminal pngcairo size 600,600
set xrange[0:31]
set yrange[0:31]
plot "/dev/stdin" using 2:3 with linespoints title ""
