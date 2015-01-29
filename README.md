# text-search [![Build Status](https://travis-ci.org/cganoo/text-search.svg?branch=master)](https://travis-ci.org/cganoo/text-search)

An in-memory implementation of relevant text search

## How to use this?

### Satisfy Dependencies

Following are essential:

* [Git](http://git-scm.com/downloads)
* [Java SE 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)

It is recommended to also use the following for experimenting with the source code:

* [Gradle](https://gradle.org/)
* [IntelliJ](https://www.jetbrains.com/idea/)
	
### Get the source and artifacts from Github

1. `git clone https://github.com/cganoo/text-search.git`
2. `cd text-search/`

### Choose Command Line Options that you want

* `-help` : <b>Display usage</b>
* `-search searchEngine highlightStrategy document query` : <b>find relevant match in the document for the query by using the  searchEngine and the highlightStrategy</b>

* `searchEngine` can take values <b>default, lucene</b>
* `highlightStrategy` can take values <b>distinct, interpolated</b>
* `document` and `query` must point to text files on disk

### Usage Example

<b> NOTE:</b> 
* The pre-compiled jar is available at `text-search/out/artifacts/text_search_jar/text-search.jar`
* Sample textual data files are available at `text-search/data`
* Generated output is written back to `text-search/data` and includes a timestamp in the filename for convenience

<b>Navigate to the source directory and run the desired command.</b>

	$ cd text-search 
	$ java -cp "./out/artifacts/text_search_jar/text-search.jar" textsearch.Application -search lucene interpolated ./data/document.txt ./data/query.txt
	
<b>Spring Boot will manage the application lifecycle and emit a bunch of logs</b>. <i>For example, for the above command:</i>
	
	textsearch.api.Search: Tokenizing specified document ...
	textsearch.api.Search: Adjusting textsearch.search strategy based on the specified searchEngine ...
	textsearch.api.Search: Generating relevant match using lucene ...
	textsearch.api.Search: Lucene Logs: textsearch.search hitCount: 2
	textsearch.api.Search: Hits for queryString [designer kitchen] found!
	textsearch.api.Search: Lucene Logs: id: 3, content: Premium stainless steel designer appliances;, score:0.3484565
	textsearch.api.Search: Lucene Logs: id: 5, content: A kitchen that most chefs would drool over with easy to clean gas stove and countertops;, score:0.199118
	textsearch.api.Search: Lucene Logs: doc ids = [3, 5]
	
<b>View the generated output</b>
	
	$ cat ./data/lucene_interpolated_11_25_17.txt
	##### SearchEngine #####
	lucene

	##### Highlight Strategy #####
	interpolated

	##### Document #####
	Our luxury loft-style apartments were constructed as condominiums, so your new residence will have: Solid floors and walls (this will be the quietest apartment you've EVER lived in); Premium stainless steel designer appliances; Distinctive accent walls and hardwood flooring; A kitchen that most chefs would drool over with easy to clean gas stove and countertops; Walk in closets with built in storage; Full size washer and dryer in each apartment home. In addition, all residents will enjoy use of our top-notch amenities, including reserved parking, cutting-edge fitness center, wireless internet cafe/business center, and rooftop lounge to soak up the sun!

	##### Query #####
	designer kitchen

	##### Relevant Match #####
	Premium stainless steel designer appliances; Distinctive accent walls and hardwood flooring; A kitchen that most chefs would drool over with easy to clean gas stove and countertops;
	
	

### Architecture

### License

text-search is licensed under the MIT license. It is primarily intended for fun and instructive purposes.
Use this at your own risk.

### Credit

The original motivation for text-search came from conversations with some awesome engineers at [ApartmentList.com](https://www.apartmentlist.com).
Source code was developed by the author alone.

### Author

Chaitanya Ganoo  
www.linkedin.com/in/cganoo
