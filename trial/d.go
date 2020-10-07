package main

import "fmt"

func nextInt() int {
	var val int
	if _, err := fmt.Scan(&val); err != nil {
		panic("error reading input")
	}
	return val
}

func main() {
	n := nextInt()
	l := 1
	r := n
	for l <= r {
		m := (l + r) / 2
		fmt.Println(m)
		answer := nextInt()
		if answer == 1 {
			l = m + 1
		} else {
			r = m - 1
		}
	}
	fmt.Println("!", l)
}