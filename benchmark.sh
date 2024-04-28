#!/bin/bash

# Default values for JMH options
default_includes=".*"
default_operations_per_invocation=10
default_time_on_iteration='1s'
default_jmh_timeout='10m'
default_warmup='1s'
default_warmup_batch_size=10
default_warmup_iterations=5
default_iterations=10
default_batch_size=1
default_fork=1
default_fail_on_error=false
default_force_gc=false

# Function to display usage information
show_usage() {
    echo "Usage: $0 [OPTIONS]"
    echo "Options:"
    echo "  -i, --includes <REGEX>                Regular expression pattern of benchmark(s). Default is '$default_includes'."
    echo "  -o, --operations-per-invocation <INT> Operations per invocation. Default is $default_operations_per_invocation."
    echo "  -t, --time-on-iteration <TIME>        Time per measurement iteration. Default is '$default_time_on_iteration'."
    echo "  -j, --jmh-timeout <TIME>              Timeout for benchmark iteration. Default is '$default_jmh_timeout'."
    echo "  -w, --warmup <TIME>                   Warmup time per iteration. Default is '$default_warmup'."
    echo "  -b, --warmup-batch-size <INT>         Warmup batch size. Default is $default_warmup_batch_size."
    echo "  -wi, --warmup-iterations <INT>        Number of warmup iterations. Default is $default_warmup_iterations."
    echo "  -it, --iterations <INT>               Number of measurement iterations. Default is $default_iterations."
    echo "  -bch, --batch-size <INT>              Batch size. Default is $default_batch_size."
    echo "  -f, --fork <INT>                      Number of benchmark forks. Default is $default_fork."
    echo "  -fe, --fail-on-error <true/false>     Fail JMH on error? Default is $default_fail_on_error."
    echo "  -gc, --force-gc <true/false>          Force GC between iterations? Default is $default_force_gc."
    echo "  -h, --help                            Show this help message."
}

# Parse command line arguments
while [[ "$#" -gt 0 ]]; do
    case $1 in
        -i|--includes) includes="$2"; shift ;;
        -o|--operations-per-invocation) operations_per_invocation="$2"; shift ;;
        -t|--time-on-iteration) time_on_iteration="$2"; shift ;;
        -j|--jmh-timeout) jmh_timeout="$2"; shift ;;
        -w|--warmup) warmup="$2"; shift ;;
        -b|--warmup-batch-size) warmup_batch_size="$2"; shift ;;
        -wi|--warmup-iterations) warmup_iterations="$2"; shift ;;
        -it|--iterations) iterations="$2"; shift ;;
        -bch|--batch-size) batch_size="$2"; shift ;;
        -f|--fork) fork="$2"; shift ;;
        -fe|--fail-on-error) fail_on_error="$2"; shift ;;
        -gc|--force-gc) force_gc="$2"; shift ;;
        -h|--help) show_usage; exit 0 ;;
        *) echo "Unknown parameter passed: $1"; show_usage; exit 1 ;;
    esac
    shift
done

# Set default values if not provided
includes="${includes:-$default_includes}"
operations_per_invocation="${operations_per_invocation:-$default_operations_per_invocation}"
time_on_iteration="${time_on_iteration:-$default_time_on_iteration}"
jmh_timeout="${jmh_timeout:-$default_jmh_timeout}"
warmup="${warmup:-$default_warmup}"
warmup_batch_size="${warmup_batch_size:-$default_warmup_batch_size}"
warmup_iterations="${warmup_iterations:-$default_warmup_iterations}"
iterations="${iterations:-$default_iterations}"
batch_size="${batch_size:-$default_batch_size}"
fork="${fork:-$default_fork}"
fail_on_error="${fail_on_error:-$default_fail_on_error}"
force_gc="${force_gc:-$default_force_gc}"

# Run Gradle with JMH task

rm -Rf build/results/jmh

./gradlew jmh -PjmhIncludes="$includes" -PoperationsPerInvocation="$operations_per_invocation" \
    -PtimeOnIteration="$time_on_iteration" -PjmhTimeout="$jmh_timeout" -Pwarmup="$warmup" \
    -PwarmupBatchSize="$warmup_batch_size" -PwarmupIterations="$warmup_iterations" \
    -Piterations="$iterations" -PbatchSize="$batch_size" \
    -Pfork="$fork" -PfailOnError="$fail_on_error" -PforceGC="$force_gc" \

cp build/results/jmh/results.json jmh-result-all.json
