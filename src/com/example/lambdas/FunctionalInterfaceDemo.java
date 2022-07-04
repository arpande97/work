package com.example.lambdas;

import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.htmlcleaner.HtmlCleaner;

/**
 * Demonstrates:
 * 
 *  Function & Predicate (including Chaining & Compose)
 *  Bi-Function as an alternate to Predicate
 *    
 *  Consumer & Supplier
 *  Show API too (including primitive functional interfaces) !!
 *  
 *  Method References
 *  TODO: Constructor References
 * 
 *
 */
//FP can aid in taking care of mutabiltiy of objects
//Pure Functions : Referential transparency & zero side effects
//an example of function that is not referentially transparent:
//        const multiplier = 2;
//        function multiply(x) { return x * multiplier; }
public class FunctionalInterfaceDemo {
	
	public static void main(String[] args) {
		
		String doc1 = "<html><body>One of the most common uses of <i>streams</i> is to represent queries over data in collections</body></html>";
		String doc2 = "<html><body>Information integration systems provide valuable services to users by integrating information from a number of autonomous, heterogeneous and distributed Web sources</body></html>";
		String doc3 = "<html><body>Solr is the popular, blazing fast open source enterprise search platform from the Apache Lucene</body></html>";
		String doc4 = "<html><body>Java 8 goes one more step ahead and has developed a streams API which lets us think about parallelism</body></html>";
		
		List<String> documents = new ArrayList<>(Arrays.asList(doc1, doc2, doc3, doc4));
		
		List<String> targetDocuments = new ArrayList<>();
		Level level;
		
		for (String doc : documents) {
			boolean isTargetDoc = filter(doc, d -> d.contains("stream"));
			BiFunction<String, String, Boolean> biFunction = (d, c) -> d.contains(c);
			
			// (3) Method References (ClassName::instanceMethod)
			//BiFunction<String, String, Boolean> biFunction = String::contains;
			
//			Function<String, Boolean> function = doc::contains;
//			if(function.apply("stream"))
			//Remember: lambda functions provide the body of functional interfaces.
			
//			if(isTargetDoc) {
//				doc = transform(doc, d -> Indexer.stripHtmlTags(d));
//				doc = transform(doc, d -> Indexer.removeStopwords(d));
//				
////				Function<String, String> htmlCleaner = d -> Indexer.stripHtmlTags(d);
////				doc = transform(doc, htmlCleaner);
////				
////				Function<String, String> stopWordRemover = d -> Indexer.removeStopwords(d);
////				doc = transform(doc, stopWordRemover);
////				stopWordRemover.apply(doc);
//				
//				
//			}
			
//			BiFunction<String, String, Boolean> biFunction = (d, c) -> d.contains(c);
//			if(biFunction.apply(doc, "stream")) {
//				UnaryOperator<String> htmlCleaner = d -> Indexer.stripHtmlTags(d);
//				doc = transform(doc, htmlCleaner);
//				
//				UnaryOperator<String> stopWordRemover = d -> Indexer.removeStopwords(d);
//				doc = transform(doc, stopWordRemover);
//				
//				System.out.println(doc);
//				
//				
//			}
			
			//composed Functions
			
//			if(biFunction.apply(doc, "stream")) {
//				Function<String, String> htmlCleaner = d -> Indexer.stripHtmlTags(d);
//				Function<String, String> stopWordRemover = d -> Indexer.removeStopwords(d);
//				
//				Function<String, String> docProcessor = htmlCleaner.andThen(stopWordRemover);
//				doc = transform(doc, docProcessor);
//				
////				System.out.println(doc);
//				
//				targetDocuments.add(doc);
//			}

			//next part of the code is for method references
			if(biFunction.apply(doc, "stream")) {
				Function<String, String> htmlCleaner = d -> Indexer.stripHtmlTags(d);
				// (1) Method References (ClassName::staticMethod)
				Function<String, String> stopWordRemover = Indexer::removeStopwords;
				
				doc = transform(doc, htmlCleaner);
				doc = transform(doc, stopWordRemover);
				
				targetDocuments.add(doc);
				
				
				//Constructor references
				Supplier<String> supplier = String::new; //amounts to () -> new String();
				System.out.println("\nsupplier.get: " + supplier.get());
				
				Function<String, String> function = String::new; //amounts to s -> new String(s);
				System.out.println("\nfunction.apply: " + function.apply("java"));
				
				BiFunction<Integer, Float, HashMap> bifunction = HashMap::new; // amounts to (c, lf) -> new HashMap(c, lf)
				System.out.println("\nbiFunction.apply: " + bifunction.apply(100, 0.75f));
			}
		}
		//Consumer<T> has one method. void accept(T t);
//		Consumer<String> consume = d -> System.out.println(d);
//		targetDocuments.forEach(consume);
		
		//forEach takes in a consumer<T> as an arg
//		targetDocuments.forEach(d -> System.out.println(d));
		
		// (2) Method References (objectRef::instanceMethod)
		targetDocuments.forEach(System.out::println);
		
		
		for(String doc : targetDocuments) {
			try {
				if(doc.length() > 80) {
					throw new Exception("oversized document");
				}
			} catch (Exception e) {
				
				//deferred execution
				//you are printing it instead of using sout, but print has supplier as its argument, so only if supplier.get() gets called you'll get the print 
				//statement. This is known as deferred execution.
				print(() -> e.getMessage() + " ~ " + doc);
			}
		}
		
		
	}	
	private static boolean errorFlag = true;
	static void print(Supplier<String> supplier) {
		if(errorFlag) {
			System.out.println(supplier.get());
		}
	}
	
	static boolean filter(String doc, Predicate<String> filter) {
		return filter.test(doc);
	}
	
	static String transform(String doc, UnaryOperator<String> transformer) {
		return transformer.apply(doc);
	}
	static String transform(String doc, Function<String, String> transformer) {
		return transformer.apply(doc);
	}
	
	
	
}

class Indexer {
	
	private static List<String> stopWords = Arrays.asList("of", "the", "a", "is", "to", "in", "and");
	
	static String stripHtmlTags(String doc) {
		return new HtmlCleaner().clean(doc).getText().toString();
	}
	
	static String removeStopwords(String doc) {
		
		StringBuilder sb = new StringBuilder();
		for (String word : doc.split(" ")) {
			if (!stopWords.contains(word))
				sb.append(word).append(" ");
		}
		
		return sb.toString();
	}	
	
}

