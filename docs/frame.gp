set terminal pngcairo size 600,600
set xrange[0:33]
set yrange[0:33]
set style line 1 \
    linecolor rgb '#0d60ad' \
    linetype 1 linewidth 1 \
    pointtype 7 pointsize 1
plot "/dev/stdin" using 2:3 with linespoints  linestyle 1 notitle
