package de.perkins.common.util.json;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.Random;

public class BabyBenchmark {
    private static final List<String> JSON = List.of("""
            {
            	"id": "0001",
            	"type": "donut",
            	"name": "Cake",
            	"ppu": 0.55,
            	"batters":
            		{
            			"batter":
            				[
            					{ "id": "1001", "type": "Regular" },
            					{ "id": "1002", "type": "Chocolate" },
            					{ "id": "1003", "type": "Blueberry" },
            					{ "id": "1004", "type": "Devil's Food" }
            				]
            		},
            	"topping":
            		[
            			{ "id": "5001", "type": "None" },
            			{ "id": "5002", "type": "Glazed" },
            			{ "id": "5005", "type": "Sugar" },
            			{ "id": "5007", "type": "Powdered Sugar" },
            			{ "id": "5006", "type": "Chocolate with Sprinkles" },
            			{ "id": "5003", "type": "Chocolate" },
            			{ "id": "5004", "type": "Maple" }
            		]
            }
            """,
            """
                    [
                    	{
                    		"id": "0001",
                    		"type": "donut",
                    		"name": "Cake",
                    		"ppu": 0.55,
                    		"batters":
                    			{
                    				"batter":
                    					[
                    						{ "id": "1001", "type": "Regular" },
                    						{ "id": "1002", "type": "Chocolate" },
                    						{ "id": "1003", "type": "Blueberry" },
                    						{ "id": "1004", "type": "Devil's Food" }
                    					]
                    			},
                    		"topping":
                    			[
                    				{ "id": "5001", "type": "None" },
                    				{ "id": "5002", "type": "Glazed" },
                    				{ "id": "5005", "type": "Sugar" },
                    				{ "id": "5007", "type": "Powdered Sugar" },
                    				{ "id": "5006", "type": "Chocolate with Sprinkles" },
                    				{ "id": "5003", "type": "Chocolate" },
                    				{ "id": "5004", "type": "Maple" }
                    			]
                    	},
                    	{
                    		"id": "0002",
                    		"type": "donut",
                    		"name": "Raised",
                    		"ppu": 0.55,
                    		"batters":
                    			{
                    				"batter":
                    					[
                    						{ "id": "1001", "type": "Regular" }
                    					]
                    			},
                    		"topping":
                    			[
                    				{ "id": "5001", "type": "None" },
                    				{ "id": "5002", "type": "Glazed" },
                    				{ "id": "5005", "type": "Sugar" },
                    				{ "id": "5003", "type": "Chocolate" },
                    				{ "id": "5004", "type": "Maple" }
                    			]
                    	},
                    	{
                    		"id": "0003",
                    		"type": "donut",
                    		"name": "Old Fashioned",
                    		"ppu": 0.55,
                    		"batters":
                    			{
                    				"batter":
                    					[
                    						{ "id": "1001", "type": "Regular" },
                    						{ "id": "1002", "type": "Chocolate" }
                    					]
                    			},
                    		"topping":
                    			[
                    				{ "id": "5001", "type": "None" },
                    				{ "id": "5002", "type": "Glazed" },
                    				{ "id": "5003", "type": "Chocolate" },
                    				{ "id": "5004", "type": "Maple" }
                    			]
                    	}
                    ]
                    """,
            """
                    {
                    	"id": "0001",
                    	"type": "donut",
                    	"name": "Cake",
                    	"image":
                    		{
                    			"url": "images/0001.jpg",
                    			"width": 200,
                    			"height": 200
                    		},
                    	"thumbnail":
                    		{
                    			"url": "images/thumbnails/0001.jpg",
                    			"width": 32,
                    			"height": 32
                    		}
                    }
                    """,
            """
                    {
                    	"items":
                    		{
                    			"item":
                    				[
                    					{
                    						"id": "0001",
                    						"type": "donut",
                    						"name": "Cake",
                    						"ppu": 0.55,
                    						"batters":
                    							{
                    								"batter":
                    									[
                    										{ "id": "1001", "type": "Regular" },
                    										{ "id": "1002", "type": "Chocolate" },
                    										{ "id": "1003", "type": "Blueberry" },
                    										{ "id": "1004", "type": "Devil's Food" }
                    									]
                    							},
                    						"topping":
                    							[
                    								{ "id": "5001", "type": "None" },
                    								{ "id": "5002", "type": "Glazed" },
                    								{ "id": "5005", "type": "Sugar" },
                    								{ "id": "5007", "type": "Powdered Sugar" },
                    								{ "id": "5006", "type": "Chocolate with Sprinkles" },
                    								{ "id": "5003", "type": "Chocolate" },
                    								{ "id": "5004", "type": "Maple" }
                    							]
                    					},
                    
                    					...
                    
                    				]
                    		}
                    }
                    """);
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BabyBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    public void test(Blackhole blackhole) {
        int index = RANDOM.nextInt(JSON.size());
        String json = JSON.get(index);
        blackhole.consume(Json.parse(json));
    }
}
