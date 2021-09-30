package org.jteodoro.simplelocalcache;

import java.util.List;
import java.util.Objects;

public class SimpleLocalCacheApplication {

	public static void main(String[] args) throws InterruptedException {
		CachingService<Integer> service = CachingService.of(10, 1000, PurgeType.LRU);
		List<Integer> creation = List.of(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17);
		creation.parallelStream()
			.forEach(item -> service.add(String.valueOf(item), item));

		List<Integer> usage = List.of(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,
			0,1,2,3,4,5,6,7, 0,1,2,3,4,5,6,7,13,14,15,16,17,11,12,13,14,15,16,
			5,6,7,8,9,10,11,12,11,12,13,14,15,16,
			5,6,7,8,9,10,11,12,0,1,2,3,4,5,6,
			5,6,7,8,9,10,11,12,11,12,13,14,15,16,
			5,6,7,8,9,10,11,12,0,1,2,3,4,5,6);
		
		List<String> answers = usage.parallelStream()
			.map(item -> {
				Integer v = service.get(String.valueOf(item));
				return Objects.nonNull(v)
					? "hit"
					: "miss";
			}).toList();

		System.out.println(service.size());
		System.out.println(service.getMaxItems());

		System.out.println("hits: "+ answers.stream().filter(str -> str.equals("hit")).count());
		System.out.println("miss: "+ answers.stream().filter(str -> str.equals("miss")).count());
		
		Thread.sleep(5000);

		System.out.println(service.size());
		System.out.println(service.getMaxItems());
	}

}
