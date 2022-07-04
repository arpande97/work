package com.example.lambdas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class StreamOperations {
	
	static class Book implements Comparable{		
		private long isbn;
		private String title;	
		private double rating;
		private double price;
		private String source;
		
		public Book(long isbn, String title, double rating, double price, String source) {
			this.isbn = isbn;
			this.title = title;
			this.rating = rating;
			this.price = price;
			this.source = source;
		}
		
		public long getIsbn() {
			return isbn;
		}
		
		public String getTitle() {
			return title;
		}
		
		public double getRating() {
			return rating;
		}
		
		public double getPrice() {
			return price;
		}
		
		public String getSource() {
			return source;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (isbn ^ (isbn >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Book other = (Book) obj;
			if (isbn != other.isbn)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Book [isbn=" + isbn + ", title=" + title + ", rating=" + rating + ", price=" + price + ", source="
					+ source + "]";
		}

		@Override
		public int compareTo(Object o) {
			// TODO Auto-generated method stub
			return Long.valueOf(isbn).compareTo(((Book)o).getIsbn());
		}
		
	}	
	
	// Print at most 5 DISTINCT books with rating >= 4.5
	// DB world: select distinct (ISBN) from book where rating >= 4.5 limit 0, 5;
	private static void slice(List<Book> books) {
		System.out.println("\nSlice ... ");
		
		List<String> result = books.stream()
		     .filter(d -> d.getRating() >= 4.5)
		     .distinct()
		     .limit(5) //limit is the short circuit execution
		     //.skip(5)
		     //.peek(d -> System.out.println(d.getTitle())
		     .map(d -> d.getTitle())
		     //.forEach(System.out::println);
		     .collect(Collectors.toList());
		
		for(String title: result)
			System.out.println(title + " ");
	}
	
	private static void match(List<Book> books) {
		System.out.println("\nMatching...");
		
		//looking for inexpensive and highly rated books
		
		boolean anyMatch = books.stream()
		     .anyMatch(d -> d.getRating() >= 4.8 && d.getPrice() <= 50.0);
		
		boolean allMatch = books.stream()
			     .allMatch(d -> d.getRating() >= 4.8);
		
		boolean noneMatch = books.stream()
				                 .noneMatch(d -> d.getRating() <= 2.0);
		
		System.out.println("anyMatch? " + anyMatch);
		System.out.println("allMatch? " + allMatch);
		System.out.println("noneMatch? " + noneMatch);
		
	}
	
	private static void find(List<Book> books) {
		//typically parameterless
		//used with filter operation
		
		System.out.println("\nFinding...");
		
		//optional is a container class
		//might contain something or null
		
//		 books.stream()
//		     .filter(d -> d.getRating() >= 4.8 && d.getPrice() <= 50)
//		     .findAny().ifPresent(System.out::println);
		
		Optional<Book> result2 = books.stream()
	     .filter(d -> d.getRating() >= 4.8 && d.getPrice() <= 50)
	     .findFirst();
		
		System.out.println(result2.get());
//		
//		if(result1.isPresent())
//			System.out.println(result1.get());
//		else System.out.println("No such book found!!");
		 
//		 Optional<Book> opt = Optional.of(books.get(0));
//		 System.out.println(opt.get());
		
		
		
	}
	
	private static void reduce(List<Book> books) {
		System.out.println("\nReduce...");
		
		books.stream()
		     .filter(b -> b.getRating() >= 4.5)
		     .reduce((b1, b2) -> b1.getPrice() <= b2.getPrice() ? b1 : b2)
		     .ifPresent(b -> System.out.println("lowest Priced book " + b));
	}
	
	private static void reduceImperatively(List<Book> books) {
		System.out.println("\nReduce Imperatively..");
		
		Book result = null;
		for(Book book: books) {
			if(result == null) {
				if(book.getRating() >= 4.5) result = book;
				continue;
			}
			if(book.getRating() >= 4.5 && result.getPrice() > book.getPrice()) {
				result = book;
			}
		}
		
		System.out.println("(imperative) Lowest priced book" + result);
	}
	
	private static void overloadedReductions() {
		System.out.println("\noverloadedReductions...");
		
		String[] grades = {"a", "b", "b"};
		
		//Stream API: Design principle
		//API does not differentiate b/w sequential and parallel streams
		String concat1 = Arrays.stream(grades)
		      .reduce("", (s1, s2) -> s1 + s2);
		System.out.println("concat1: " + concat1);
		
		
		
		//we shouldn't have done the following as we are using mutable reduction with reduce()
		//single instance of container(StringBuillder) is used +
		//              SB is not thread-safe +
		//              combiner redundantly combines
		StringBuilder concat2 = Arrays.stream(grades)
				               .reduce(new StringBuilder(), (sb, s) -> sb.append(s),
				            		   (sb1, sb2) -> sb1.append(sb2));
		System.out.println("concat2: " + concat2);
		
		
		
		
	}
	
	private static void collectToCollection(List<Book> books) {
		System.out.println("\nIn collect to collection");
		
		List<Book> list1 = books.stream()
		     .filter(b -> b.getRating() >= 4.5)
		     .distinct()
		     .collect(Collectors.toList());
		
		System.out.println("ListSize: " + list1.size());
		
		List<Book> list2 = books.stream()
				                .filter(b -> b.getRating() >= 4.5)
				                .distinct()
				                .collect(ArrayList::new,//() -> new ArrayList<>(), 
				                		(a, t) -> a.add(t),
				                		(a1, a2) -> a1.addAll(a2));
		System.out.println("list2 size: " + list2.size());
		
		Set<Book> set1 = books.stream()    
				              .filter(b -> b.getRating() >= 4.5)
				              .collect(Collectors.toSet());
		
		System.out.println("set1 size: " + set1.size());
		
		TreeSet<Book> set2 = books.stream() 
				                  .filter(b -> b.getRating() >= 4.5)
				                  .collect(Collectors.toCollection(() -> new TreeSet<>()));
		System.out.println("set2 size: " + set2.size());
		
		
	}
	
	private static void mutableReduction() {
		System.out.println("\nMutable Reduction...");
		String[] grades = {"A", "A", "B"};
		
		StringBuilder concat2 = Arrays.stream(grades).parallel()
				                      .collect(() -> new StringBuilder(),
				                    		  (sb, s) -> sb.append(s),
				                    		  (sb1, sb2) -> sb1.append(sb2));
		
		System.out.println(concat2);
		
		//another way to do this but with reduce
		//this is not efficient as new SB is being spawned every time
//		StringBuilder concat3 = Arrays.stream(grades)
//				                      .reduce(new StringBuilder(),
//				                    		  (sb, s) -> new StringBuilder().append(sb).append(s),
//				                    		  (sb1, sb2) -> sb1.append(sb2));
		
		String concatWithJoining = Arrays.stream(grades).parallel()
				                         .collect(Collectors.joining());
		System.out.println(concatWithJoining);
	}
	
	private static void collectToMap(List<Book> books) {
		//group by clause in SQL
		
		Map<Long, Book> map = books.stream()
//		     .collect(Collectors.toMap(Book::getIsbn//b -> b.getIsbn(), Function.identity()//b -> b));
			 .collect(Collectors.toMap(b -> b.getIsbn(), b -> b, (b1, b2) -> b1.getPrice() <= b2.getPrice() ? b1 : b2));
//		for(Entry<Long, Book> entry : map.entrySet()) {
//			
//		}
		
		
		System.out.println(map instanceof HashMap);
		
		Map<Long, Book> treemap = books.stream()
//			     .collect(Collectors.toMap(Book::getIsbn//b -> b.getIsbn(), Function.identity()//b -> b));
				 .collect(Collectors.toMap(b -> b.getIsbn(), b -> b, (b1, b2) -> b1.getPrice() <= b2.getPrice() ? b1 : b2, () -> new TreeMap<>()));
		
		
		Map<Double, List<Book>> ratingsMap = treemap.values().stream()
		       .collect(Collectors.toMap(Book::getRating, b -> Collections.singletonList(b), (l1, l2) -> {ArrayList<Book> l = new ArrayList<>();
		       l.addAll(l2);
		       return l;}));
		
		
	}
	
	public static void main(String[] args) {
		
		List<Book> books = new ArrayList<>();
		
		books.addAll(DataExtractor.getFromAmazon("java"));
		books.addAll(DataExtractor.getFromBarnesAndNoble("java"));
		
		//All matching and finding operations + limit are short circuit operations (recall &&, ||)
		
		
//		slice(books);	
//		match(books);
		find(books);
//		reduce(books);
//		reduceImperatively(books);
//		overloadedReductions();
//		String version = System.getProperty("java.runtime.version");
//		System.out.println(version);
//		mutableReduction();
//		collectToCollection(books);
//		collectToMap(books);
		List<String> list = Arrays.asList("shubhankur", "archit");
		
		
		//fp/declarative
//		Collections.sort(list, (o1, o2) -> o1.length() - o2.length());
//		
//		
//		//imperative
//		Collections.sort(list, new Comparator<String>() {
//
//			@Override
//			public int compare(String o1, String o2) {
//				// TODO Auto-generated method stub
//				return o1.length() - o2.length();
//			}
//			
//		});
		
		//fp depends on functional interfaces
		
		//Runnable void run()
		//Callable T call()
		//Comparator int compare()
//		Predicate<String> p = (t) -> t.length() < 10 && t.charAt(0) == 'a';
//		
//		 Predicate<String> p2 = new Predicate<String>() {
//
//			@Override
//			public boolean test(String t) {
//				// TODO Auto-generated method stub
//				return t.length() < 3 && t.charAt(0) == 'a';
//			}
//			 
//		 };
//		 System.out.println(p2.test("archit"));
		//Predicate<T> boolean test(T t)
		//Consumer<T> void accept(T t)
		//BiConsumer<T, U> void accept(T t, U u);
//		Consumer<String> c = (s) -> System.out.println(s);
//		c.accept("archit");
		HashSet<String> sets = new HashSet<>();
		sets.add("archit");
		sets.add("shubhankur");
		sets.add("jay");
		
//		for(String s : sets) {
//			System.out.println(s);
//		}
		
//		sets.forEach((s) -> System.out.println(s));
//		HashMap<String, Integer> maps = new HashMap<>();
//		maps.put("archit", 1);
//		maps.put("shubhankur", 2);
//		maps.put("chits", 3);
//		BiConsumer<String, Integer> biConsumer = (k, v) -> System.out.println(k + " " + v);
//		for(String s : maps.keySet()) {
//			biConsumer.accept(s, maps.get(s));
//		}
//		
//		maps.forEach((k, v) -> System.out.println("key: " + k + " Value: " + v));
		
		//Function<T, R> R apply(T t);
//		Function<Integer, Integer> func = (num) -> num - 3;
//		System.out.println(func.apply(6));
		//BiFunction<T, U, R> 
//		BiFunction<String, String, Integer> b = (s, t) -> s.length() + t.length();
//		System.out.println(biFunc.apply("archit", "shubhankur"));
		
//		Supplier<String> supp = () -> new String("archit");
//		
//		System.out.println(supp.get());
		
		//what are functional interfaces?
		//lambdas (T, U, R) -> {}
		//                  -> T.length()
		//Predicate, Consumer, BiConsumer, Supplier, Function, BinaryOperator, BiFunction
		
		
//		List<Integer> someList = Arrays.asList(1, 2, 4, 64, 534, 6, 534, 534);
//		List<Integer> filteredList = someList.stream()
//		        .filter((a) -> a > 5)
//		        .distinct()
//		        .collect(Collectors.toList());
//		for(int num : filteredList) System.out.println(num);
		
		//stream()
		//filter()
		//map()
		//reduce()
		//distinct()
		//collect()
		//Collectors.toList()
		//collect(Collectors.toList()) --> list
		//collect(Collectors.toMap()) --> hashmap
		//reduce("", (a, b) -> a + b) --> string
		
	}
	
}
