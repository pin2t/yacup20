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

var cpuprofile = flag.String("cpuprofile", "", "write cpu profile to file")

func nextInt(s *bufio.Scanner) int {
	if s.Scan() {
		var r int
		var err error
		if r, err = strconv.Atoi(s.Text()); err != nil {
			panic(err)
		}
		return r
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
	scanner := bufio.NewScanner(bufio.NewReader(os.Stdin))
	scanner.Split(bufio.ScanWords)
	k := nextInt(scanner)
	n := nextInt(scanner)
	petya := 0
	vasya := 0
	win := ""
	for i := 0; i < n; i++ {
		val := nextInt(scanner)
		if val % 15 != 0 {
			if val % 3 == 0 {
				petya++
			} else if val % 5 == 0 {
				vasya++
			}
		}
		if (petya == k && len(win) == 0) {
			win = "Petya"
			break
		}
		if (vasya == k && len(win) == 0) {
			win = "Vasya"
			break
		}
	}
	if (win == "") {
		if (vasya == petya) {
			win = "Draw"
		} else if (vasya > petya) {
			win = "Vasya"
		} else {
			win = "Petya"
		}
	}
	fmt.Println(win)
}