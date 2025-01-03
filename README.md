# Learn Flink: Hands-On Training
Goals and Scope of this Training #
This training presents an introduction to Apache Flink that includes just enough to get you started writing scalable streaming ETL, analytics, and event-driven applications, while leaving out a lot of (ultimately important) details. The focus is on providing straightforward introductions to Flink’s APIs for managing state and time, with the expectation that having mastered these fundamentals, you’ll be much better equipped to pick up the rest of what you need to know from the more detailed reference documentation. The links at the end of each section will lead you to where you can learn more.

Specifically, you will learn:

how to implement streaming data processing pipelines
how and why Flink manages state
how to use event time to consistently compute accurate analytics
how to build event-driven applications on continuous streams
how Flink is able to provide fault-tolerant, stateful stream processing with exactly-once semantics
This training focuses on four critical concepts: continuous processing of streaming data, event time, stateful stream processing, and state snapshots. This page introduces these concepts.

Note: Accompanying this training is a set of hands-on exercises that will guide you through learning how to work with the concepts being presented. A link to the relevant exercise is provided at the end of each section.
Stream Processing #
Streams are data’s natural habitat. Whether it is events from web servers, trades from a stock exchange, or sensor readings from a machine on a factory floor, data is created as part of a stream. But when you analyze data, you can either organize your processing around bounded or unbounded streams, and which of these paradigms you choose has profound consequences.

Bounded vs Unbounded
Batch processing is the paradigm at work when you process a bounded data stream. In this mode of operation you can choose to ingest the entire dataset before producing any results, which means that it is possible, for example, to sort the data, compute global statistics, or produce a final report that summarizes all of the input.

Stream processing, on the other hand, involves unbounded data streams. Conceptually, at least, the input may never end, and so you are forced to continuously process the data as it arrives.

In Flink, applications are composed of streaming dataflows that may be transformed by user-defined operators. These dataflows form directed graphs that start with one or more sources, and end in one or more sinks.
![image](https://github.com/user-attachments/assets/a9a8ea6f-8ac2-4766-a574-df043e0dcdaa)

Program Dataflow
Often there is a one-to-one correspondence between the transformations in the program and the operators in the dataflow. Sometimes, however, one transformation may consist of multiple operators.

An application may consume real-time data from streaming sources such as message queues or distributed logs, like Apache Kafka or Kinesis. But flink can also consume bounded, historic data from a variety of data sources. Similarly, the streams of results being produced by a Flink application can be sent to a wide variety of systems that can be connected as sinks.
![image](https://github.com/user-attachments/assets/5e0cb287-7609-48b8-a41d-65ab78ce76ad)

Flink Application Sources and Sinks
Parallel Dataflows #
Programs in Flink are inherently parallel and distributed. During execution, a stream has one or more stream partitions, and each operator has one or more operator subtasks. The operator subtasks are independent of one another, and execute in different threads and possibly on different machines or containers.

The number of operator subtasks is the parallelism of that particular operator. Different operators of the same program may have different levels of parallelism.

Parallel Dataflow
Streams can transport data between two operators in a one-to-one (or forwarding) pattern, or in a redistributing pattern:

One-to-one streams (for example between the Source and the map() operators in the figure above) preserve the partitioning and ordering of the elements. That means that subtask[1] of the map() operator will see the same elements in the same order as they were produced by subtask[1] of the Source operator.

Redistributing streams (as between map() and keyBy/window above, as well as between keyBy/window and Sink) change the partitioning of streams. Each operator subtask sends data to different target subtasks, depending on the selected transformation. Examples are keyBy() (which re-partitions by hashing the key), broadcast(), or rebalance() (which re-partitions randomly). In a redistributing exchange the ordering among the elements is only preserved within each pair of sending and receiving subtasks (for example, subtask[1] of map() and subtask[2] of keyBy/window). So, for example, the redistribution between the keyBy/window and the Sink operators shown above introduces non-determinism regarding the order in which the aggregated results for different keys arrive at the Sink.

Timely Stream Processing #
For most streaming applications it is very valuable to be able re-process historic data with the same code that is used to process live data – and to produce deterministic, consistent results, regardless.

It can also be crucial to pay attention to the order in which events occurred, rather than the order in which they are delivered for processing, and to be able to reason about when a set of events is (or should be) complete. For example, consider the set of events involved in an e-commerce transaction, or financial trade.

These requirements for timely stream processing can be met by using event time timestamps that are recorded in the data stream, rather than using the clocks of the machines processing the data.

Stateful Stream Processing #
Flink’s operations can be stateful. This means that how one event is handled can depend on the accumulated effect of all the events that came before it. State may be used for something simple, such as counting events per minute to display on a dashboard, or for something more complex, such as computing features for a fraud detection model.

A Flink application is run in parallel on a distributed cluster. The various parallel instances of a given operator will execute independently, in separate threads, and in general will be running on different machines.

The set of parallel instances of a stateful operator is effectively a sharded key-value store. Each parallel instance is responsible for handling events for a specific group of keys, and the state for those keys is kept locally.

The diagram below shows a job running with a parallelism of two across the first three operators in the job graph, terminating in a sink that has a parallelism of one. The third operator is stateful, and you can see that a fully-connected network shuffle is occurring between the second and third operators. This is being done to partition the stream by some key, so that all of the events that need to be processed together, will be.

Parallel Job
State is always accessed locally, which helps Flink applications achieve high throughput and low-latency. You can choose to keep state on the JVM heap, or if it is too large, in efficiently organized on-disk data structures.

Local State
Fault Tolerance via State Snapshots #
Flink is able to provide fault-tolerant, exactly-once semantics through a combination of state snapshots and stream replay. These snapshots capture the entire state of the distributed pipeline, recording offsets into the input queues as well as the state throughout the job graph that has resulted from having ingested the data up to that point. When a failure occurs, the sources are rewound, the state is restored, and processing is resumed. As depicted above, these state snapshots are captured asynchronously, without impeding the ongoing processing.

Intro to the DataStream API #
The focus of this training is to broadly cover the DataStream API well enough that you will be able to get started writing streaming applications.

What can be Streamed? #
Flink’s DataStream APIs for Java and Scala will let you stream anything they can serialize. Flink’s own serializer is used for

basic types, i.e., String, Long, Integer, Boolean, Array
composite types: Tuples, POJOs, and Scala case classes
and Flink falls back to Kryo for other types. It is also possible to use other serializers with Flink. Avro, in particular, is well supported.

Java tuples and POJOs #
Flink’s native serializer can operate efficiently on tuples and POJOs.

Tuples #
For Java, Flink defines its own Tuple0 thru Tuple25 types.

Tuple2<String, Integer> person = Tuple2.of("Fred", 35);

// zero based index!  
String name = person.f0;
Integer age = person.f1;
POJOs #
Flink recognizes a data type as a POJO type (and allows “by-name” field referencing) if the following conditions are fulfilled:

The class is public and standalone (no non-static inner class)
The class has a public no-argument constructor
All non-static, non-transient fields in the class (and all superclasses) are either public (and non-final) or have public getter- and setter- methods that follow the Java beans naming conventions for getters and setters.
Example:

public class Person {
    public String name;  
    public Integer age;  
    public Person() {}
    public Person(String name, Integer age) {  
        . . .
    }
}  

Person person = new Person("Fred Flintstone", 35);
Flink’s serializer supports schema evolution for POJO types.

Scala tuples and case classes #
These work just as you’d expect.

Back to top

A Complete Example #
This example takes a stream of records about people as input, and filters it to only include the adults.

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.api.common.functions.FilterFunction;

public class Example {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Person> flintstones = env.fromElements(
                new Person("Fred", 35),
                new Person("Wilma", 35),
                new Person("Pebbles", 2));

        DataStream<Person> adults = flintstones.filter(new FilterFunction<Person>() {
            @Override
            public boolean filter(Person person) throws Exception {
                return person.age >= 18;
            }
        });

        adults.print();

        env.execute();
    }

    public static class Person {
        public String name;
        public Integer age;
        public Person() {}

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String toString() {
            return this.name.toString() + ": age " + this.age.toString();
        }
    }
}
Stream execution environment #
Every Flink application needs an execution environment, env in this example. Streaming applications need to use a StreamExecutionEnvironment.

The DataStream API calls made in your application build a job graph that is attached to the StreamExecutionEnvironment. When env.execute() is called this graph is packaged up and sent to the JobManager, which parallelizes the job and distributes slices of it to the Task Managers for execution. Each parallel slice of your job will be executed in a task slot.

Note that if you don’t call execute(), your application won’t be run.
![image](https://github.com/user-attachments/assets/5192bdf2-335b-48cb-92ae-c0ce535a9a89)

Flink runtime: client, job manager, task managers
This distributed runtime depends on your application being serializable. It also requires that all dependencies are available to each node in the cluster.

Basic stream sources #
The example above constructs a DataStream<Person> using env.fromElements(...). This is a convenient way to throw together a simple stream for use in a prototype or test. There is also a fromCollection(Collection) method on StreamExecutionEnvironment. So instead, you could do this:

List<Person> people = new ArrayList<Person>();

people.add(new Person("Fred", 35));
people.add(new Person("Wilma", 35));
people.add(new Person("Pebbles", 2));

DataStream<Person> flintstones = env.fromCollection(people);
Another convenient way to get some data into a stream while prototyping is to use a socket

DataStream<String> lines = env.socketTextStream("localhost", 9999);
or a file

DataStream<String> lines = env.readTextFile("file:///path");
In real applications the most commonly used data sources are those that support low-latency, high throughput parallel reads in combination with rewind and replay – the prerequisites for high performance and fault tolerance – such as Apache Kafka, Kinesis, and various filesystems. REST APIs and databases are also frequently used for stream enrichment.

Basic stream sinks #
The example above uses adults.print() to print its results to the task manager logs (which will appear in your IDE’s console, when running in an IDE). This will call toString() on each element of the stream.

The output looks something like this

1> Fred: age 35
2> Wilma: age 35
where 1> and 2> indicate which sub-task (i.e., thread) produced the output.

In production, commonly used sinks include the StreamingFileSink, various databases, and several pub-sub systems.

Debugging #
In production, your application will run in a remote cluster or set of containers. And if it fails, it will fail remotely. The JobManager and TaskManager logs can be very helpful in debugging such failures, but it is much easier to do local debugging inside an IDE, which is something that Flink supports. You can set breakpoints, examine local variables, and step through your code. You can also step into Flink’s code, which can be a great way to learn more about its internals if you are curious to see how Flink works.

Back to top

Hands-on #
At this point you know enough to get started coding and running a simple DataStream application. Clone the flink-training-repo , and after following the instructions in the README, do the first exercise: Filtering a Stream (Ride Cleansing) .

Back to top

Further Reading #
Flink Serialization Tuning Vol. 1: Choosing your Serializer — if you can
Anatomy of a Flink Program
Data Sources
Data Sinks
DataStream Connectors

Data Pipelines & ETL #
One very common use case for Apache Flink is to implement ETL (extract, transform, load) pipelines that take data from one or more sources, perform some transformations and/or enrichments, and then store the results somewhere. In this section we are going to look at how to use Flink’s DataStream API to implement this kind of application.

Note that Flink’s Table and SQL APIs are well suited for many ETL use cases. But regardless of whether you ultimately use the DataStream API directly, or not, having a solid understanding the basics presented here will prove valuable.

Stateless Transformations #
This section covers map() and flatmap(), the basic operations used to implement stateless transformations. The examples in this section assume you are familiar with the Taxi Ride data used in the hands-on exercises in the flink-training-repo .

map() #
In the first exercise you filtered a stream of taxi ride events. In that same code base there’s a GeoUtils class that provides a static method GeoUtils.mapToGridCell(float lon, float lat) which maps a location (longitude, latitude) to a grid cell that refers to an area that is approximately 100x100 meters in size.

Now let’s enrich our stream of taxi ride objects by adding startCell and endCell fields to each event. You can create an EnrichedRide object that extends TaxiRide, adding these fields:

public static class EnrichedRide extends TaxiRide {
    public int startCell;
    public int endCell;

    public EnrichedRide() {}

    public EnrichedRide(TaxiRide ride) {
        this.rideId = ride.rideId;
        this.isStart = ride.isStart;
        ...
        this.startCell = GeoUtils.mapToGridCell(ride.startLon, ride.startLat);
        this.endCell = GeoUtils.mapToGridCell(ride.endLon, ride.endLat);
    }

    public String toString() {
        return super.toString() + "," +
            Integer.toString(this.startCell) + "," +
            Integer.toString(this.endCell);
    }
}
You can then create an application that transforms the stream

DataStream<TaxiRide> rides = env.addSource(new TaxiRideSource(...));

DataStream<EnrichedRide> enrichedNYCRides = rides
    .filter(new RideCleansingSolution.NYCFilter())
    .map(new Enrichment());

enrichedNYCRides.print();
with this MapFunction:

public static class Enrichment implements MapFunction<TaxiRide, EnrichedRide> {

    @Override
    public EnrichedRide map(TaxiRide taxiRide) throws Exception {
        return new EnrichedRide(taxiRide);
    }
}
flatmap() #
A MapFunction is suitable only when performing a one-to-one transformation: for each and every stream element coming in, map() will emit one transformed element. Otherwise, you will want to use flatmap()

DataStream<TaxiRide> rides = env.addSource(new TaxiRideSource(...));

DataStream<EnrichedRide> enrichedNYCRides = rides
    .flatMap(new NYCEnrichment());

enrichedNYCRides.print();
together with a FlatMapFunction:

public static class NYCEnrichment implements FlatMapFunction<TaxiRide, EnrichedRide> {

    @Override
    public void flatMap(TaxiRide taxiRide, Collector<EnrichedRide> out) throws Exception {
        FilterFunction<TaxiRide> valid = new RideCleansing.NYCFilter();
        if (valid.filter(taxiRide)) {
            out.collect(new EnrichedRide(taxiRide));
        }
    }
}
With the Collector provided in this interface, the flatmap() method can emit as many stream elements as you like, including none at all.

Back to top

Keyed Streams #
keyBy() #
It is often very useful to be able to partition a stream around one of its attributes, so that all events with the same value of that attribute are grouped together. For example, suppose you wanted to find the longest taxi rides starting in each of the grid cells. Thinking in terms of a SQL query, this would mean doing some sort of GROUP BY with the startCell, while in Flink this is done with keyBy(KeySelector)

rides
    .flatMap(new NYCEnrichment())
    .keyBy(enrichedRide -> enrichedRide.startCell);
Every keyBy causes a network shuffle that repartitions the stream. In general this is pretty expensive, since it involves network communication along with serialization and deserialization.

keyBy and network shuffle
Keys are computed #
KeySelectors aren’t limited to extracting a key from your events. They can, instead, compute the key in whatever way you want, so long as the resulting key is deterministic, and has valid implementations of hashCode() and equals(). This restriction rules out KeySelectors that generate random numbers, or that return Arrays or Enums, but you can have composite keys using Tuples or POJOs, for example, so long as their elements follow these same rules.

The keys must be produced in a deterministic way, because they are recomputed whenever they are needed, rather than being attached to the stream records.

For example, rather than creating a new EnrichedRide class with a startCell field that we then use as a key via

keyBy(enrichedRide -> enrichedRide.startCell);
we could do this, instead:

keyBy(ride -> GeoUtils.mapToGridCell(ride.startLon, ride.startLat));
Aggregations on Keyed Streams #
This bit of code creates a new stream of tuples containing the startCell and duration (in minutes) for each end-of-ride event:

import org.joda.time.Interval;

DataStream<Tuple2<Integer, Minutes>> minutesByStartCell = enrichedNYCRides
    .flatMap(new FlatMapFunction<EnrichedRide, Tuple2<Integer, Minutes>>() {

        @Override
        public void flatMap(EnrichedRide ride,
                            Collector<Tuple2<Integer, Minutes>> out) throws Exception {
            if (!ride.isStart) {
                Interval rideInterval = new Interval(ride.startTime, ride.endTime);
                Minutes duration = rideInterval.toDuration().toStandardMinutes();
                out.collect(new Tuple2<>(ride.startCell, duration));
            }
        }
    });
Now it is possible to produce a stream that contains only those rides that are the longest rides ever seen (to that point) for each startCell.

There are a variety of ways that the field to use as the key can be expressed. Earlier you saw an example with an EnrichedRide POJO, where the field to use as the key was specified with its name. This case involves Tuple2 objects, and the index within the tuple (starting from 0) is used to specify the key.

minutesByStartCell
  .keyBy(value -> value.f0) // .keyBy(value -> value.startCell)
  .maxBy(1) // duration
  .print();
The output stream now contains a record for each key every time the duration reaches a new maximum – as shown here with cell 50797:

...
4> (64549,5M)
4> (46298,18M)
1> (51549,14M)
1> (53043,13M)
1> (56031,22M)
1> (50797,6M)
...
1> (50797,8M)
...
1> (50797,11M)
...
1> (50797,12M)
(Implicit) State #
This is the first example in this training that involves stateful streaming. Though the state is being handled transparently, Flink has to keep track of the maximum duration for each distinct key.

Whenever state gets involved in your application, you should think about how large the state might become. Whenever the key space is unbounded, then so is the amount of state Flink will need.

When working with streams, it generally makes more sense to think in terms of aggregations over finite windows, rather than over the entire stream.

reduce() and other aggregators #
maxBy(), used above, is just one example of a number of aggregator functions available on Flink’s KeyedStreams. There is also a more general purpose reduce() function that you can use to implement your own custom aggregations.

Back to top

Stateful Transformations #
Why is Flink Involved in Managing State? #
Your applications are certainly capable of using state without getting Flink involved in managing it – but Flink offers some compelling features for the state it manages:

local: Flink state is kept local to the machine that processes it, and can be accessed at memory speed
durable: Flink state is fault-tolerant, i.e., it is automatically checkpointed at regular intervals, and is restored upon failure
vertically scalable: Flink state can be kept in embedded RocksDB instances that scale by adding more local disk
horizontally scalable: Flink state is redistributed as your cluster grows and shrinks
queryable: Flink state can be queried externally via the Queryable State API.
In this section you will learn how to work with Flink’s APIs that manage keyed state.

Rich Functions #
At this point you have already seen several of Flink’s function interfaces, including FilterFunction, MapFunction, and FlatMapFunction. These are all examples of the Single Abstract Method pattern.

For each of these interfaces, Flink also provides a so-called “rich” variant, e.g., RichFlatMapFunction, which has some additional methods, including:

open(Configuration c)
close()
getRuntimeContext()
open() is called once, during operator initialization. This is an opportunity to load some static data, or to open a connection to an external service, for example.

getRuntimeContext() provides access to a whole suite of potentially interesting things, but most notably it is how you can create and access state managed by Flink.

An Example with Keyed State #
In this example, imagine you have a stream of events that you want to de-duplicate, so that you only keep the first event with each key. Here’s an application that does that, using a RichFlatMapFunction called Deduplicator:

private static class Event {
    public final String key;
    public final long timestamp;
    ...
}

public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
  
    env.addSource(new EventSource())
        .keyBy(e -> e.key)
        .flatMap(new Deduplicator())
        .print();
  
    env.execute();
}
To accomplish this, Deduplicator will need to somehow remember, for each key, whether or not there has already been an event for that key. It will do so using Flink’s keyed state interface.

When you are working with a keyed stream like this one, Flink will maintain a key/value store for each item of state being managed.

Flink supports several different types of keyed state, and this example uses the simplest one, namely ValueState. This means that for each key, Flink will store a single object – in this case, an object of type Boolean.

Our Deduplicator class has two methods: open() and flatMap(). The open method establishes the use of managed state by defining a ValueStateDescriptor<Boolean>. The arguments to the constructor specify a name for this item of keyed state (“keyHasBeenSeen”), and provide information that can be used to serialize these objects (in this case, Types.BOOLEAN).

public static class Deduplicator extends RichFlatMapFunction<Event, Event> {
    ValueState<Boolean> keyHasBeenSeen;

    @Override
    public void open(Configuration conf) {
        ValueStateDescriptor<Boolean> desc = new ValueStateDescriptor<>("keyHasBeenSeen", Types.BOOLEAN);
        keyHasBeenSeen = getRuntimeContext().getState(desc);
    }

    @Override
    public void flatMap(Event event, Collector<Event> out) throws Exception {
        if (keyHasBeenSeen.value() == null) {
            out.collect(event);
            keyHasBeenSeen.update(true);
        }
    }
}
When the flatMap method calls keyHasBeenSeen.value(), Flink’s runtime looks up the value of this piece of state for the key in context, and only if it is null does it go ahead and collect the event to the output. It also updates keyHasBeenSeen to true in this case.

This mechanism for accessing and updating key-partitioned state may seem rather magical, since the key is not explicitly visible in the implementation of our Deduplicator. When Flink’s runtime calls the open method of our RichFlatMapFunction, there is no event, and thus no key in context at that moment. But when it calls the flatMap method, the key for the event being processed is available to the runtime, and is used behind the scenes to determine which entry in Flink’s state backend is being operated on.

When deployed to a distributed cluster, there will be many instances of this Deduplicator, each of which will responsible for a disjoint subset of the entire keyspace. Thus, when you see a single item of ValueState, such as

ValueState<Boolean> keyHasBeenSeen;
understand that this represents not just a single Boolean, but rather a distributed, sharded, key/value store.

Clearing State #
There’s a potential problem with the example above: What will happen if the key space is unbounded? Flink is storing somewhere an instance of Boolean for every distinct key that is used. If there’s a bounded set of keys then this will be fine, but in applications where the set of keys is growing in an unbounded way, it’s necessary to clear the state for keys that are no longer needed. This is done by calling clear() on the state object, as in:

keyHasBeenSeen.clear();
You might want to do this, for example, after a period of inactivity for a given key. You’ll see how to use Timers to do this when you learn about ProcessFunctions in the section on event-driven applications.

There’s also a State Time-to-Live (TTL) option that you can configure with the state descriptor that specifies when you want the state for stale keys to be automatically cleared.

Non-keyed State #
It is also possible to work with managed state in non-keyed contexts. This is sometimes called operator state. The interfaces involved are somewhat different, and since it is unusual for user-defined functions to need non-keyed state, it is not covered here. This feature is most often used in the implementation of sources and sinks.

Back to top

Connected Streams #
Sometimes instead of applying a pre-defined transformation like this:

simple transformation
you want to be able to dynamically alter some aspects of the transformation – by streaming in thresholds, or rules, or other parameters. The pattern in Flink that supports this is something called connected streams, wherein a single operator has two input streams, like this:

connected streams
Connected streams can also be used to implement streaming joins.

Example #
In this example, a control stream is used to specify words which must be filtered out of the streamOfWords. A RichCoFlatMapFunction called ControlFunction is applied to the connected streams to get this done.

public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    DataStream<String> control = env
        .fromElements("DROP", "IGNORE")
        .keyBy(x -> x);

    DataStream<String> streamOfWords = env
        .fromElements("Apache", "DROP", "Flink", "IGNORE")
        .keyBy(x -> x);
  
    control
        .connect(streamOfWords)
        .flatMap(new ControlFunction())
        .print();

    env.execute();
}
Note that the two streams being connected must be keyed in compatible ways. The role of a keyBy is to partition a stream’s data, and when keyed streams are connected, they must be partitioned in the same way. This ensures that all of the events from both streams with the same key are sent to the same instance. This makes it possible, then, to join the two streams on that key, for example.

In this case the streams are both of type DataStream<String>, and both streams are keyed by the string. As you will see below, this RichCoFlatMapFunction is storing a Boolean value in keyed state, and this Boolean is shared by the two streams.

public static class ControlFunction extends RichCoFlatMapFunction<String, String, String> {
    private ValueState<Boolean> blocked;
      
    @Override
    public void open(Configuration config) {
        blocked = getRuntimeContext()
            .getState(new ValueStateDescriptor<>("blocked", Boolean.class));
    }
      
    @Override
    public void flatMap1(String control_value, Collector<String> out) throws Exception {
        blocked.update(Boolean.TRUE);
    }
      
    @Override
    public void flatMap2(String data_value, Collector<String> out) throws Exception {
        if (blocked.value() == null) {
            out.collect(data_value);
        }
    }
}
A RichCoFlatMapFunction is a kind of FlatMapFunction that can be applied to a pair of connected streams, and it has access to the rich function interface. This means that it can be made stateful.

The blocked Boolean is being used to remember the keys (words, in this case) that have been mentioned on the control stream, and those words are being filtered out of the streamOfWords stream. This is keyed state, and it is shared between the two streams, which is why the two streams have to share the same keyspace.

flatMap1 and flatMap2 are called by the Flink runtime with elements from each of the two connected streams – in our case, elements from the control stream are passed into flatMap1, and elements from streamOfWords are passed into flatMap2. This was determined by the order in which the two streams are connected with control.connect(streamOfWords).

It is important to recognize that you have no control over the order in which the flatMap1 and flatMap2 callbacks are called. These two input streams are racing against each other, and the Flink runtime will do what it wants to regarding consuming events from one stream or the other. In cases where timing and/or ordering matter, you may find it necessary to buffer events in managed Flink state until your application is ready to process them. (Note: if you are truly desperate, it is possible to exert some limited control over the order in which a two-input operator consumes its inputs by using a custom Operator that implements the InputSelectable interface.

Streaming Analytics #
Event Time and Watermarks #
Introduction #
Flink explicitly supports three different notions of time:

event time: the time when an event occurred, as recorded by the device producing (or storing) the event

ingestion time: a timestamp recorded by Flink at the moment it ingests the event

processing time: the time when a specific operator in your pipeline is processing the event

For reproducible results, e.g., when computing the maximum price a stock reached during the first hour of trading on a given day, you should use event time. In this way the result won’t depend on when the calculation is performed. This kind of real-time application is sometimes performed using processing time, but then the results are determined by the events that happen to be processed during that hour, rather than the events that occurred then. Computing analytics based on processing time causes inconsistencies, and makes it difficult to re-analyze historic data or test new implementations.

Working with Event Time #
If you want to use event time, you will also need to supply a Timestamp Extractor and Watermark Generator that Flink will use to track the progress of event time. This will be covered in the section below on Working with Watermarks, but first we should explain what watermarks are.

Watermarks #
Let’s work through a simple example that will show why watermarks are needed, and how they work.

In this example you have a stream of timestamped events that arrive somewhat out of order, as shown below. The numbers shown are timestamps that indicate when these events actually occurred. The first event to arrive happened at time 4, and it is followed by an event that happened earlier, at time 2, and so on:

··· 23 19 22 24 21 14 17 13 12 15 9 11 7 2 4 →
Now imagine that you are trying create a stream sorter. This is meant to be an application that processes each event from a stream as it arrives, and emits a new stream containing the same events, but ordered by their timestamps.

Some observations:

(1) The first element your stream sorter sees is the 4, but you can’t just immediately release it as the first element of the sorted stream. It may have arrived out of order, and an earlier event might yet arrive. In fact, you have the benefit of some god-like knowledge of this stream’s future, and you can see that your stream sorter should wait at least until the 2 arrives before producing any results.

Some buffering, and some delay, is necessary.

(2) If you do this wrong, you could end up waiting forever. First the sorter saw an event from time 4, and then an event from time 2. Will an event with a timestamp less than 2 ever arrive? Maybe. Maybe not. You could wait forever and never see a 1.

Eventually you have to be courageous and emit the 2 as the start of the sorted stream.

(3) What you need then is some sort of policy that defines when, for any given timestamped event, to stop waiting for the arrival of earlier events.

This is precisely what watermarks do — they define when to stop waiting for earlier events.

Event time processing in Flink depends on watermark generators that insert special timestamped elements into the stream, called watermarks. A watermark for time t is an assertion that the stream is (probably) now complete up through time t.

When should this stream sorter stop waiting, and push out the 2 to start the sorted stream? When a watermark arrives with a timestamp of 2, or greater.

(4) You might imagine different policies for deciding how to generate watermarks.

Each event arrives after some delay, and these delays vary, so some events are delayed more than others. One simple approach is to assume that these delays are bounded by some maximum delay. Flink refers to this strategy as bounded-out-of-orderness watermarking. It is easy to imagine more complex approaches to watermarking, but for most applications a fixed delay works well enough.

Latency vs. Completeness #
Another way to think about watermarks is that they give you, the developer of a streaming application, control over the tradeoff between latency and completeness. Unlike in batch processing, where one has the luxury of being able to have complete knowledge of the input before producing any results, with streaming you must eventually stop waiting to see more of the input, and produce some sort of result.

You can either configure your watermarking aggressively, with a short bounded delay, and thereby take the risk of producing results with rather incomplete knowledge of the input – i.e., a possibly wrong result, produced quickly. Or you can wait longer, and produce results that take advantage of having more complete knowledge of the input stream(s).

It is also possible to implement hybrid solutions that produce initial results quickly, and then supply updates to those results as additional (late) data is processed. This is a good approach for some applications.

Lateness #
Lateness is defined relative to the watermarks. A Watermark(t) asserts that the stream is complete up through time t; any event following this watermark whose timestamp is ≤ t is late.

Working with Watermarks #
In order to perform event-time-based event processing, Flink needs to know the time associated with each event, and it also needs the stream to include watermarks.

The Taxi data sources used in the hands-on exercises take care of these details for you. But in your own applications you will have to take care of this yourself, which is usually done by implementing a class that extracts the timestamps from the events, and generates watermarks on demand. The easiest way to do this is by using a WatermarkStrategy:

DataStream<Event> stream = ...

WatermarkStrategy<Event> strategy = WatermarkStrategy
        .<Event>forBoundedOutOfOrderness(Duration.ofSeconds(20))
        .withTimestampAssigner((event, timestamp) -> event.timestamp);

DataStream<Event> withTimestampsAndWatermarks =
    stream.assignTimestampsAndWatermarks(strategy);
Back to top

Windows #
Flink features very expressive window semantics.

In this section you will learn:

how windows are used to compute aggregates on unbounded streams,
which types of windows Flink supports, and
how to implement a DataStream program with a windowed aggregation
Introduction #
It is natural when doing stream processing to want to compute aggregated analytics on bounded subsets of the streams in order to answer questions like these:

number of page views per minute
number of sessions per user per week
maximum temperature per sensor per minute
Computing windowed analytics with Flink depends on two principal abstractions: Window Assigners that assign events to windows (creating new window objects as necessary), and Window Functions that are applied to the events assigned to a window.

Flink’s windowing API also has notions of Triggers, which determine when to call the window function, and Evictors, which can remove elements collected in a window.

In its basic form, you apply windowing to a keyed stream like this:

stream.
    .keyBy(<key selector>)
    .window(<window assigner>)
    .reduce|aggregate|process(<window function>);
You can also use windowing with non-keyed streams, but keep in mind that in this case, the processing will not be done in parallel:

stream.
    .windowAll(<window assigner>)
    .reduce|aggregate|process(<window function>);
Window Assigners #
Flink has several built-in types of window assigners, which are illustrated below:

Window assigners
![image](https://github.com/user-attachments/assets/f77cb928-0877-41bf-97c6-8b63be263678)

Some examples of what these window assigners might be used for, and how to specify them:

Tumbling time windows
page views per minute
TumblingEventTimeWindows.of(Time.minutes(1))
Sliding time windows
page views per minute computed every 10 seconds
SlidingEventTimeWindows.of(Time.minutes(1), Time.seconds(10))
Session windows
page views per session, where sessions are defined by a gap of at least 30 minutes between sessions
EventTimeSessionWindows.withGap(Time.minutes(30))
Durations can be specified using one of Time.milliseconds(n), Time.seconds(n), Time.minutes(n), Time.hours(n), and Time.days(n).

The time-based window assigners (including session windows) come in both event time and processing time flavors. There are significant tradeoffs between these two types of time windows. With processing time windowing you have to accept these limitations:

can not correctly process historic data,
can not correctly handle out-of-order data,
results will be non-deterministic,
but with the advantage of lower latency.

When working with count-based windows, keep in mind that these windows will not fire until a batch is complete. There’s no option to time-out and process a partial window, though you could implement that behavior yourself with a custom Trigger.

A global window assigner assigns every event (with the same key) to the same global window. This is only useful if you are going to do your own custom windowing, with a custom Trigger. In many cases where this might seem useful you will be better off using a ProcessFunction as described in another section.

Window Functions #
You have three basic options for how to process the contents of your windows:

as a batch, using a ProcessWindowFunction that will be passed an Iterable with the window’s contents;
incrementally, with a ReduceFunction or an AggregateFunction that is called as each event is assigned to the window;
or with a combination of the two, wherein the pre-aggregated results of a ReduceFunction or an AggregateFunction are supplied to a ProcessWindowFunction when the window is triggered.
Here are examples of approaches 1 and 3. Each implementation finds the peak value from each sensor in 1 minute event time windows, and producing a stream of Tuples containing (key, end-of-window-timestamp, max_value).

ProcessWindowFunction Example #
DataStream<SensorReading> input = ...

input
    .keyBy(x -> x.key)
    .window(TumblingEventTimeWindows.of(Time.minutes(1)))
    .process(new MyWastefulMax());

public static class MyWastefulMax extends ProcessWindowFunction<
        SensorReading,                  // input type
        Tuple3<String, Long, Integer>,  // output type
        String,                         // key type
        TimeWindow> {                   // window type
    
    @Override
    public void process(
            String key,
            Context context, 
            Iterable<SensorReading> events,
            Collector<Tuple3<String, Long, Integer>> out) {

        int max = 0;
        for (SensorReading event : events) {
            max = Math.max(event.value, max);
        }
        out.collect(Tuple3.of(key, context.window().getEnd(), max));
    }
}
A couple of things to note in this implementation:

All of the events assigned to the window have to be buffered in keyed Flink state until the window is triggered. This is potentially quite expensive.
Our ProcessWindowFunction is being passed a Context object which contains information about the window. Its interface looks like this:
public abstract class Context implements java.io.Serializable {
    public abstract W window();
    
    public abstract long currentProcessingTime();
    public abstract long currentWatermark();

    public abstract KeyedStateStore windowState();
    public abstract KeyedStateStore globalState();
}
windowState and globalState are places where you can store per-key, per-window, or global per-key information for all windows of that key. This might be useful, for example, if you want to record something about the current window and use that when processing a subsequent window.

Incremental Aggregation Example #
DataStream<SensorReading> input = ...

input
    .keyBy(x -> x.key)
    .window(TumblingEventTimeWindows.of(Time.minutes(1)))
    .reduce(new MyReducingMax(), new MyWindowFunction());

private static class MyReducingMax implements ReduceFunction<SensorReading> {
    public SensorReading reduce(SensorReading r1, SensorReading r2) {
        return r1.value() > r2.value() ? r1 : r2;
    }
}

private static class MyWindowFunction extends ProcessWindowFunction<
    SensorReading, Tuple3<String, Long, SensorReading>, String, TimeWindow> {

    @Override
    public void process(
            String key,
            Context context,
            Iterable<SensorReading> maxReading,
            Collector<Tuple3<String, Long, SensorReading>> out) {

        SensorReading max = maxReading.iterator().next();
        out.collect(Tuple3.of(key, context.window().getEnd(), max));
    }
}
Notice that the Iterable<SensorReading> will contain exactly one reading – the pre-aggregated maximum computed by MyReducingMax.

Late Events #
By default, when using event time windows, late events are dropped. There are two optional parts of the window API that give you more control over this.

You can arrange for the events that would be dropped to be collected to an alternate output stream instead, using a mechanism called Side Outputs. Here is an example of what that might look like:

OutputTag<Event> lateTag = new OutputTag<Event>("late"){};

SingleOutputStreamOperator<Event> result = stream.
    .keyBy(...)
    .window(...)
    .sideOutputLateData(lateTag)
    .process(...);
  
DataStream<Event> lateStream = result.getSideOutput(lateTag);
You can also specify an interval of allowed lateness during which the late events will continue to be assigned to the appropriate window(s) (whose state will have been retained). By default each late event will cause the window function to be called again (sometimes called a late firing).

By default the allowed lateness is 0. In other words, elements behind the watermark are dropped (or sent to the side output).

For example:

stream.
    .keyBy(...)
    .window(...)
    .allowedLateness(Time.seconds(10))
    .process(...);
When the allowed lateness is greater than zero, only those events that are so late that they would be dropped are sent to the side output (if it has been configured).

Surprises #
Some aspects of Flink’s windowing API may not behave in the way you would expect. Based on frequently asked questions on the flink-user mailing list and elsewhere, here are some facts about windows that may surprise you.

Sliding Windows Make Copies #
Sliding window assigners can create lots of window objects, and will copy each event into every relevant window. For example, if you have sliding windows every 15 minutes that are 24-hours in length, each event will be copied into 4 * 24 = 96 windows.

Time Windows are Aligned to the Epoch #
Just because you are using hour-long processing-time windows and start your application running at 12:05 does not mean that the first window will close at 1:05. The first window will be 55 minutes long and close at 1:00.

Note, however, that the tumbling and sliding window assigners take an optional offset parameter that can be used to change the alignment of the windows. See Tumbling Windows and Sliding Windows for details.

Windows Can Follow Windows #
For example, it works to do this:

stream
    .keyBy(t -> t.key)
    .window(<window assigner>)
    .reduce(<reduce function>)
    .windowAll(<same window assigner>)
    .reduce(<same reduce function>);
You might expect Flink’s runtime to be smart enough to do this parallel pre-aggregation for you (provided you are using a ReduceFunction or AggregateFunction), but it’s not.

The reason why this works is that the events produced by a time window are assigned timestamps based on the time at the end of the window. So, for example, all of the events produced by an hour-long window will have timestamps marking the end of an hour. Any subsequent window consuming those events should have a duration that is the same as, or a multiple of, the previous window.

No Results for Empty TimeWindows #
Windows are only created when events are assigned to them. So if there are no events in a given time frame, no results will be reported.

Late Events Can Cause Late Merges #
Session windows are based on an abstraction of windows that can merge. Each element is initially assigned to a new window, after which windows are merged whenever the gap between them is small enough. In this way, a late event can bridge the gap separating two previously separate sessions, producing a late merge.

Event-driven Applications #
Process Functions #
Introduction #
A ProcessFunction combines event processing with timers and state, making it a powerful building block for stream processing applications. This is the basis for creating event-driven applications with Flink. It is very similar to a RichFlatMapFunction, but with the addition of timers.

Example #
If you’ve done the hands-on exercise in the Streaming Analytics training, you will recall that it uses a TumblingEventTimeWindow to compute the sum of the tips for each driver during each hour, like this:

// compute the sum of the tips per hour for each driver
DataStream<Tuple3<Long, Long, Float>> hourlyTips = fares
        .keyBy((TaxiFare fare) -> fare.driverId)
        .window(TumblingEventTimeWindows.of(Time.hours(1)))
        .process(new AddTips());
It is reasonably straightforward, and educational, to do the same thing with a KeyedProcessFunction. Let us begin by replacing the code above with this:

// compute the sum of the tips per hour for each driver
DataStream<Tuple3<Long, Long, Float>> hourlyTips = fares
        .keyBy((TaxiFare fare) -> fare.driverId)
        .process(new PseudoWindow(Time.hours(1)));
In this code snippet a KeyedProcessFunction called PseudoWindow is being applied to a keyed stream, the result of which is a DataStream<Tuple3<Long, Long, Float>> (the same kind of stream produced by the implementation that uses Flink’s built-in time windows).

The overall outline of PseudoWindow has this shape:

// Compute the sum of the tips for each driver in hour-long windows.
// The keys are driverIds.
public static class PseudoWindow extends 
        KeyedProcessFunction<Long, TaxiFare, Tuple3<Long, Long, Float>> {

    private final long durationMsec;

    public PseudoWindow(Time duration) {
        this.durationMsec = duration.toMilliseconds();
    }

    @Override
    // Called once during initialization.
    public void open(Configuration conf) {
        . . .
    }

    @Override
    // Called as each fare arrives to be processed.
    public void processElement(
            TaxiFare fare,
            Context ctx,
            Collector<Tuple3<Long, Long, Float>> out) throws Exception {

        . . .
    }

    @Override
    // Called when the current watermark indicates that a window is now complete.
    public void onTimer(long timestamp, 
            OnTimerContext context, 
            Collector<Tuple3<Long, Long, Float>> out) throws Exception {

        . . .
    }
}
Things to be aware of:

There are several types of ProcessFunctions – this is a KeyedProcessFunction, but there are also CoProcessFunctions, BroadcastProcessFunctions, etc.

A KeyedProcessFunction is a kind of RichFunction. Being a RichFunction, it has access to the open and getRuntimeContext methods needed for working with managed keyed state.

There are two callbacks to implement: processElement and onTimer. processElement is called with each incoming event; onTimer is called when timers fire. These can be either event time or processing time timers. Both processElement and onTimer are provided with a context object that can be used to interact with a TimerService (among other things). Both callbacks are also passed a Collector that can be used to emit results.

The open() method #
// Keyed, managed state, with an entry for each window, keyed by the window's end time.
// There is a separate MapState object for each driver.
private transient MapState<Long, Float> sumOfTips;

@Override
public void open(Configuration conf) {

    MapStateDescriptor<Long, Float> sumDesc =
            new MapStateDescriptor<>("sumOfTips", Long.class, Float.class);
    sumOfTips = getRuntimeContext().getMapState(sumDesc);
}
Because the fare events can arrive out of order, it will sometimes be necessary to process events for one hour before having finished computing the results for the previous hour. In fact, if the watermarking delay is much longer than the window length, then there may be many windows open simultaneously, rather than just two. This implementation supports this by using a MapState that maps the timestamp for the end of each window to the sum of the tips for that window.

The processElement() method #
public void processElement(
        TaxiFare fare,
        Context ctx,
        Collector<Tuple3<Long, Long, Float>> out) throws Exception {

    long eventTime = fare.getEventTime();
    TimerService timerService = ctx.timerService();

    if (eventTime <= timerService.currentWatermark()) {
        // This event is late; its window has already been triggered.
    } else {
        // Round up eventTime to the end of the window containing this event.
        long endOfWindow = (eventTime - (eventTime % durationMsec) + durationMsec - 1);

        // Schedule a callback for when the window has been completed.
        timerService.registerEventTimeTimer(endOfWindow);

        // Add this fare's tip to the running total for that window.
        Float sum = sumOfTips.get(endOfWindow);
        if (sum == null) {
            sum = 0.0F;
        }
        sum += fare.tip;
        sumOfTips.put(endOfWindow, sum);
    }
}
Things to consider:

What happens with late events? Events that are behind the watermark (i.e., late) are being dropped. If you want to do something better than this, consider using a side output, which is explained in the next section.

This example uses a MapState where the keys are timestamps, and sets a Timer for that same timestamp. This is a common pattern; it makes it easy and efficient to lookup relevant information when the timer fires.

The onTimer() method #
public void onTimer(
        long timestamp, 
        OnTimerContext context, 
        Collector<Tuple3<Long, Long, Float>> out) throws Exception {

    long driverId = context.getCurrentKey();
    // Look up the result for the hour that just ended.
    Float sumOfTips = this.sumOfTips.get(timestamp);

    Tuple3<Long, Long, Float> result = Tuple3.of(driverId, timestamp, sumOfTips);
    out.collect(result);
    this.sumOfTips.remove(timestamp);
}
Observations:

The OnTimerContext context passed in to onTimer can be used to determine the current key.

Our pseudo-windows are being triggered when the current watermark reaches the end of each hour, at which point onTimer is called. This onTimer method removes the related entry from sumOfTips, which has the effect of making it impossible to accommodate late events. This is the equivalent of setting the allowedLateness to zero when working with Flink’s time windows.

Performance Considerations #
Flink provides MapState and ListState types that are optimized for RocksDB. Where possible, these should be used instead of a ValueState object holding some sort of collection. The RocksDB state backend can append to ListState without going through (de)serialization, and for MapState, each key/value pair is a separate RocksDB object, so MapState can be efficiently accessed and updated.

Back to top

Side Outputs #
Introduction #
There are several good reasons to want to have more than one output stream from a Flink operator, such as reporting:

exceptions
malformed events
late events
operational alerts, such as timed-out connections to external services
Side outputs are a convenient way to do this. Beyond error reporting, side outputs are also a good way to implement an n-way split of a stream.

Example #
You are now in a position to do something with the late events that were ignored in the previous section.

A side output channel is associated with an OutputTag<T>. These tags have generic types that correspond to the type of the side output’s DataStream, and they have names.

private static final OutputTag<TaxiFare> lateFares = new OutputTag<TaxiFare>("lateFares") {};
Shown above is a static OutputTag<TaxiFare> that can be referenced both when emitting late events in the processElement method of the PseudoWindow:

if (eventTime <= timerService.currentWatermark()) {
    // This event is late; its window has already been triggered.
    ctx.output(lateFares, fare);
} else {
    . . .
}
and when accessing the stream from this side output in the main method of the job:

// compute the sum of the tips per hour for each driver
SingleOutputStreamOperator hourlyTips = fares
        .keyBy((TaxiFare fare) -> fare.driverId)
        .process(new PseudoWindow(Time.hours(1)));

hourlyTips.getSideOutput(lateFares).print();
Alternatively, you can use two OutputTags with the same name to refer to the same side output, but if you do, they must have the same type.

Back to top

Closing Remarks #
In this example you have seen how a ProcessFunction can be used to reimplement a straightforward time window. Of course, if Flink’s built-in windowing API meets your needs, by all means, go ahead and use it. But if you find yourself considering doing something contorted with Flink’s windows, don’t be afraid to roll your own.

Also, ProcessFunctions are useful for many other use cases beyond computing analytics. The hands-on exercise below provides an example of something completely different.

Another common use case for ProcessFunctions is for expiring stale state. If you think back to the Rides and Fares Exercise , where a RichCoFlatMapFunction is used to compute a simple join, the sample solution assumes that the TaxiRides and TaxiFares are perfectly matched, one-to-one for each rideId. If an event is lost, the other event for the same rideId will be held in state forever. This could instead be implemented as a KeyedCoProcessFunction, and a timer could be used to detect and clear any stale state.

Fault Tolerance via State Snapshots #
State Backends #
The keyed state managed by Flink is a sort of sharded, key/value store, and the working copy of each item of keyed state is kept somewhere local to the taskmanager responsible for that key. Operator state is also local to the machine(s) that need(s) it.

This state that Flink manages is stored in a state backend. Two implementations of state backends are available – one based on RocksDB, an embedded key/value store that keeps its working state on disk, and another heap-based state backend that keeps its working state in memory, on the Java heap.

Name	Working State	Snapshotting
EmbeddedRocksDBStateBackend	Local disk (tmp dir)	Full / Incremental
Supports state larger than available memory
Rule of thumb: 10x slower than heap-based backends
HashMapStateBackend	JVM Heap	Full
Fast, requires large heap
Subject to GC
When working with state kept in a heap-based state backend, accesses and updates involve reading and writing objects on the heap. But for objects kept in the EmbeddedRocksDBStateBackend, accesses and updates involve serialization and deserialization, and so are much more expensive. But the amount of state you can have with RocksDB is limited only by the size of the local disk. Note also that only the EmbeddedRocksDBStateBackend is able to do incremental snapshotting, which is a significant benefit for applications with large amounts of slowly changing state.

Both of these state backends are able to do asynchronous snapshotting, meaning that they can take a snapshot without impeding the ongoing stream processing.

Checkpoint Storage #
Flink periodically takes persistent snapshots of all the state in every operator and copies these snapshots somewhere more durable, such as a distributed file system. In the event of the failure, Flink can restore the complete state of your application and resume processing as though nothing had gone wrong.

The location where these snapshots are stored is defined via the jobs checkpoint storage. Two implementations of checkpoint storage are available - one that persists its state snapshots to a distributed file system, and another that users the JobManager’s heap.

Name	State Backup
FileSystemCheckpointStorage	Distributed file system
Supports very large state size
Highly durable
Recommended for production deployments
JobManagerCheckpointStorage	JobManager JVM Heap
Good for testing and experimentation with small state (locally)
Back to top

State Snapshots #
Definitions #
Snapshot – a generic term referring to a global, consistent image of the state of a Flink job. A snapshot includes a pointer into each of the data sources (e.g., an offset into a file or Kafka partition), as well as a copy of the state from each of the job’s stateful operators that resulted from having processed all of the events up to those positions in the sources.
Checkpoint – a snapshot taken automatically by Flink for the purpose of being able to recover from faults. Checkpoints can be incremental, and are optimized for being restored quickly.
Externalized Checkpoint – normally checkpoints are not intended to be manipulated by users. Flink retains only the n-most-recent checkpoints (n being configurable) while a job is running, and deletes them when a job is cancelled. But you can configure them to be retained instead, in which case you can manually resume from them.
Savepoint – a snapshot triggered manually by a user (or an API call) for some operational purpose, such as a stateful redeploy/upgrade/rescaling operation. Savepoints are always complete, and are optimized for operational flexibility.
How does State Snapshotting Work? #
Flink uses a variant of the Chandy-Lamport algorithm known as asynchronous barrier snapshotting.

When a task manager is instructed by the checkpoint coordinator (part of the job manager) to begin a checkpoint, it has all of the sources record their offsets and insert numbered checkpoint barriers into their streams. These barriers flow through the job graph, indicating the part of the stream before and after each checkpoint.
![image](https://github.com/user-attachments/assets/867de326-7fa0-4da2-9aff-f3a9dcced008)

Checkpoint barriers are inserted into the streams
Checkpoint n will contain the state of each operator that resulted from having consumed every event before checkpoint barrier n, and none of the events after it.

As each operator in the job graph receives one of these barriers, it records its state. Operators with two input streams (such as a CoProcessFunction) perform barrier alignment so that the snapshot will reflect the state resulting from consuming events from both input streams up to (but not past) both barriers.
![image](https://github.com/user-attachments/assets/3ec054ae-67d0-46a4-b653-9742a12bdba7)

Barrier alignment
Flink’s state backends use a copy-on-write mechanism to allow stream processing to continue unimpeded while older versions of the state are being asynchronously snapshotted. Only when the snapshots have been durably persisted will these older versions of the state be garbage collected.

Exactly Once Guarantees #
When things go wrong in a stream processing application, it is possible to have either lost, or duplicated results. With Flink, depending on the choices you make for your application and the cluster you run it on, any of these outcomes is possible:

Flink makes no effort to recover from failures (at most once)
Nothing is lost, but you may experience duplicated results (at least once)
Nothing is lost or duplicated (exactly once)
Given that Flink recovers from faults by rewinding and replaying the source data streams, when the ideal situation is described as exactly once this does not mean that every event will be processed exactly once. Instead, it means that every event will affect the state being managed by Flink exactly once.

Barrier alignment is only needed for providing exactly once guarantees. If you don’t need this, you can gain some performance by configuring Flink to use CheckpointingMode.AT_LEAST_ONCE, which has the effect of disabling barrier alignment.




# FLINK-1.14.5
For the latest information about Flink, please visit our website at:

   https://flink.apache.org

and our GitHub Account 

   https://github.com/apache/flink

If you have any questions, ask on our Mailing lists:

   user@flink.apache.org
   dev@flink.apache.org

This distribution includes cryptographic software.  The country in 
which you currently reside may have restrictions on the import, 
possession, use, and/or re-export to another country, of 
encryption software.  BEFORE using any encryption software, please 
check your country's laws, regulations and policies concerning the
import, possession, or use, and re-export of encryption software, to 
see if this is permitted.  See <http://www.wassenaar.org/> for more
information.

The U.S. Government Department of Commerce, Bureau of Industry and
Security (BIS), has classified this software as Export Commodity 
Control Number (ECCN) 5D002.C.1, which includes information security
software using or performing cryptographic functions with asymmetric
algorithms.  The form and manner of this Apache Software Foundation
distribution makes it eligible for export under the License Exception
ENC Technology Software Unrestricted (TSU) exception (see the BIS 
Export Administration Regulations, Section 740.13) for both object 
code and source code.

