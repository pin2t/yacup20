package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
	"time"
)

type interval struct {
	typ string
	start, end time.Time
}

func newInterval(t string, s time.Time) interval {
	var end time.Time
	var wd int = int(s.Weekday())
	if wd == 0 { wd = 7 }
	switch (t) {
	case "WEEK": end = s.AddDate(0, 0, 7 - wd)
	}
	return interval{t, s, end}
}

func (i interval) next() interval {
	var newend, newstart time.Time
	newstart = i.end.AddDate(0, 0, 1)
	switch (i.typ) {
	case "WEEK": newend = i.end.AddDate(0, 0, 7)
	}
	return interval{i.typ, newstart, newend}
}

func (i interval) include(dt time.Time) bool {
	return (i.start.Equal(dt) || i.start.Before(dt)) && 
		(i.end.Equal(dt) || i.end.After(dt))
}

func main() {
	scanner := bufio.NewScanner(bufio.NewReader(os.Stdin))
	if !scanner.Scan() { panic("read error") }
	typ := scanner.Text()
	if !scanner.Scan() { panic("read error") }
	var start, end time.Time
	var err error
	line := scanner.Text()
	times := strings.Split(line, " ")
	if start, err = time.Parse("2006-01-02", times[0]); err != nil { panic(err) }
	if end, err = time.Parse("2006-01-02", times[1]); err != nil { panic(err) }
	intervals := make([]interval, 0)
	i := newInterval(typ, start)
	for !i.include(end) { 
		intervals = append(intervals, i)
		i = i.next() 
	}
	i.end = end
	intervals = append(intervals, i)
	fmt.Println(len(intervals))
	for _, i := range intervals { 
		fmt.Println(i.start.Format("2006-01-02"), i.end.Format("2006-01-02"))
	}
}