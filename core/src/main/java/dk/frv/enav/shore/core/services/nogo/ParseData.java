package dk.frv.enav.shore.core.services.nogo;

import java.util.ArrayList;
import java.util.List;
import dk.frv.enav.shore.core.domain.DepthDenmark;


public class ParseData {

	/**
	 * @param args
	 */
	List<List<DepthDenmark>> lines;
	DepthDenmark minDepthDenmark;
	DepthDenmark currentDepthDenmark;
	int currentIndex;



	public List<List<DepthDenmark>> getParsed(List<List<DepthDenmark>> lines) {
//		System.out.println("Finding lines");

		this.lines = lines;

//		System.out.println(lines);

		// Data initialized

		List<List<DepthDenmark>> result = new ArrayList<List<DepthDenmark>>();

		for (int i = 0; i < lines.size(); i++) {
			// Grab a line
			List<DepthDenmark> currentLine = lines.get(i);

			DepthDenmark currentDepthDenmark = null;
			DepthDenmark nextDepthDenmark = null;
			DepthDenmark previousDepthDenmark = null;

			List<DepthDenmark> K = new ArrayList<DepthDenmark>();

			for (int j = 0; j < currentLine.size(); j++) {
				
				currentDepthDenmark = currentLine.get(j);
//				System.out.println("The current DepthDenmark is: " + currentDepthDenmark.getN() + ", " + currentDepthDenmark.getM());

				if (j == 0) {
					// It's a new line
					K.add(currentDepthDenmark);
					previousDepthDenmark = currentDepthDenmark;
//					System.out.println("Start");
					
					if (currentLine.size() != 1){
					
					continue;
					}
				}

				// It has a next
				if ( currentLine.size() > j + 1) {

					nextDepthDenmark = currentLine.get(j + 1);

					
					if((previousDepthDenmark.getN() != currentDepthDenmark.getN()-1) && nextDepthDenmark.getN() != currentDepthDenmark.getN() + 1){
						//Punkt der står alene
						
						if (K.size() != 0){
							result.add(K);
							K = new ArrayList<DepthDenmark>();
							}
						
						//result.add(K);
						
//						K = new ArrayList<DepthDenmark>();
//						K.add(currentDepthDenmark);

						K = new ArrayList<DepthDenmark>();
						previousDepthDenmark = currentDepthDenmark;
						continue;
					}
					
					if ((previousDepthDenmark.getN() == currentDepthDenmark.getN() - 1)
							&& (nextDepthDenmark.getN() == currentDepthDenmark.getN() + 1)) {
						// Det er en del af et linje stykke
						previousDepthDenmark = currentDepthDenmark;
//						System.out.println("Linje stykke");
						continue;
					}

					// Er det en slut p et intern linjestykke?
					if (!(nextDepthDenmark.getN() == currentDepthDenmark.getN() + 1) && (currentDepthDenmark.getN() == previousDepthDenmark.getN()+1)) {
						K.add(currentDepthDenmark);
						result.add(K);
						K = new ArrayList<DepthDenmark>();

						previousDepthDenmark = currentDepthDenmark;
//						System.out.println("Slut på intern linjestykke");
						continue;
					}

					// Er det en start p et intern linjestykke?
					if (!(previousDepthDenmark.getN() == currentDepthDenmark.getN() - 1) && (currentDepthDenmark.getN()+1 == nextDepthDenmark.getN())) {
						
						if (K.size() != 0){
							result.add(K);
							K = new ArrayList<DepthDenmark>();
							}
						
						K.add(currentDepthDenmark);

						previousDepthDenmark = currentDepthDenmark;
//						System.out.println("Start p intern linjestykke");
						continue;
					}
				}

//				System.out.println("j is: " + j);
//				System.out.println("The current line size is: " + currentLine.size());
				
				
				
				if (currentLine.size() == j + 1) {
					// End of the line

					// Is it a seperate DepthDenmark
					if (!(previousDepthDenmark.getN() == currentDepthDenmark.getN() - 1)) {
						//result.add(K);
						//K = new ArrayList<DepthDenmark>();
						K.add(currentDepthDenmark);
						result.add(K);
						K = new ArrayList<DepthDenmark>();

//						System.out.println("Single seperate end DepthDenmark");
						continue;
					}

					if ((previousDepthDenmark.getN() == currentDepthDenmark.getN() - 1)) {
						K.add(currentDepthDenmark);
						result.add(K);
						
						K = new ArrayList<DepthDenmark>();


//						System.out.println("Line end DepthDenmark");
						continue;
					}
					
					
				}

			}

		}

		return result;
		



	}

}
