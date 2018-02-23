# Custom Collector vs Reducing Collector

Given a list of `Routes` that contain a link to a `Campaign` where only a single route can be associated with a campaign, 
we want to generate a `Map` from the unique campaign `id` to the unique route associated with that campaign. 

We do that in 2 ways: 1) using a custom collector for the `groupingBy` collector; and 2) using the `reducing` collector 
for the `groupingBy` collector.

## Requires

- Java 8

----

Author: <henry.g.brown@gmail.com>