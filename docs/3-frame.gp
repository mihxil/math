set terminal pngcairo size 600,600
set xrange[0:10]
set yrange[0:10]
set zrange[0:10]
set view equal xyz
set xyplane at 0
set style line 1 \
    linecolor rgb '#0d60ad' \
    linetype 1 linewidth 1 \
    pointtype 7 pointsize 1
splot "/dev/stdin" using 2:3:4 with linespoints  linestyle 1 notitle
