# output as png image
set terminal png

# save file to "benchmark.png"
set output "benchmark.png"

# graph title
set title "AnonFS TOR Latency Report"

#nicer aspect ratio for image size
set size 1,1

# y-axis grid
set grid y

#x-axis label
set xlabel "request"

#y-axis label
set ylabel "response time (ms)"

#plot data from "out.data" using column 9 with smooth sbezier lines
plot "out.data" using 9 smooth sbezier with lines title "Number of requests VS Response time"
