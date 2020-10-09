package main

import (
	"bufio"
	"flag"
	"fmt"
	"io"
	"log"
	"os"
	"runtime/pprof"
	"strconv"
)

var cpuprofile = flag.String("cpuprofile", "", "write cpu profile to file")

type scanner struct {
	*bufio.Scanner
}

func newScanner(reader io.Reader) *scanner {
	return &scanner{bufio.NewScanner(bufio.NewReader(reader))}
}

func (s *scanner) readInt() int {
	if s.Scan() {
		r, err := strconv.Atoi(s.Text())
		if err != nil {
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
	scanner := newScanner(os.Stdin)
	scanner.Split(bufio.ScanWords)
	k := scanner.readInt()
	n := scanner.readInt()
	petya := 0
	vasya := 0
	win := ""
	for i := 0; i < n; i++ {
		val := scanner.readInt()
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