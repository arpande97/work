package com.example.lambdas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

interface CacheIterator {
	boolean hasNext();
	Bookmark next();
}

interface Test {
	void apply();
}

public class Cache {
	private Bookmark[] items;
	private int next = 0;
	
	public Cache(int size) {
		items = new Bookmark[size];
	}
	
	public void add(Bookmark item) {
		if(next < items.length)
			items[next++] = item;
	}
	
	public CacheIterator iterator() {
		return new MyCacheIterator();
	}
	
	private class MyCacheIterator implements CacheIterator {
		private int i = 0;

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return i < items.length;
		}

		@Override
		public Bookmark next() {
			// TODO Auto-generated method stub
			return items[i++];
		}
	}
	
	public static void main(String[] args) {
		Cache recommendedItems = new Cache(5);
		
		Bookmark item1 = new Bookmark();
		item1.setId(2000);
		item1.setTitle("Use Final Liberally");
		item1.setRating(1.0);
		
		Bookmark item2 = new Bookmark();
		item2.setId(2001);
		item2.setTitle("How do I import a pre-existing Java project into Eclipse");
		item2.setRating(2.0);
		
		Bookmark item3 = new Bookmark();
		item3.setId(2002);
		item3.setTitle("Interface vs Abstract Classes");
		item3.setRating(3);
		
		Bookmark item4 = new Bookmark();
		item4.setId(2003);
		item4.setTitle("NIO tutorial by Greg Travis");
		item4.setRating(4.0);
		
		Bookmark item5 = new Bookmark();
		item5.setId(2004);
		item5.setTitle("Virtual Hosting and Tomcat");
		item5.setRating(5.0);
		
		recommendedItems.add(item1);
		recommendedItems.add(item2);
		recommendedItems.add(item3);
		recommendedItems.add(item4);
		recommendedItems.add(item5);
		
		CacheIterator iterator = recommendedItems.iterator();
		
		while(iterator.hasNext()) {
			System.out.println(iterator.next().getTitle());
		}
		
		Arrays.sort(recommendedItems.items, new Comparator<Bookmark>() {

			@Override
			public int compare(Bookmark o1, Bookmark o2) {
				// TODO Auto-generated method stub
				return o1.getRating() < o2.getRating() ? 1 : -1;
			}
			
		});
		System.out.println("\nSorted by rating...(using anonymous class)\n");
		iterator = recommendedItems.iterator();
		
		while(iterator.hasNext()) {
			System.out.println(iterator.next().getTitle());
		}
		
		Arrays.sort(recommendedItems.items, (a, b) -> a.getTitle().length() - b.getTitle().length());
		
		System.out.println("\n Sorted by length \n....");
		
		iterator = recommendedItems.iterator();
		
		while(iterator.hasNext()) {
			System.out.println(iterator.next().getTitle());
		}
		
//		new Cache(5).go(() -> System.out.println("\nLambda"));
	}
	static int global = 0;
	public void go(Test test) {
		test.apply();
		
		int count = 0;
		
		List<Integer> trick = new ArrayList<>();
		
		//capturing the local variables
		//closure
		//can't do something like count++ because count in the enclosing scope should be final or effectively final
		for (int i = 0; i < 5; i++) {
			
			//int count = 0;
			//local variables can't be shadowed
			new Thread(() -> System.out.println(count)).start();
			
			//incrementing global is fine because global doesn't need to be effectively final but this is also not recommended
			new Thread(() -> System.out.println(global++)).start();
			
			new Thread(() -> {
				trick.add(count);
				int temp = trick.get(0);
				trick.set(0, temp++);
			}).start();
		}
		//count++;
		
		//imaging if go() finished its execution and is popped off the stack, we would not have the value of count in threads inside for loop that are 
		//still running
	}
}
