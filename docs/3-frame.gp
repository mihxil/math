set terminal pngcairo size 600,600
set xrange[0:10]
set yrange[0:10]
set zrange[0:10]
set view equal xyz
set xyplane at -0.5
set style line 1 \
    linecolor rgb '#bebeee' \
    linetype 1 linewidth 1 \
    pointtype 7 pointsize 1
set multiplot
splot $data using 2:3:4 with lines  linestyle 1 notitle
splot $data using 2:3:4 with points pointtype 7 notitle
