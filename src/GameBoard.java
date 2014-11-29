import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GameBoard{
	
	public static final List<CharPoint> gameBoard = new ArrayList<CharPoint>();
	public static final List<CharPoint> ownedChar = new ArrayList<CharPoint>();
	public static List<Path> possibleWords = new ArrayList<Path>();
	
	public GameBoard(String boardChar){
		
		char[] charArray = boardChar.toCharArray();
		
		int i = 12;
		int k = 0;
		
		while( i >= 0){
			int j = 0;
			
			while(j<10){
				
				char character = charArray[k];
				Point point = new Point(i,j);
				CharPoint charPoint = new CharPoint(point,Character.toString(character));
				
				gameBoard.add(charPoint);
				
				if(charPoint.getBool())
					ownedChar.add(charPoint);
					
				j++;
				k++;
				
			}
			i--;
		}
	}
	
	public static CharPoint getCharPoint(int i, int j){
	
		Point toFind = new Point(i,j);
		
		for(CharPoint charPoint: gameBoard){
			
			if(charPoint.getPoint().equals(toFind))
				return charPoint;
		}
		
		CharPoint test = new CharPoint(toFind, "!");
		return test;
		
	}
	
	public static void printGameBoard(){
		
		for (int i = 12; i >=0; i--){
			for (int j = 9; j >= 0; j--){
				System.out.print(getCharPoint(i,j).getChar());			
				System.out.print("\t");
			}
			System.out.print("\n");
		}
	}
	
	public static void analyzeGameBoard(){
		
		String current = null;
		ArrayList<Point> used = new ArrayList<Point>();
		
		possibleWords.clear();
		
		for(CharPoint charPoint: ownedChar){
			
			System.out.println("analyzing....");
		
			used.add(charPoint.getPoint());
			current = charPoint.getChar();
			//makes dictionary smaller!
			Dictionary.subDictinonary(current);
		
			for(CharPoint neighbor: charPoint.getNeighBors()){
				analyzeNeighbors(charPoint,current,used);
			}
		}
		
		analyzeWordsLength(possibleWords);
		analyzeWordsReach(possibleWords);
		analyzeWordsReachMin(possibleWords);
	}
	
	public static void analyzeNeighbors(CharPoint start, String current, 
			ArrayList<Point> used){
		
		for(CharPoint neighbor: start.getNeighBors()){
			
			ArrayList<Point> usedClone = (ArrayList<Point>) used.clone();
			Path path = new Path(current, usedClone);
			
			boolean cont = true;
		
			if(usedClone.contains(neighbor.getPoint()))
				cont = false;
			
			String currentPlus = current.concat(neighbor.getChar());
			
			if(!(Dictionary.wordStart(currentPlus)))
				cont = false;
			
			if(cont){
				
				usedClone.add(neighbor.getPoint());
				path.replaceWord(currentPlus);
				
				if(Dictionary.isWord(path.getWord())&&!(possibleWords.contains(path))){
					possibleWords.add(path);
				}
				
				analyzeNeighbors(neighbor,currentPlus,usedClone);
			}
		}
	}
		
	public static void analyzeWordsLength(List<Path> possibleWords2){	
		
		System.out.println("\nYour Longest Words....");
		
		Collections.sort(possibleWords2, new LengthComparator());
		
		int maxSize = 5;
		
		if(possibleWords2.size()<5)
			maxSize = possibleWords2.size();
		
		for (int index = 0; index < maxSize ; index++){
			Path path = possibleWords2.get(index);
			System.out.print(path.getWord());
			
			for(Point point: path.getPath())
				System.out.print(point.toString());
			
			System.out.println();
		}
	}
	
	public static void analyzeWordsReach(List<Path> possibleWords2){
		
		System.out.println("\nYour Tallest Reach...");
		
		Collections.sort(possibleWords2, new ReachComparator());
		
		int maxSize = 5;
		
		if(possibleWords2.size()<5)
			maxSize = possibleWords2.size();
		
		for (int index = 0; index < maxSize; index++){
			Path path = possibleWords2.get(index);
			System.out.print(path.getWord());
			
			for(Point point: path.getPath())
				System.out.print(point.toString());
			
			System.out.println();
		}
	}
	
	public static void analyzeWordsReachMin(List<Path> possibleWords2){
		
		System.out.println("\nYour Lowest Reach...");
		
		Collections.sort(possibleWords2, new ReachComparatorMin());
		
		int maxSize = 5;
		
		if(possibleWords2.size()<5)
			maxSize = possibleWords2.size();
		
		for (int index = 0; index < maxSize ; index++){
			Path path = possibleWords2.get(index);
			System.out.print(path.getWord());
			
			for(Point point: path.getPath())
				System.out.print(point.toString());
			
			System.out.println();
		}
	}
}