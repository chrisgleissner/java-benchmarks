# JMH Benchmarks

All benchmarks were performed using JMH. To reproduce:
```
mvn clean install
java -jar target/benchmarks.jar
```

### Counters

```
Benchmark                             Mode  Cnt          Score         Error  Units
CounterBenchmark.countAtomicInteger  thrpt   10  179949825.404 ± 2091319.654  ops/s
CounterBenchmark.countInt            thrpt   10  242001288.865 ± 2284995.074  ops/s
CounterBenchmark.countMutableInt     thrpt   10  236877297.518 ± 3218770.423  ops/s
```
