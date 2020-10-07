package main

import (
	"bufio"
	"flag"
	"fmt"
	"log"
	"os"
	"runtime/pprof"
	"strconv"
)

func remove(slice []uint64, s int) []uint64 {
    return append(slice[:s], slice[s+1:]...)
}

func max(a, b uint64) uint64 {
	if a > b { return a } 
	return b
}

func ringAlarms(times []uint64, t, x uint64) (rings uint64) {
	rings = 0
	for i := 0; i < len(times); i++ { 
		if times[i] <= t {
			rings += max((t - times[i]) / x, 0) + 1 
		}
	}
	return rings
}

var cpuprofile = flag.String("cpuprofile", "", "write cpu profile to file")

func nextInt(s *bufio.Scanner) uint64 {
	if s.Scan() {
		var r int
		var err error
		if r, err = strconv.Atoi(s.Text()); err != nil {
			panic(err)
		}
		return uint64(r)
	}
	panic("EOF")
}

func main() {
    flag.Parse()
    if *cpuprofile != "" {
        f, err := os.Create(*cpuprofile)
        if err != nil {
            log.Fatal(err)
        }
        pprof.StartCPUProfile(f)
        defer pprof.StopCPUProfile()
    }	
	var n int
	var x, k uint64
	scanner := bufio.NewScanner(bufio.NewReader(os.Stdin))
	scanner.Split(bufio.ScanWords)
	n = int(nextInt(scanner))
	x = nextInt(scanner)
	k = nextInt(scanner)
	times := make([]uint64, n)
	for i := 0; i < n; i++ { times[i] = nextInt(scanner) }
	dedup := make(map[uint64]uint64)
	for i := 0; i < len(times); i++ {
		bucket := times[i] % x 
		val, ok := dedup[bucket]
		if ok {
			if times[i] < val { dedup[bucket] = times[i] }
		} else { dedup[bucket] = times[i] }
	}
	times = make([]uint64, n)
	nn := 0
	for _, ring := range dedup {
		times[nn] = ring; nn++
	}
	times = times[:nn]
	r := uint64(1000000000) * uint64(1000000000)
	l := uint64(0)
	for l <= r {
		m := (l + r) / 2
		if ringAlarms(times, m, x) < k {
			l = m + 1
		} else {
			r = m - 1
		}
	}
	if ringAlarms(times, l, x) == k {
		fmt.Println(l)
	} else if ringAlarms(times, r + 1, x) == k {
		fmt.Println(r + 1)
	} else { fmt.Println(r) } 
}