# StringSearch
## High-performance pattern matching algorithms in Java

The Java language lacks fast string searching algorithms. StringSearch provides implementations of the Boyer-Moore and the Shift-Or (bit-parallel) algorithms. These algorithms are easily five to ten times faster than the naïve implementation found in `java.lang.String`.

## Download

[Download StringSearch 2.2 (JAR)](http://repo.eaio.com/maven2/com/eaio/stringsearch/stringsearch/2.2/stringsearch-2.2.jar)

[Download StringSearch 2.2 (Source JAR)](http://repo.eaio.com/maven2/com/eaio/stringsearch/stringsearch/2.2/stringsearch-2.2-sources.jar)

Or get StringSearch through Maven:

```XML
	<dependencies>
		<dependency>
			<groupId>com.eaio.stringsearch</groupId>
			<artifactId>stringsearch</artifactId>
			<version>2.2</version>
		</dependency>
	</dependencies>
…
	<repositories>
		<repository>
			<id>eaio.com</id>
			<url>http://repo.eaio.com/maven2</url>
		</repository>
	</repositories>
```

### StringSearch 1.2

StringSearch 1.2, which includes a native library and a different selection of algorithms, is still available.

[Download StringSearch 1.2 (ZIP)](http://johannburkard.de/software/stringsearch/stringsearch-1.2.zip)

[Download StringSearch 1.2 (TAR.GZ)](http://johannburkard.de/software/stringsearch/stringsearch-1.2.tar.gz)

## Documentation

This library contains implementations of the following pattern matching algorithms:

* General purpose
	* [BNDM](http://johannburkard.de/software/stringsearch/site/apidocs/com/eaio/stringsearch/BNDM.html)
	* [BoyerMooreHorspool](http://johannburkard.de/software/stringsearch/site/apidocs/com/eaio/stringsearch/BoyerMooreHorspool.html)
	* [BoyerMooreHorspoolRaita](http://johannburkard.de/software/stringsearch/site/apidocs/com/eaio/stringsearch/BoyerMooreHorspoolRaita.html)
* Searching with wildcards (don't-care-symbols)
	* [BNDMWildcards](http://johannburkard.de/software/stringsearch/site/apidocs/com/eaio/stringsearch/BNDMWildcards.html)
* Searching with mismatches
	* [ShiftOrMismatches](http://johannburkard.de/software/stringsearch/site/apidocs/com/eaio/stringsearch/ShiftOrMismatches.html)
* Case-insensitive searching
	* [BNDMCI](http://johannburkard.de/software/stringsearch/site/apidocs/com/eaio/stringsearch/BNDMCI.html)
* Case-insensitive searching with wildcards (don't-care-symbols)
	* [BNDMWildcardsCI](http://johannburkard.de/software/stringsearch/site/apidocs/com/eaio/stringsearch/BNDMWildcardsCI.html)

## License

StringSearch is licensed under the [MIT License](http://johannburkard.de/software/stringsearch/copying.txt)  ([OSI certified](http://opensource.org/licenses/mit-license.php)).

## Other Resources

* [StringSearch on johannburkard.de](http://johannburkard.de/software/stringsearch/)
* [Maven-generated Site](http://johannburkard.de/software/stringsearch/site/)
* [APIdoc](http://johannburkard.de/software/stringsearch/site/apidocs/)
