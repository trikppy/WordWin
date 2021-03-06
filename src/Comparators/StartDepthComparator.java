package Comparators;

import WordWin.Path;

public class StartDepthComparator implements java.util.Comparator<Path> {

    public int compare(Path p1, Path p2) {
    	
    	if(p1.getPath().get(0).getRow() < p2.getPath().get(0).getRow())
            return -1;

        if(p2.getPath().get(0).getRow() < p1.getPath().get(0).getRow())
            return 1;

        return p1.getWord().compareTo(p2.getWord());
    }
    
    @Override
    public String toString(){
    	return "(Opponent's) Winning";
    }
}