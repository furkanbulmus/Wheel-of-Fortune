import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Main {
	public static Stack s3 = new Stack(10);
	public static Stack s4 = new Stack(10);
	public static Stack temps3 = new Stack(10);
	public static Stack temps4 = new Stack(10);
	
	public static void main(String[] args) throws InterruptedException {
		Play();
	}
		
	public static void Play() throws InterruptedException {
		
		Stack s1 = new Stack(196);
		Stack s2 = new Stack(26);
		Stack tempCountry = new Stack(196);
		Stack tempLetter = new Stack(26);
		
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String country = "";
		char letter = 'a';
		boolean gameEnd = false;
		int pointCtrl = 0;
		int point = 0;
		int totalPoint = 0;
		boolean notPoint = false;
		int countryLength = 0;
		int round = 0;
		boolean doubleMoney = false;
		
		try {  // We put the countries in our country file on a stack.
			File countries = new File("countries.txt");
			Scanner in = new Scanner(countries);
			while(in.hasNextLine()) {
				String line = in.nextLine();
				if (line != null) {
					s1.push(line.toUpperCase(Locale.ENGLISH));
				}
			}
			in.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < 26; i++)  // We put the alphabet on the stack.
			s2.push(alphabet.charAt(i));
		
		System.out.print("Please enter your name: ");
		Scanner name = new Scanner(System.in);
		String nameSurname = name.nextLine();
		System.out.println("");
		
		Random r = new Random(); 
		int x = r.nextInt(197);  // We generate a random number for country selection.
		
		for (int i = 0; i < s1.size() - x; i++) 
			tempCountry.push(s1.pop());  
		
		country = (String) s1.peek();  // We extract the selected country from the stack.
		System.out.println("Country selected:");
		countryLength = country.length();
		
		while ((!tempCountry.isEmpty())) 
			s1.push(tempCountry.pop());
		
		CircularQueue q1 = new CircularQueue(countryLength);
		CircularQueue q2 = new CircularQueue(countryLength);
		CircularQueue tempWord = new CircularQueue(countryLength);
		CircularQueue tempSpace = new CircularQueue(countryLength);
		
	    for (int i = 0; i < country.length(); i++) {
	    	q1.enqueue(country.charAt(i));  // We put the letters of the selected country in queue.
	    	q2.enqueue('-');
	    }
	    display(q2);
	    
	    while (gameEnd == false) {  // gameEnd is checking the finish status.
	    	
	    	doubleMoney = false;
	    	pointCtrl = 0;
	    	round++;
	    	System.out.println("");
	    	System.out.println("Step: " + round);
	    	System.out.println("The wheel is spinning...");
			Random rndWheel = new Random();
			int wheelNumber = rndWheel.nextInt(8);  // A random number is generated for the wheel.
			notPoint = false;
			Thread.sleep(1500);
			
			switch (wheelNumber) {  // The score status are determined by the number on the wheel.
			  case 0:
			    System.out.println("Wheel: 10");
			    point = 10;
			    break;
			  case 1:
			    System.out.println("Wheel: 20");
			    point = 20;
			    break;
			  case 2:
			    System.out.println("Wheel: 100");
			    point = 100;
			    break;
			  case 3:
			    System.out.println("Wheel: 250");
			    point = 250;
			    break;
			  case 4:
			    System.out.println("Wheel: 500");
			    point = 500;
			    break;
			  case 5:
			    System.out.println("Wheel: 1000");
			    point = 1000;
			    break;
			  case 6:
			    System.out.println("Wheel: Double Money :D");
			    doubleMoney = true;  // When the question is answered correctly, the money is doubled.
			    notPoint = true;
			    break;
			  case 7:
			    System.out.println("Wheel: Bankrupt :(");
			    totalPoint = 0;  // It resets the money.
			    notPoint = true;
			    break;
			}
	    	
	    	Random rndLetter = new Random();
		    int letterNumber = rndLetter.nextInt(s2.size()); // A random letter is determined.
		    
		    for (int i = 0; i < s2.size() - letterNumber; i++) 
				tempLetter.push(s2.pop());  
		    
		    Thread.sleep(1000);
	        letter = (char) s2.pop();
	        System.out.println("Letter is: " + letter);
			
			while (!(tempLetter.isEmpty())) 
				s2.push(tempLetter.pop());
			
	        int counter = 0;
			
			while (!(q1.isEmpty())) {  // It is checked whether the selected letter is in the word.
				if (q1.peek().equals(letter)) {
					while (!(q2.isEmpty())) 
						tempSpace.enqueue(q2.dequeue());
					
					for (int i = 0; i < counter; i++) 
						q2.enqueue(tempSpace.dequeue());
					
					q2.enqueue(letter);
					tempSpace.dequeue();
					
					while (!(tempSpace.isEmpty())) 
						q2.enqueue(tempSpace.dequeue());				
					pointCtrl++;
				}
				tempWord.enqueue(q1.dequeue());
				counter++;
			}
			
			while(!(tempWord.isEmpty()))
				q1.enqueue(tempWord.dequeue());
			
			display(q2);
			if (notPoint == false) // The score is calculated.
				totalPoint = totalPoint + (pointCtrl * point);
			if (doubleMoney == true && pointCtrl != 0)
				totalPoint = totalPoint * 2;
				
			System.out.println("Total point is: " + totalPoint);
			Thread.sleep(1500);
			int end = 0;
			
			while (! (q2.isEmpty())) {  // Checking if the word is found.
				if (q2.peek().equals('-'))
					end++;
				tempSpace.enqueue(q2.dequeue());
			}
			
			while (! (tempSpace.isEmpty())) 
				q2.enqueue(tempSpace.dequeue());
			if (end == 0) {  // If the game is over, it goes out of the loop.
				gameEnd = true;
				System.out.println("You win $" + totalPoint);
			}
	    }
	 }    	
	
	public static void display(CircularQueue cq) {  // Queue printing function.
		for (int i = 0; i < cq.size(); i++) {
			System.out.print(cq.peek() + " ");
			cq.enqueue(cq.dequeue());
		}
		System.out.println("");
	}
	
	public static void highScore() {
		Object[] tempName;
		try {
			File highScore = new File("HighScoreTable.txt");
			Scanner in = new Scanner(highScore);
			while(in.hasNextLine()) {
				String line = in.nextLine();
				if (line != null) {
					tempName = line.split(" ");
					temps3.push(tempName[0]);
					temps4.push(tempName[1]);
					}
				}
			in.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(!temps4.isEmpty())
        {
            int tmp = (int) temps4.pop();
            String temp = (String) temps3.pop();
 
            while(!s4.isEmpty() && (int) s4.peek() > tmp) {
            	 temps4.push(s4.pop());
            	 temps3.push(s3.pop());
            }
            s4.push(tmp);
            s3.push(temp);
        }
		
		System.out.println("The Stack is: " + s3);
		System.out.println("The Stack is: " + s4);
		
		
	}
	
  }
	    
	    
	    
	    
	    
	
		

	

