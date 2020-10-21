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

func newInterval(typ string, start time.Time) interval {
	var end time.Time
	switch (typ) {
	case "WEEK": 
		var weekday int = int(start.Weekday())
		if weekday == 0 { weekday = 7 } // reenumerate days (1 - Monday .. 7 - Sunday)
		end = start.AddDate(0, 0, 7 - weekday)
	case "MONTH": 
		year, month, _ := start.Date()
		end = time.Date(year, month + 1, 0, 0, 0, 0, 0, time.UTC)
	case "QUARTER": 
		lastmonth := []int{3, 3, 3, 6, 6, 6, 9, 9, 9, 12, 12 ,12}
		year, month, _ := start.Date()
		end = time.Date(year, 
			time.Month(lastmonth[int(month) - 1] + 1), 0, 0, 0, 0, 0, time.UTC)
	case "YEAR":
		year, _, _ := start.Date()
		end = time.Date(year + 1, 1, 0, 0, 0, 0, 0, time.UTC)
	case "LAST_SUNDAY_OF_YEAR":
		year, _, _ := start.Date()
		end = time.Date(year + 1, 1, 0, 0, 0, 0, 0, time.UTC)
		for end.Weekday() != time.Sunday { end = end.AddDate(0, 0, -1) }
		end = end.AddDate(0, 0, -1)
		if end.Before(start) {
			end = time.Date(year + 2, 1, 0, 0, 0, 0, 0, time.UTC)
			for end.Weekday() != time.Sunday { end = end.AddDate(0, 0, -1) }
			end = end.AddDate(0, 0, -1)
		}
	}
	return interval{typ, start, end}
}

func (i interval) next() interval {
	return newInterval(i.typ, i.end.AddDate(0, 0, 1))
}

func (i interval) include(t time.Time) bool {
	return (i.start.Equal(t) || i.start.Before(t)) && 
		(i.end.Equal(t) || i.end.After(t))
}

const dateFormat = "2006-01-02"

func main() {
	scanner := bufio.NewScanner(bufio.NewReader(os.Stdin))
	if !scanner.Scan() { panic("read error") }
	typ := scanner.Text()
	if !scanner.Scan() { panic("read error") }
	var start, end time.Time
	var err error
	line := scanner.Text()
	times := strings.Split(line, " ")
	if start, err = time.Parse(dateFormat, times[0]); err != nil { panic(err) }
	if end, err = time.Parse(dateFormat, times[1]); err != nil { panic(err) }
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
		fmt.Println(i.start.Format(dateFormat), i.end.Format(dateFormat))
	}
}