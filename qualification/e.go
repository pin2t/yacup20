package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

var important []bool
type row []string

func (r row) similar(other row) bool {
	if len(r) != len(other) { 
		panic("rows has different lengths") 
	}
	sim := false
	eq := true
	for i := 0; i < len(r); i++ {
		if r[i] != "" && r[i] == other[i] && 
			(!important[i] || r[i] != "" || other[i] != "") { 
				sim = true 
		}
		if r[i] != "" && other[i] != "" && !important[i] && r[i] != other[i] { 
			eq = false 
		}
	}
	return sim && eq;
}

// assumes scanner scan lines
func readRow(scanner *bufio.Scanner, k int) row {
	if !scanner.Scan() { panic("missing imput") }
	var result row = make([]string, k)
	i := 0
	builder := &strings.Builder{}
	for _, c := range scanner.Text() {
		if c == '\t' {
			result[i] = builder.String()
			builder.Reset() 
			i++ 
			if i >= k { panic("too much columns in input") }
		} else {
			builder.WriteRune(c)
		}
	}
	return result
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	if !scanner.Scan() { 
		panic("missing imput") 
	}
	var n, k int
	if _, err := fmt.Sscan(scanner.Text(), &n, &k); err != nil { 
		panic(err) 
	}
	important = make([]bool, k)
	for i := 0; i < k; i++ { 
		important[i] = false 
	}
	if !scanner.Scan() { 
		panic("missing imput") 
	}
	impScanner := bufio.NewScanner(strings.NewReader(scanner.Text()))
	impScanner.Split(bufio.ScanWords)
	impScanner.Scan()
	for impScanner.Scan() {
		idx, _ := strconv.Atoi(impScanner.Text())
		important[idx - 1] = true
	}
	rows := make([]row, n)
	for i := 0; i < n; i++ { 
		rows[i] = readRow(scanner, k) 
	}
	similar := 0
	for i := 0; i < n - 1; i++ {
		for j := i + 1; j < n; j++ {
			if rows[i].similar(rows[j]) { 
				similar++ 
			}
		}
	}
	fmt.Println(similar)
}