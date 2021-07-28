# Leveraging Parallel Streams for Fast Data Processing in Java.

Start day 2021-26-07 <br>
Finish day

## Module One: Introduction Parallelism in the Java Stream API

- Stream API introduced in 2014 in Java 8.
- Is based on lambda expressions and aggregation functions.
- Apply the parallelism.
- You can run a Stream in Java in **parallel** or **sequentially**
- If you execute a for loop and do not use its result, the JVM will not execute
  the for loop of the code.

**JMH Library**

A Java framework that helps to measure the performance of our applications using the JVM.

**Benchmark**

Is a technique used to measure the performance of a system or one of its components.

## Module Two

### How can get the best performances

**Auto-Boxing:** Is when we use classes that are defined as wrappers(Integer, Boolean, 
Double, etc.) in our Java code, because the JVM at the end uses the **boxing** process 
to convert those classes into primary variables since the JVM only operates with these 
values.

**Pointer Chasing** <br>
How does a CPU core access data from memory?<br>
- Data first pass through all levels of cache L1, L2, L3 before passing to the core.
- The memory is being transfer line by line from the main memory to the different 
  cache levels.
- Each line usually has 64 bits of memory.
- To obtain the values we have to follow the pointers that are stores in memory, this 
  knows as pointer chasing.

#### Tips

- If you have performance problems in your java application, before using parallel 
  operations you should see if you have code that uses boxing and unboxing and try to 
  eliminate it.
  
## Module Three: Analyzing the Fork/Join Implementation of Parallel Streams

- There can be only one ForkJoinPool in each JVM.
- Created when the JVM is created.
- That is used for all the Parallel Streams
- The size is being setting by the number of virtual cores.

**How does the ForkJoinPool framework work?** <br>

Let's assume that we have a PC that has 4 cores (c1, c2, c3, c4) then we will execute 
a task in the main core c1 in turn this task will be divided in two more tasks t2, t3 
so the main task will enter in standby mode until the 2 subtasks finish their 
execution, now let's suppose that the tasks t2 and t3 are divided each one in two 
subtasks more we would have t4, t5, t6, t7 tasks in queue the ForkJoinPool framework 
will execute what is known as work-stealing that disperses in automatic all the 
subtasks in all cores of the PC, so the tasks can be executed in parallel and obtain 
the result faster. 

- You should measure the performance of your application to see if it better to use 
  sequential or parallel operations.
- For operations with little data to be processed it is better to use sequential 
  operations.
- For operations with big data to be processed it is better to use parallel operations.  


**Avoiding the use Reduction**<br>
- If we have non-associative operations it is not recommended using the framework 
  ForkJoinPool because of posible errors.

> The provided reduction operation is used to join partial results and has to be 
> associative.

**Parallelism is not suited for any kind of computations**<br>
