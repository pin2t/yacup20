package main

import (
	"fmt"
	"math/rand"
)

// generates a lot of random numbers for c.go or c.java solution
func main() {
	fmt.Println("1000 1000000")
	for i := 0; i < 1000000; i++ {
		if i > 0 {
			fmt.Print(" ")
		}
		fmt.Print(rand.Int() % 1000)
	}
	fmt.Println()
}