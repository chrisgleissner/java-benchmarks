# JMH Benchmarks

All benchmarks were performed using JMH. To reproduce:
```
mvn clean install
java -jar target/benchmarks.jar
```

To reproduce a single benchmark, e.g. SetterBenchmark:
```
mvn clean install
java -jar target/benchmarks.jar SetterBenchmark
```


All measurements below were obtained on OpenJDK 11.0.1 64Bit and Ubuntu 18.04 run inside VirtualBox 5.2 on an Intel I7-6700K clocked at 4.6GHz.
All 4 cores were allocated to the VM.


## Counters

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

## Getters and Setters

Direct vs [LambdaMetaFactory](https://docs.oracle.com/javase/8/docs/api/java/lang/invoke/LambdaMetafactory.html) vs Reflection

### GetterBenchmark

```
Benchmark                          Mode  Cnt  Score   Error  Units
GetterBenchmark.direct             avgt   10  4.218 ± 0.064  ns/op
GetterBenchmark.lambdaMetaFactory  avgt   10  4.161 ± 0.031  ns/op
GetterBenchmark.reflection         avgt   10  6.785 ± 0.125  ns/op
```

### SetterBenchmark

```
Benchmark                          Mode  Cnt  Score   Error  Units
SetterBenchmark.direct             avgt   10  4.863 ± 0.072  ns/op
SetterBenchmark.lambdaMetaFactory  avgt   10  4.831 ± 0.027  ns/op
SetterBenchmark.reflection         avgt   10  7.782 ± 0.085  ns/op
```
