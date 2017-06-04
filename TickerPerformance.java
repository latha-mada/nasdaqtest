package nasdaq;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.HashMap;
//import java.util.List;

/**
 * 
 * @author Srilatha Mada
 * This program gives solution to three types of queries on the given data.
 * This is written on machine with JAVA-version 1.7.
 * The data is 5-day data of ticker prices, and 1 sector to ticker mapping file.
 * The queries are to calculate are:
 * <p>
 *
 * Which ticker has the most profit on which day? 
* <p>
* Which ticker has the most profit in a 5 days?
* <p>
* Which sector performed best?
* <p>
*
 *
 */
public class TickerPerformance
{
	public static void main(String[] args)
	{
		int day=0;
		//String[] files = {"C:\\Users\\smada\\Documents\\exercise\\tickers_prices_day_1.csv","C:\\Users\\smada\\Documents\\exercise\\tickers_prices_day_2.csv","C:\\Users\\smada\\Documents\\exercise\\tickers_prices_day_3.csv","C:\\Users\\smada\\Documents\\exercise\\tickers_prices_day_4.csv","C:\\Users\\smada\\Documents\\exercise\\tickers_prices_day_5.csv"};
		String[] files = {"tickers_prices_day_1.csv","tickers_prices_day_2.csv","tickers_prices_day_3.csv","tickers_prices_day_4.csv","tickers_prices_day_5.csv"};
		HashMap<String,ArrayList<Integer>> tickerPricesMap = new HashMap<String,ArrayList<Integer>>();
		HashMap<Integer,HashMap<String,ArrayList<Integer>>> dayMap = new HashMap<Integer,HashMap<String,ArrayList<Integer>>>();
		for (String file : files)
		{
			tickerPricesMap = readFile(file);
			day++;
			dayMap.put(day,new HashMap<String,ArrayList<Integer>>());
			dayMap.put(day, tickerPricesMap);
		}
		question1(dayMap);
		question2(dayMap);
		question3(dayMap);
	}
	
	/**
	 * This method is to read the file with ticker prices on 5 different days.
	 * the files are stored in a Hashmap<Integer,HashMap<String,ArrayList<Integer>>>, 
	 * with key being the day of the file ( ranging from 1 - 5)
	 * and inner hashMap has key of the ticker and value of the array of its prices for a particular day.
	 * @param file
	 * @return HashMap of the file read;
	 */
	
	public static HashMap<String,ArrayList<Integer>> readFile(String file)
	{
		HashMap<String,ArrayList<Integer>> map = new HashMap<String,ArrayList<Integer>>();
		BufferedReader bf = null;
		String line = "";
		try
		{
			bf = new BufferedReader(new FileReader(file));
			while((line=bf.readLine())!=null)
			{
				String ticker = (line.split(","))[0];
				String[] fields = line.substring(line.indexOf("["),line.length()).split("\\[|\\]")[1].split(",");
				map.put(ticker,new ArrayList<Integer>());
				for (String field : fields)
				{
					map.get(ticker).add(Integer.parseInt(field));
				}
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		finally
		{
			if (bf != null)
			{
				try
				{
					bf.close();
				}
				catch(IOException ioe1)
				{
					ioe1.printStackTrace();
				}
			}
		}
		return map;
	}
	
	/**
	 * this method answers the first question :
	 * <p>
	 * Which ticker has the most profit on which day?
	 * I calculated the  profit on each ticker and save that into hashMap(hm) whose key is the 
	 * Ticker concatenated with day and separated by ":" ex: CL:1 . this is necessary to track the profit 
	 * for which day.
	 *  and value is the profit 
	 * @param map
	 */
	public static void question1(HashMap<Integer,HashMap<String,ArrayList<Integer>>> map)
	{
		HashMap<String,Integer> hm = new HashMap<String,Integer>();
		int maxProfit = Integer.MIN_VALUE;
		for (Integer d : map.keySet())
		{	
			for (String key : map.get(d).keySet())
			{
				int profit = calcProfit(map.get(d).get(key));
				hm.put((key+":"+d),profit);
				if (profit > maxProfit)
					maxProfit = profit;
			}
		}
		//System.out.println("maximum profit = "+maxProfit);
		System.out.println("1:Which ticker has the most profit on which day?");
		System.out.println("Answer: Tickers with maximum profit are");
		for (String key : hm.keySet())
		{
			if (hm.get(key).compareTo((Integer) maxProfit)==0)
			{
				System.out.println("	"+key.split(":")[0]+" on day"+key.split(":")[1]);
			}
		}
	}

	/**
	 * This method addresses question 2.
	 * <p>
	 * Which ticker has the most profit in a 5 days?
	 * <p>
	 * I appended the priceArray of the tickers for all five days into the 
	 * HashMap(hm) whose key is ticker name.
	 * Then calculated the profit on the whole list of the ticker.
	 * "calcProfit" is used to pass the arrayList and calculate the profit.
	 * @param map
	 */
	public static void question2(HashMap<Integer,HashMap<String,ArrayList<Integer>>> map)
	{
		HashMap<String,ArrayList<Integer>> hm = new HashMap<String,ArrayList<Integer>>();
		int profit=0;
		int maxProfit = Integer.MIN_VALUE;
		for (Integer d : map.keySet())
		{
			for (String key : map.get(d).keySet())
			{
				if (!hm.containsKey(key))
				{
					hm.put(key,new ArrayList<Integer>());
					hm.get(key).addAll(map.get(d).get(key));
				}
					
				else
				{
					hm.get(key).addAll(map.get(d).get(key));
				}
			}
		}
		
		for (String key : hm.keySet())
		{
			profit=calcProfit(hm.get(key));
			if (profit>maxProfit)
			{
				maxProfit=profit;
			}
			hm.put(key, new ArrayList<Integer>());
			hm.get(key).add((Integer)profit);
		}
		System.out.println();
		System.out.println("2:Which ticker has the most profit in a 5 days??");
		for (String key : hm.keySet())
		{
			if (hm.get(key).get(0).equals((Integer)maxProfit))
			{
				System.out.println("Answer: The Ticker with maximum profit in 5 days is "+key);
			}
		}
	}
/**
 * This is a helper method to calculate the profit for a given list of elements.
 * this method is called by question1 and question2.
 * @param list
 * @return int
 */
	
	public static int calcProfit(ArrayList<Integer> list)
	{
		int profit=0;
		int diff;
		for (int i=0; i<(list.size()-1); i++)
		{
			for (int k=i+1; k<(list.size()); k++)
			{
				diff = list.get(k)-list.get(i);
				if (diff>profit)
				{
					profit=diff;
				}
			}
		}
		return profit;
	}
	
	/**
	 * This method answers the third question.
	 * <p>
	 * Which sector performed best?
	 * HashMap-hm stores the Ticker,
	 * <difference of closing after 5th day and opening on 1st day,opening of 1st day>
	 * difference of closing after 5th day and opening on 1st day is change in price behaviour for 5 days.
	 * <p>
	 * HashMap-tickerSectorHM is the <ticker,sector> map returned by  readSectorFile method.
	 * HashMap-hmSector is <sector,arrayList<all the price change for 5 days for tickers in that 
	 * particular sector>. from this arrayList I sum all the elements of the arrayList
	 * to get total change in price in that particular sector.
	 * HashMap -hmSecOpeningSum is <sector,sum of all the opening prices of all tinckers in a particular sector>
	 * this sum of opening price is used to calculate the average performance of a sector. 
	 * @param map
	 *
	 */
	public static void question3(HashMap<Integer,HashMap<String,ArrayList<Integer>>> map)
	{
		//int[] dayArr = {1,5};
		String opening = "";
		String closing = "";
		HashMap<String,ArrayList<Double>> hm = new HashMap<String,ArrayList<Double>>();
		//HashMap<String,Double> openingSumHM = new HashMap<String,Double>();
		HashMap<String,ArrayList<Double>> hmSector = new HashMap<String,ArrayList<Double>>();
		HashMap<String,String> tickerSectorHM = new HashMap<String,String>();
		HashMap<String,Double> hmSecOpeningSum = new HashMap<String,Double>();
		for (Integer d : map.keySet())
		{
			for (String key : map.get(d).keySet())
			{

				if (d.equals(1) || d.equals(5))
				{
					if (d.equals(1))
					{
						//System.out.println("opening");
						opening = map.get(d).get(key).get(0).toString();
						
					}
					else 
					{
						closing = map.get(d).get(key).get(map.get(d).get(key).size()-1).toString();
						//System.out.println("closing "+closing);
					}
					if (hm.containsKey(key))
					{
						double o = (Double.parseDouble(closing)-hm.get(key).get(0));
						hm.get(key).add(o);
					}
					else
					{
						hm.put(key, new ArrayList<Double>());
						hm.get(key).add(Double.parseDouble(opening));
					}
				}
			}
		}
		//tickerSectorHM = readSectorFile("C:\\Users\\smada\\Documents\\exercise\\tickers_sectors.csv");
		tickerSectorHM = readSectorFile("tickers_sectors.csv");
		for (String key : hm.keySet())
		{
			if (!hmSector.containsKey(tickerSectorHM.get(key)))
			{
				hmSector.put(tickerSectorHM.get(key),new ArrayList<Double>());
				hmSecOpeningSum.put(tickerSectorHM.get(key),null);
			
			}
			
			hmSector.get(tickerSectorHM.get(key)).add(hm.get(key).get(1));
			double openingSum= (hmSecOpeningSum.get(tickerSectorHM.get(key))==null?0:hmSecOpeningSum.get(tickerSectorHM.get(key)))+hm.get(key).get(0);
			hmSecOpeningSum.put(tickerSectorHM.get(key),openingSum);
			
		}
		
		double sum=0,avg=0,maxAvg=0;
		for (String key : hmSector.keySet())
		{
			avg=0;
			sum=0;
			for (double value : hmSector.get(key))
			{
				sum+=value;
			}
			avg=sum/hmSecOpeningSum.get(key)*100;
			if (maxAvg<avg)
				maxAvg=avg;
			hmSector.put(key,new ArrayList<Double>());
			hmSector.get(key).add(avg);
		}
		//maxAvg*=100;
		System.out.println("\n");
		
		for (String key : hmSector.keySet())
		{
			String str = "%";
			System.out.printf("%-15s \tprofit%s\t %.2f\n",key,str,hmSector.get(key).get(0));
		}
		System.out.println();
		System.out.println("3: Which sector performed best?");
		System.out.println("Answer: The Sector that performed the best is");
		for (String key : hmSector.keySet())
		{
			String str = "%";
			//System.out.println(key+"  "+hmSector.get(key).toString());
			if (hmSector.get(key).get(0).compareTo((Double) maxAvg) == 0)
				System.out.printf("\t%s with profit%s of %.2f\n ",key,str,hmSector.get(key).get(0));
		
		}
	}
	/**
	 * this Method will read the ticker-sector data file and stores it in hm HashMap and returns it
	 * to question3 method;
	 * @param file
	 * @return HashMap
	 */
	public static HashMap<String,String> readSectorFile(String file)
	{
		HashMap<String,String> hm = new HashMap<String,String>();
		BufferedReader bf = null;
		String line = "";
		try
		{
			bf = new BufferedReader(new FileReader(file));
			while((line = bf.readLine())!=null)
			{
				hm.put(line.split(",")[0], line.split(",")[1]);
			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			try
			{
				bf.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return hm;
	}
	
}

