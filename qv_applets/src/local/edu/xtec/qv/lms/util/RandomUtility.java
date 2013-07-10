package edu.xtec.qv.lms.util;

public class RandomUtility{

	public static int mod(int a, int b){
		int res=a%b;
		return res;
	}

	public static int random(int i, int min, int max){
		long llavor=9140;
		int j=0;
		while(j<i){
	    		llavor = llavor*llavor;
			String t = llavor+"";
			int ini=t.length()-6;
			if (ini<0){
				ini=0;
			}
			int end=(t.length()-2);
			if (end<0){
				end=0;
			}
	    		String s = t.substring(ini,end);
			
			llavor=Long.parseLong(s);
			j++;
	    	}
		int res = mod((int)llavor,max)+min;
		System.out.println("random("+i+","+min+","+max+")="+res);
	    	return res;
	}

	public static int[] getRandomArrayRandomizable(int num, int rand, String randomizable){
		System.out.println("getRandomArrayRandomizable("+num+","+rand+","+randomizable+")");
		// Retorna un array amb el parell index: index canviat
		if (rand==0) randomizable = "";
		int[] opt = new int[num];
	        
	    int[] randomOpt = new int[num+1];
		randomOpt[0]=0;
		int i=1;
		while(i<=num){
			if (randomizable.indexOf(","+i+",")>=0){ //Pot canviar l'ordre
				opt[i-1]=i;
				randomOpt[i]=0;
			} else { //Ordre fixe
				opt[i-1]=-1;
				randomOpt[i]=i;
			}
			i++;
		}
		int j=1;
		while(j<=num){	
			if (randomOpt[j]==0){ //Encara no té valor assignat
				int m = j+rand;
				int k = random(m, 1, 9);
				int ind=0;
				int l=0;
				while(ind<k){
					if (opt[mod(l, num)]!=-1){
						ind++;
					}
					if (ind<k){
						l++;
					}
				}
				randomOpt[j] = opt[mod(l, num)];
				opt[mod(l, num)]=-1;
			}
			j++;
		}
		
		for (int k=0;k<randomOpt.length;k++){
			System.out.println("randomOpt["+k+"]="+randomOpt[k]);
		}
		return randomOpt;
	}
	
	public static int getReverse(int[] randomOpt, int pos){
		int rev = pos;
		boolean bFound = false;
		for(int i=0; !bFound && randomOpt!=null && i<randomOpt.length; i++){
			if (randomOpt[i]==pos){
				bFound = true;
				rev = i;
			}
		}
		System.out.println("getReverse pos="+pos+" rev="+rev);
		return rev;
	}

}