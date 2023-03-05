

#### Check if any duplicate postgresql process is running

```
$ lsof -n -i:5432 | grep LISTEN
```