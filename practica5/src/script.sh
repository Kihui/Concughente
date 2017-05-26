#!/bin/bash
i=2
j=1
hilos=$1
echo "" > exec.txt
echo "" > speedup.txt
t1=0
while [ $j -le 10 ]; do
    t=`java Main 256 256 256 1`
    t1=$((t1+t))
    #echo $s
    echo $t >> exec.txt
    let j=j+1
done
while [ $i -le $hilos ]; do
    let j=1
    let s=0
    while [ $j -le 10 ]; do
	t=`java Main 256 256 256 $i`
        s=$((s+t))
	#echo $s
	echo $t >> exec.txt
        let j=j+1
    done
    let prom=$((t1/(s/10)))
    let i=i+1
    echo $prom >> speedup.txt
done
