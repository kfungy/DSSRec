# Recommender

This application outputs recommendations of similar videos given a CSV input

### How to run:
There are two input args to the program:

args(0) = The path of the input csv file. For ex. /tmp/user_video_watches_100k.csv

args(1) = The destination path to output the results to. For ex. /tmp/results.csv

You are free to point to your own input csv file for args(0)

Example:
```sh
$ cd [project root directory]
$ sbt "run src/main/resources/user_video_watches_10k.csv src/test/resources/test2.csv"
```

#### Tests
Testing tool: scalatest

```sh
$ cd [project root directory]
$ sbt clean compile test
```
Code Coverage: sbt-scoverage

```sh
$ cd [project root directory]
$ sbt clean compile test
$ sbt coverageReport
```

### Build
```sh
$ cd [project root directory]
$ sbt assembly
```

### Docker
For convenience the sample csv input files are packed into the project's "src/main/resources/" folder

```sh
$ cd [project root directory]
$ docker build -t dssrec:0.0.1 .
$ docker run -t dssrec:0.0.1 sbt "run src/main/resources/user_video_watches_10k.csv /tmp/test10k.csv"
$ docker run -t dssrec:0.0.1 sbt "run src/main/resources/user_video_watches_100k.csv /tmp/test100k.csv"
$ docker run -t dssrec:0.0.1 sbt "run src/main/resources/user_video_watches_1M.csv /tmp/test1M.csv"
```

### To dos
more testing

further optimize prediction performance

open to suggestions :)
