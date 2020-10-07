package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

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
	scanner := bufio.NewScanner(bufio.NewReader(os.Stdin))
	scanner.Split(bufio.ScanWords)
	n := nextInt(scanner)
	clusters := make([]map[int]bool, 0)
	for i := 0; i < n; i++ {
		a := nextInt(scanner)
		b := nextInt(scanner)
		found := false
		for _, cluster := range clusters {
			if _, ok := cluster[a]; ok {
				found = true
			}
			if _, ok := cluster[b]; ok {
				found = true
			}
			if found {
				cluster[a] = true
				cluster[b] = true
			}
		}
		if !found {
			clusters = append(clusters, map[int]bool{a: true, b: true})
		}
	}
	q := nextInt(scanner)
	for i := 0; i < q; i++ {
		result := []int{}
		x := nextInt(scanner)
		found := false
		var cluster map[int]bool
		for _, c := range clusters {
			if _, ok := c[x]; ok {
				cluster = c
				found = true
			}
		}
		k := nextInt(scanner)
		for j := 0; j < k; j++ {
			y := nextInt(scanner)
			if found { 
				if _, ok := cluster[y]; ok {
					result = append(result, y)
				}
			}
		}
		fmt.Print(len(result))
		for _, server := range result {
			fmt.Print(" ", server)
		}
		fmt.Println()
	}
}