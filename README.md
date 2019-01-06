# JMH Benchmarks

All benchmarks were performed using JMH. To reproduce:
```
mvn clean install
java -jar target/benchmarks.jar
```

### Counters

AtomicInteger vs [MutableInt](https://commons.apache.org/proper/commons-lang/javadocs/api-release/index.html) vs int

```
Benchmark                       Mode  Cnt  Score   Error  Units
CounterBenchmark.atomicInteger  avgt   10  5.541 ± 0.032  ns/op
CounterBenchmark.atomicLong     avgt   10  5.514 ± 0.053  ns/op

CounterBenchmark.mutableInt     avgt   10  4.214 ± 0.064  ns/op
CounterBenchmark.mutableLong    avgt   10  4.030 ± 0.033  ns/op

CounterBenchmark.primitiveInt   avgt   10  4.169 ± 0.061  ns/op
CounterBenchmark.primitiveLong  avgt   10  4.147 ± 0.050  ns/op
```
