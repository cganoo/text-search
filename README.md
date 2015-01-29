# text-search [![Build Status](https://travis-ci.org/cganoo/text-search.svg?branch=master)](https://travis-ci.org/cganoo/text-search)

An in-memory implementation of relevant text search with configurable options

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
	
### Highlights 

* <b>Application description</b>: A stand-alone java app
* <b>Lifecycle management</b>: [Spring Boot](http://projects.spring.io/spring-boot/) is used along with annotation-based configuration
* <b>User Interface</b>: A CLI is provided where the user can specify search options. File-based input/output is used for data
* <b>Build and Dependency management</b>: Gradle is used for this
* <b>Algoithms used</b>: A custom in-memory exhaustive search algorithm working on indexes (tokenized fragments) is implemented. The app also integrates with Lucene for an alternative algorithm. 
* <b>Unit Tests</b>: A few representative Junit4 unit tests run with Spring are included.

### Future Work

* <b>Caching</b>: Obvious benefits, since a real-life usecase will have docuemnts to search for, available offline for pre-computation. CCould be implemented as file-based indexing in Lucene instead of RAM-based or by integrate with a separate database (easiest would be a hosted RDBMS or DynamoDB from  AWS)
* <b>String-matching variants</b>: The usecase could be tweaked to optimize for search location rather than surrounding fragments a variety of string searching algorithms could be implemented
* <b>Fuzzy matches</b>: The usecase could be tweaked to allow fuzzy matching (e.g. keychain for kitchen)
* <b>Improve Lucene algorithm</b>: Leverage lucene-highlighter to get matching fragments
* <b>Cloud search</b>: Integrate with AWS Cloudsearch or similar offering from Google Cloud Platform (GCP)

### License

text-search is licensed under the MIT license. It is primarily intended for fun and instructive purposes.
Use this at your own risk.

### Credit

The original motivation for text-search came from conversations with some awesome engineers at [ApartmentList.com](https://www.apartmentlist.com).
Source code was developed by the author alone.

### Author

Chaitanya Ganoo  
www.linkedin.com/in/cganoo
