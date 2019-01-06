# JMH Benchmarks

All benchmarks were performed using JMH. To reproduce:
```
mvn clean install
java -jar target/benchmarks.jar
```

All measurements below were obtained on OpenJDK 11 and Ubuntu 18.04 run inside VirtualBox 5.2 on an Intel I7-6700K clocked at 4.6GHz.
All 4 cores were allocated to the VM.

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

## GetterBenchmark

Direct vs [LambdaMetaFactory](https://docs.oracle.com/javase/8/docs/api/java/lang/invoke/LambdaMetafactory.html) vs Reflection

```
Benchmark                          Mode  Cnt   Score   Error  Units
GetterBenchmark.direct             avgt   10   4.875 ± 1.110  ns/op
GetterBenchmark.lambdaMetaFactory  avgt   10  22.908 ± 2.796  ns/op
GetterBenchmark.reflection         avgt   10  22.168 ± 1.767  ns/op
```