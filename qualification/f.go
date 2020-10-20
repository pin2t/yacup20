package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
	"sort"
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
	if !scanner.Scan() {
		panic("error reading URL")
	}
	url := scanner.Text()
	port := nextInt(scanner)
	a := nextInt(scanner)
	b := nextInt(scanner)
	var err error
	var response *http.Response
	if response, err = http.Get(fmt.Sprintf("%s:%d?a=%d&b=%d", url, port, a,b)); err != nil {
		panic(err)
	}
	var body []byte
	if body, err = ioutil.ReadAll(response.Body); err != nil {
		panic(err)
	}
	var arr []int
	if err = json.Unmarshal(body, &arr); err != nil {
		panic(err)
	}
	sort.Sort(sort.Reverse(sort.IntSlice(arr)))
	for _, i := range arr { if i > 0 { fmt.Println(i) } }
}