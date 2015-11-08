package org.example.java8;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/*
 * JAVA 8 FEATURES
 */
public class TestJava8 {

	public static void testJava8() {
		// TEST JAVA 8

		// An instance of a functional interface using a lambda expression
		TestInterface tester = () -> System.out.println("Java SE 8 is working!");
		tester.test();

		TestInterfaceSecond test2  = (v1, v2) -> {
			System.out.println("Hello test 2 interface, arg1 = " + v1 + ", arg2 = "+v2);
		};

		test2.test2(1, "cat");
	}

	public static void replacingInterfaces() {
		// REPLACING INTERFACES (FOR RUNNABLE)

		Runnable r1 = new Runnable() {

			@Override
			public void run() {
				System.out.println("Running thread 1");
			}
		};

		Runnable r2 = new Runnable() {

			@Override
			public void run() {
				System.out.println("Running thread 2");
			}
		};
		new Thread(r1).start();
		new Thread(r2).start();
	}

	public static void replacingInterfacesWithLambdas() {
		Runnable r1 = () -> {
			try {
				Thread.sleep(1000);	// Adding sleep
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Running thread 1");
		};
		Runnable r2 = () -> System.out.println("Running thread 2");
		new Thread(r1).start();
		new Thread(r2).start();
	}

	public static void builtInFunctionalInterfaces() {
		// USING BUILT-IN FUNCTIONAL INTERFACES WITH LAMBDAS

		List<String> strings = new ArrayList<>();
		strings.add("CCC");
		strings.add("DDD");
		strings.add("AAA");
		strings.add("BBB");
		strings.add("EEE");

		/*
		// Simple case-sensitive sort operation
		Collections.sort(strings);;
		System.out.println("After sorting:");
		for(String str : strings) {
			System.out.println(str);
		}

		// Case-insensitive sort operation using anonymous inner-class
		Collections.sort(strings, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});

		// Case-insensitive sort operation using Lambdas
		Comparator<String> comp = (str1, str2) -> {
			return str1.compareToIgnoreCase(str2);
		};
		Collections.sort(strings, comp);
		 */

		// Shorter: Case-insensitive sort operation using Lambdas

		Collections.sort(strings, (str1, str2) -> { return str1.compareToIgnoreCase(str2); });
		System.out.println("After sorting:");
		//				for(String str : strings) System.out.println(str);
		strings.forEach(str -> System.out.println(str));
	}

	public static void filteringCollectionsWithPredicateInterfaces(List<Person> people) {
		Predicate<Person> pred = new Predicate<Person>() {

			@Override
			public boolean test(Person p) {
				return (p.getAge() >= 65);
			}
		};

		for (Person person : people) {
			if(pred.test(person)) {
				System.out.println(person.toString());
			}
		}
	}
	
	public static void filteringCollectionsWithPredicateInterfacesLambdas(List<Person> people) {
		
		Predicate<Person> pred = (Person p) -> { return p.getAge() >= 65; };
		Predicate<Person> predYounger = (Person p) -> { return p.getAge() < 65; };
		displayPeople(people, pred);
		displayPeople(people, predYounger);
	}
	private static void displayPeople(List<Person> people, Predicate<Person> pred) {
		people.forEach(person -> { 
			if(pred.test(person)) {
				System.out.println(person.toString());
			}
		});
	}
	
	public static void staticMethodReferences(List<Person> people) {
		System.out.println("=== start: staticMethodReferences ===");
		people.forEach(p -> System.out.println(p));
		
		Collections.sort(people, Person::compareAges);
		
		people.forEach(p -> System.out.println(p));
		System.out.println("=== end: staticMethodReferences ===");
	}
	
//	public static void instanceMethodReferences(List<Person> people) {
//		System.out.println("=== start: staticMethodReferences ===");
//		people.forEach(p -> System.out.println(p));
//		
//		Collections.sort(people, this::compareAges);	// Doesn't seem to work!!!
//		
//		people.forEach(p -> System.out.println(p));
//		System.out.println("=== end: staticMethodReferences ===");
//	}
//	private int compareAges(Person a1, Person a2) {
//		Integer age1 = a1.getAge();
//		return age1.compareTo(a2.getAge());
//	}
	
	public static void implDefaultMethodsInInterfaces(List<Person> people) {
		System.out.println("=== start: implDefaultMethodsInInterfaces ===");
		Predicate<Person> pred = (Person p) -> { return p.getAge() >= 65; };
		Predicate<Person> predYounger = (Person p) -> { return p.getAge() < 65; };
		displayPeopleDefaultMethods(people, pred);
		displayPeopleDefaultMethods(people, predYounger);
		System.out.println("=== end: implDefaultMethodsInInterfaces ===");
	}
	private static void displayPeopleDefaultMethods(List<Person> people, Predicate<Person> pred) {
		people.forEach(person -> { 
			if(pred.test(person)) {
				System.out.println(person.getPersonInfo());	// Using Default Methods In Interfaces!!!
//				System.out.println(PersonInterfaces.getPersonInfo(person));	// Using static Methods In Interfaces!!!
			}
		});
	}
	
	public static void traversingCollectionsWithStreams(List<Person> people) {
		System.out.println("=== start: traversingCollectionsWithStreams ===");
		
		Predicate<Person> pred = (Person p) -> { return p.getAge() >= 65; };
//		Predicate<Person> predYounger = (Person p) -> { return p.getAge() < 65; };
		people.stream()
			.filter(pred)
			.forEach(p -> System.out.println(p.getName()));
		
		System.out.println("=== end: traversingCollectionsWithStreams ===");
	}
	
	public static void traversingCollectionsWithParallelStreams(List<Person> people) {
		System.out.println("=== start: traversingCollectionsWithStreams ===");
		
		Predicate<Person> pred = (Person p) -> { return p.getAge() >= 65; };
//		Predicate<Person> predYounger = (Person p) -> { return p.getAge() < 65; };
		
		people.parallelStream()
			.filter(pred)
			.forEach(p -> System.out.println(p.getName()));
		
//		people.stream()
//			.parallel()
//			.filter(pred)
//			.forEach(p -> System.out.println(p.getName()));
		
		System.out.println("=== end: traversingCollectionsWithStreams ===");
	}
	
	public static void creatingStreamsFromCollectionsAndArrays() {
		System.out.println("=== start: creatingStreamsFromCollectionsAndArrays ===");
		
		Person[] people = {
				new Person("Amanda", 73),
				new Person("Kate", 12),
				new Person("Rafael", 44)
		};
		
		Stream<Person> stream = Arrays.stream(people);
//		Stream<Person> stream = Stream.of(people);
		
		stream.forEach(p -> System.out.println(p.getPersonInfo()));
		
		System.out.println("=== end: creatingStreamsFromCollectionsAndArrays ===");
	}
	
	public static void creatingParallelStreamsFromCollectionsAndArrays() {
		System.out.println("=== start: creatingParallelStreamsFromCollectionsAndArrays ===");
		
		System.out.println("Creating list...");
		List<String> strings = new ArrayList<>();
		for(int i=0; i<100; i++) {
			strings.add("Item " + i);
		}
		
		strings.stream()
			.parallel()	// parallizing among multiple processors. Note -- not sequential, but really efficient !!!
			.forEach(str -> System.out.println(str));
		
		System.out.println("=== end: creatingParallelStreamsFromCollectionsAndArrays ===");
	}
	
	public static void aggregatingStreamValues(List<Person> people) {
		System.out.println("=== start: aggregatingStreamValues ===");
		
		System.out.println("Creating list...");
		List<String> strings = new ArrayList<>();
		for(int i=0; i<10000; i++) {
			strings.add("Item " + i);
		}
		
		// COUNT
		long numItems = strings.stream()
//							.parallel()	// slower than sequential streams for simple collections - not worth it !
							.count();
		System.out.println("Element count: " + numItems);
		
		// SUM
		int sum = people.stream().mapToInt(p -> p.getAge()).sum();
		System.out.println("Sum: " + sum);

		// AVERAGE
		OptionalDouble avg = people.stream().mapToInt(p -> p.getAge()).average();	// May return divide by zero so better to use OptionalDouble
		System.out.println("Average: " + ((avg.isPresent()) ? avg.getAsDouble() : "[NaN]"));
		
		System.out.println("=== end: aggregatingStreamValues ===");
	}
	
	public static void calcTimeSpansWithInstantAndDuration() throws InterruptedException {
		System.out.println("=== start: calcTimeSpansWithInstantAndDuration ===");
		
		Instant start = Instant.now();
		System.out.println(start);
		
		Thread.sleep(1000);
		
		Instant end = Instant.now();
		System.out.println(end);
		
		Duration elapsed = Duration.between(start, end);
		System.out.println("Elapsed: " + elapsed.toMillis() + " milliseconds");
		
		System.out.println("=== end: calcTimeSpansWithInstantAndDuration ===");
	}
	
	public static void reprDateTimeValues() {
		System.out.println("=== start: reprDateTimeValues ===");
		
		LocalDate currentDate = LocalDate.now();
		System.out.println("Current Date: " + currentDate);
		
		LocalDate specificDate = LocalDate.of(2011, 1, 16);
		System.out.println("Specific Date: " + specificDate);
		
		LocalTime currentTime = LocalTime.now();
		System.out.println("Current Time: " + currentTime);
		
		LocalTime specificTime = LocalTime.of(15, 20, 9);
		System.out.println("Specific Time: " + specificTime);

		LocalDateTime currentDT = LocalDateTime.now();
		System.out.println("Specific Date Time: " + currentDT);

		LocalDateTime specificDT = LocalDateTime.of(specificDate, specificTime);
		System.out.println("Specific Date Time: " + specificDT);
		
		System.out.println("=== end: reprDateTimeValues ===");
	}

	public static void formatDateTimeValues() {
		System.out.println("=== start: formatDateTimeValues ===");
		
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter df = DateTimeFormatter.ISO_DATE;
		System.out.println("Formatted ISO Date: " + df.format(currentDate));

		LocalTime currentTime = LocalTime.now();
		DateTimeFormatter tf = DateTimeFormatter.ISO_TIME;
		System.out.println("Formatted ISO Time: " + tf.format(currentTime));

		LocalDateTime currentDT = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
		System.out.println("Formatted ISO Date/Time: " + dtf.format(currentDT));
		
		DateTimeFormatter f_long = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
		System.out.println(f_long.format(currentDT));

		DateTimeFormatter f_short = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
		System.out.println(f_short.format(currentDT));
		
		String fr_long = f_long.withLocale(Locale.FRENCH).format(currentDT);
		System.out.println(fr_long);
		String fr_short = f_short.withLocale(Locale.FRENCH).format(currentDT);
		System.out.println(fr_short);
		
		DateTimeFormatterBuilder b = new DateTimeFormatterBuilder()
				.appendValue(ChronoField.MONTH_OF_YEAR)
				.appendLiteral("||")
				.appendValue(ChronoField.DAY_OF_MONTH)
				.appendLiteral("||")
				.appendValue(ChronoField.YEAR);
		DateTimeFormatter f_custom = b.toFormatter();
		System.out.println("Custom Date/Time Format: " + f_custom.format(currentDT));
		
		System.out.println("=== end: formatDateTimeValues ===");
	}

	public static void timeZoneOffsets() {
		System.out.println("=== start: timeZoneOffsets ===");
		
		LocalDateTime currentDT = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
		System.out.println(dtf.format(currentDT));
		
		ZonedDateTime gmt = ZonedDateTime.now(ZoneId.of("GMT+11"));
		System.out.println(dtf.format(gmt));
		
		ZonedDateTime ny = ZonedDateTime.now(ZoneId.of("America/New_York"));
		System.out.println(dtf.format(ny));

		Set<String> zones = ZoneId.getAvailableZoneIds();
		Predicate<String> cond = str -> str.contains("London");
		zones.forEach(z -> {
			if(cond.test(z)) {
				ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of(z));
				System.out.println("Zone: " + zdt.getZone() + ", Time: " + dtf.format(zdt));
			}
		});
		
		System.out.println("=== end: timeZoneOffsets ===");
	}

	public static void runJSfromJava() throws FileNotFoundException {
		System.out.println("=== start: runJSfromJava ===");
		
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("nashorn");
		File f = new File("src/org/example/java8/scripts/readurl.js");
		Reader reader = new FileReader(f);
		
//		String script = "var hello = 'Hello';"
//				+ "hello += ', Sagun'; "
//				+ "hello;";
//		Object result;
		
		String result;
		try {
			result = (String) engine.eval(reader);
//			result = (String) engine.eval(script);
			System.out.println("JS output = " + result);
		} catch (ScriptException e) {
			System.out.println("There was a JavaScript error!!!");
			e.printStackTrace();
		}
		
		System.out.println("=== end: runJSfromJava ===");
	}

	public static void joinStringValuesIntoDelimitedLists() {
		System.out.println("=== start: joinStringValuesIntoDelimitedLists ===");
		
		// Creating String Joiner
		StringJoiner sj = new StringJoiner(",", "{", "}");
		sj.setEmptyValue("No stooges yet");
		System.out.println(sj);
		
		sj.add("Moe")
			.add("Larry")
			.add("Curly");
		System.out.println(sj);
		
		// Merging StringJoiner instances
		StringJoiner sj2 = new StringJoiner(",");
		sj2.add("Shemp");
		
		sj.merge(sj2);
		System.out.println(sj);
		
		// Working with collections
		Set<String> set = new TreeSet<>();
		set.add("Kathmandu");
		set.add("Pokhara");
		set.add("Biratnagar");
		StringJoiner sj3 = new StringJoiner(" and ");
		set.forEach(s -> sj3.add(s));
		System.out.println(sj3);
		
		System.out.println("=== end: joinStringValuesIntoDelimitedLists ===");
	}
	
	public static void searchTextFilesWithStreams() {
		System.out.println("=== start: searchTextFilesWithStreams ===");
		
		Path path = FileSystems.getDefault().getPath("src/org/example/java8/files", "randomstuff.txt");
		String searchTerm = "To be, or not to be";
		try (Stream<String> lines = Files.lines(path)) {
			Optional<String> line = lines.filter(l -> l.contains(searchTerm)).findFirst();
			if(line.isPresent()) {
				System.out.println("Found: " + line.get());
			} else {
				System.out.println("Not found!");
			}
		} catch(Exception e) {
			System.out.println("There was an error: " + e.getMessage());
		}
		
		System.out.println("=== end: searchTextFilesWithStreams ===");
	}
	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException {
		testJava8();
		
//		replacingInterfaces();
		replacingInterfacesWithLambdas();
		
		builtInFunctionalInterfaces();
		
		List<Person> people = new ArrayList<>();
		people.add(new Person("Me", 222));
		people.add(new Person("Somone", 333));
		people.add(new Person("Random Guy", 123));
		
//		filteringCollectionsWithPredicateInterfaces(people);
		filteringCollectionsWithPredicateInterfacesLambdas(people);
		
		staticMethodReferences(people);
//		instanceMethodReferences(people);
		
		implDefaultMethodsInInterfaces(people);
		
		traversingCollectionsWithStreams(people);
		traversingCollectionsWithParallelStreams(people);
		
		creatingStreamsFromCollectionsAndArrays();
		creatingParallelStreamsFromCollectionsAndArrays();
		
		aggregatingStreamValues(people);
		
		calcTimeSpansWithInstantAndDuration();
		reprDateTimeValues();
		formatDateTimeValues();
		timeZoneOffsets();
		
		runJSfromJava(); // Nashorn JS engine
		
		joinStringValuesIntoDelimitedLists();
		searchTextFilesWithStreams();
	}
}
