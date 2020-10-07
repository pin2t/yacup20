package main

import (
	"fmt"
	"strings"
)

func main() {
	var j string
	var s string

	fmt.Scan(&j, &s)

	result := 0
	for _, ch := range s {
		if strings.Contains(j, string(ch)) {
			result++
		}
	}

	fmt.Println(result)
}