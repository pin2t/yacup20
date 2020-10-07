package main

import (
	"fmt"
	"math/rand"
	"time"
)

func main() {
	fmt.Println("100000 1000000000 1000000000")
	rand.Seed(time.Now().UnixNano())
	for i := 0; i < 100000; i++ {
		if i > 0 {
			fmt.Print(" ")
		}
		fmt.Print(rand.Uint64() % 1000000000)
	}
	fmt.Println()
}