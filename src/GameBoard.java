import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class GameBoard{
	
	private String identifier = null;
	public ArrayList<CharPoint> gameBoard = new ArrayList<CharPoint>();
	private ArrayList<CharPoint> ownedChar = new ArrayList<CharPoint>();
	public static ArrayList<Path> possibleWords = new ArrayList<Path>();
	
	public GameBoard(){}
	
	public GameBoard(String fileName) throws IOException{
		this.identifier = fileName.replaceFirst(".txt", "");
		String boardChar = readFile(fileName);
		
		if(boardChar.length()!=130){
			System.out.println(boardChar.length());
			System.out.println("Board is not formatter correctly for a WordBase game");
			throw new IOException();
		}
		
		char[] charArray = boardChar.toCharArray();
		
		int i = 12;
		int k = 0;
		
		while( i >= 0){
			int j = 0;
			
			while(j<10){
				char character = charArray[k];
				Point point = new Point(i,j);
				CharPoint charPoint = new CharPoint(point,Character.toString(character),this);
				this.gameBoard.add(charPoint);
				
				if(Character.isUpperCase(character))
					this.ownedChar.add(charPoint);
		
				j++;
				k++;		
			}
			i--;
		}
	}
	
	public ArrayList<CharPoint> getBoard(){
		return this.gameBoard;
	}
	
	public String getIdentifer(){
		return this.identifier;
	}
	
	public static CharPoint getCharPoint(int i, int j, ArrayList<CharPoint> gameBoard){
		Point toFind = new Point(i,j);
		
		for(CharPoint charPoint: gameBoard)
			if(charPoint.getPoint().equals(toFind))
				return charPoint;
		
		CharPoint test = new CharPoint(toFind, "!");
		return test;
	}
	
	public void printGameBoard(ArrayList<CharPoint> gameBoard){
		for (int i = 12; i >=0; i--){
			for (int j = 0; j < 10; j++){
				System.out.print(getCharPoint(i,j,gameBoard).getChar());			
				System.out.print("\t");
			}
			System.out.print("\n");
		}
	}
	
	public void analyzeGameBoard(){
		String current = null;
		possibleWords.clear();
		
		for(CharPoint charPoint: ownedChar){
			
			ArrayList<Point> used = new ArrayList<Point>();
			used.add(charPoint.getPoint());
			current = charPoint.getChar();
		
			for(CharPoint neighbor: charPoint.getNeighBors()){	
				Dictionary.subDictionary(current.concat(neighbor.getChar()));
				analyzeNeighbors(charPoint,current,used);
			}
		}
	}
	
	public void analyzeNeighbors(CharPoint start, String current, 
			ArrayList<Point> used){
		
		for(CharPoint neighbor: start.getNeighBors()){
			ArrayList<Point> usedClone = (ArrayList<Point>) used.clone();
			Path path = new Path(current,usedClone,gameBoard);
			boolean cont = true;
		
			if(usedClone.contains(neighbor.getPoint()))
				cont = false;
			
			String currentPlus = current.concat(neighbor.getChar());
			
			if(!(Dictionary.wordStart(currentPlus)))
				cont = false;
			
			if(cont){
				
				usedClone.add(neighbor.getPoint());
				path.replaceWord(currentPlus);
				
				if(Dictionary.isWord(path.getWord())&&!(possibleWords.contains(path)))
					possibleWords.add(path);
				
				analyzeNeighbors(neighbor,currentPlus,usedClone);
			}
		}
	}
		
	public void printWord(ArrayList<Path> subWords, int start, int maxSize, Comparator comp){	
		System.out.println("\nYour "+comp.toString()+" Words");
		Collections.sort(subWords, comp);
		
		if(subWords.size()<maxSize)
			maxSize = subWords.size();
		
		for (int index = start; index < maxSize ; index++){
			Path path = subWords.get(index);
			System.out.print(path.getWord());
			
			for(Point point: path.getPath())
				System.out.print(point.toString());
			
			System.out.println();
		}
	}
	
	public void printWords(ArrayList<Path> subWords, int start, int maxSize){
		Comparator length = new LengthComparator();
		Comparator height = new ReachComparator();
		Comparator depth = new ReachComparatorMin();
		
		printWord(subWords, start, maxSize, length);
		printWord(subWords, start, maxSize, height);
		printWord(subWords, start, maxSize, depth);
	}

	public final String readFile(String fileName) throws IOException {
		
		FileReader inputFile = new FileReader(fileName);
		BufferedReader input = new BufferedReader(inputFile);
		
		String toReturn = "";
		String Line;
		
		while((Line = input.readLine())!=null){
			toReturn = toReturn.concat(Line);
		}
		
		input.close();
		return toReturn.replaceAll("\\s", "");
	}
	
	public final static ArrayList<Path> subWords(ArrayList<Path> moreWords, int row, int column){
		
		ArrayList<Path> toReturn = new ArrayList<Path>();
		
		for(Path path: moreWords)
			if(path.getPath().contains(new Point(row,column)))
				toReturn.add(path);
		
		return toReturn;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
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
		GameBoard other = (GameBoard) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
}