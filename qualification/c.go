package main

import (
	"encoding/json"
	"log"
	"net/http"
	"os"
	"strings"
)

func handleValidate(w http.ResponseWriter, r *http.Request) {
	number := r.URL.Query().Get("phone_number")
	if number != "" {
		if strings.HasPrefix(number, "+7") { number = "8" + number[2:] }
		number = strings.ReplaceAll(strings.Map(func (r rune) rune {
			if r >= '0' && r <= '9' { return r }
			return ' '
		}, number), " ", "")
		w.Header().Add("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		var rs = struct {
			Normalized string `json:"normalized,omitempty"`
			Status bool `json:"status"`
		}{"", false}
		if len(number) == 11 && (number[0:4] == "8912" || number[0:4] == "8982" || number[0:4] == "8986" || number[0:4] == "8934") {
			rs.Status = true
			rs.Normalized = "+7-" + number[1:4] + "-" + number[4:7] + "-" + number[7:]
		}
		var rsBytes []byte
		var err error
		if rsBytes, err = json.Marshal(rs); err != nil { panic(err) }
		if _, err = w.Write(rsBytes); err != nil { panic(err) }
	} else {
		w.WriteHeader(http.StatusBadRequest)
	}
}

func main() {
	http.HandleFunc("/ping", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
	})
	http.HandleFunc("/shutdown", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		os.Exit(0)
	})
	http.HandleFunc("/validatePhoneNumber", handleValidate)
	log.Fatal(http.ListenAndServe("127.0.0.1:7777", nil))
}