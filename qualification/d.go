package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"os"
)

func main() {
	var n, m int
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	fmt.Sscan(scanner.Text(), &n, &m)
	fmt.Print("{\"offers\": [")
	written := 0
	for i := 0; i < n; i++ { 
		scanner.Scan()
		var line string = scanner.Text()
		var offers map[string][]struct{
			ID string `json:"offer_id"`
			Sku int `json:"market_sku"`
			Price int `json:"price"`
		}
		if err := json.Unmarshal([]byte(line), &offers); err != nil { panic(err) }
		skus := make(map[int]bool, 0)
		for _, offer := range offers["offers"] {
			if _, ok := skus[offer.Sku]; !ok {
				skus[offer.Sku] = true
				if written > 0 { fmt.Print(",") }
				offerJSON, _ := json.Marshal(offer);
				fmt.Print(string(offerJSON))
				written++
				if written == m { goto outer }
			}
		}
	}
	outer: fmt.Println("]}")
}