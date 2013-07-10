// Per a cada element que es volgui ordenar cal possar-li un span
// amb identificador acabat en _opt[1-n]. La part inicial "inicial" serà
// un identificador comú a tots els elements a "ordenar".
// Rand serà un enter entre 0-80 que determinarà únicament la "ordenació".
// <span id="id_opt1" name="id_opt1"> HTML </span>
// La crida serà ordena(inicial,n,rand);


var section_order = -2; //-2:Desconegut -1:No s'ordena 0..80:Establert amb aquest valor
var item_order = -2; //-2:Desconegut -1:No s'ordena 0..80:Establert amb aquest valor

	
        function mod(a, b){
		var res=a-Math.floor(a/b)*b;
		return res;
	}
    	
    	function random(i,min,max){
            
    		llavor=9140;
    		for (j=0;j<i;j++){
    			llavor=llavor*llavor;
    			var t=llavor+"";
    			
    			var s=t.substring(t.length-6,t.length-2);
    			llavor=s;
    		}
		res = mod(llavor,max)+min;
//alert("random("+i+","+min+","+max+")="+res);
    		return res;
    	}

	function getRandomArrayRandomizable(num,rand,randomizable){ //Aquesta funció ha de substituir getRandomArray
		// Retorna un array amb el parell index: index canviat
	  if (rand==0) return;
        var opt=new Array(num);
        var randomOpt=new Array(num);
	  randomOpt[0]=0;
	  for(i=1;i<=num;i++){
		if (randomizable.indexOf(","+i+",")>=0){ //Pot canviar l'ordre
			opt[i-1]=i;
			randomOpt[i]=0;
		} else { //Ordre fixe
			opt[i-1]=-1;
			randomOpt[i]=i;
		}
	  }
	  var j=1;
		
	  for(j=1;j<=num;j++){
		//alert("j="+j+" num="+num);
		if (randomOpt[j]==0){ //Encara no té valor assignat
			var m=j+rand;
			var k=random(m,1,9);
			ind=0;
			var l=0;

			for (l=0;ind<k;){
				//alert("l="+l+" ind="+ind+" k="+k);
				if (opt[mod(l,num)]!=-1){
					ind++;
				}
				if (ind<k){
					l++;
				}
			}
			randomOpt[j]=opt[mod(l,num)];
			opt[mod(l,num)]=-1;
		 }
		//alert("randomOpt["+j+"]="+randomOpt[j]);

	   }
	   return randomOpt;
	}

	function getRandomArray(num,rand){
		// Retorna un array amb el parell index: index canviat
	  if (rand==0) return;
        var opt=new Array(num);
        var randomOpt=new Array(num);
	  for(i=1;i<=num;i++){
		opt[i-1]=i;
		randomOpt[i-1]=0;
	  }
	  var j=1;
		
	  for(j=1;j<=num;j++){
		//alert("j="+j+" num="+num);
		var m=j+rand;
		var k=random(m,1,9);
		ind=0;
		var l=0;

		for (l=0;ind<k;){
			//alert("l="+l+" ind="+ind+" k="+k);
			if (opt[mod(l,num)]!=-1){
				ind++;
			}
			if (ind<k){
				l++;
			}
		}
		randomOpt[j]=opt[mod(l,num)];
		opt[mod(l,num)]=-1;
	   }
	   return randomOpt;
	}

	function getRandomValue(order,maxValue,random){
		var randArray = getRandomArray(maxValue, random);
		return randArray[order];
	}

	function getRandomValueRandomizable(order,maxValue,randomizable){
		if (isSectionRandomOrder()){
			var random = parseInt(getSectionOrder());
			var randArray = getRandomArrayRandomizable(maxValue, random, randomizable);
			return randArray[order];
		} else {
			return order;
		}
	}

	function getSectionRandomValueRandomizable(order,maxValue,randomizable){
		if (isSectionRandomOrder()){
			var random = parseInt(getSectionOrder());
			var randArray = getRandomArrayRandomizable(maxValue, random,randomizable);
			alert("getSectionRandomValueRandomizable="+(randArray[order]));
			return randArray[order];
		} else {
			return order;
		}
		
	}

	function getRealValueRandomizable(randomValue,maxValue,randomizable){
		//alert("getRealValueRandomizable");
		if (isSectionRandomOrder()){
			var random = parseInt(getSectionOrder());
			var randArray = getRandomArrayRandomizable(maxValue, random,randomizable);
			for(i=0;i<=maxValue;i++){
				if (randArray[i]==randomValue){
					//alert("getRealValueRandomizable="+i);
					return i;
				}
			}
			return 0; //No hauria de passar mai
		} else {
			return randomValue;
		}
	}

	function isSectionRandomOrder(){ //Albert
		var v = getQueryParam('order_sections');
		if (v=="" || v=="0"){
			return false;
		} else {
			return true;
		}
	}

	function getItemOrder(){ //Albert
		if (item_order==-2){ //Desconegut
			//alert("item_order desconegut");
			if (isItemRandomOrder()){
				//alert("ha de ser aleatori");
				var s = getQueryParam('item_order');
				if (s==""){ //No s'ha establert encara
					//alert("No s'ha establert encara");
					item_order = parseInt((Math.random()*79))+1; //S'ordena
				} else {
					//alert("Ja estava establert a "+s);
					item_order = s;
				}
			} else {
				//alert("No s'ordena");
				item_order = -1; //No s'ordena
			}
		} 

		//alert("item_order="+item_order);
		if (item_order==-1){
			return parseInt(0);
		} else {
			return parseInt(item_order);
		}
	}

	function getSectionOrder(){ //Albert
		if (section_order==-2){ //Desconegut
			//alert("section_order desconegut");
			if (isSectionRandomOrder()){
				//alert("ha de ser aleatori");
				var s = getQueryParam('section_order');
				if (s==""){ //No s'ha establert encara
					//alert("No s'ha establert encara");
					section_order = parseInt((Math.random()*79))+1; //S'ordena
				} else {
					//alert("Ja estava establert a "+s);
					section_order = s;
				}
			} else {
				//alert("No s'ordena");
				section_order = -1; //No s'ordena
			}
		} 

		////alert("section_order="+section_order);
		if (section_order==-1){
			return parseInt(0);
		} else {
			return parseInt(section_order);
		}
	}

	function isItemRandomOrder(){ //Albert
		var v = getQueryParam('order_items');
		if (v=="" || v=="0"){
			return false;
		} else {
			return true;
		}
	}

	/*function ordenaItems(num,rand){
		if (rand==0 || !isItemRandomOrder()) return;
		ordena("item", num, rand);
	}*/
	
	function ordenaItems(num,randomizableItems){
		//<xsl:call-template name="getRandomizableSections"></xsl:call-template>
		//alert("orderna_items");
		var rand = getItemOrder();
		if (rand==0 || !isItemRandomOrder()) return;
		ordenaRandomizable("item", num, rand, randomizableItems);
	}
	
	function ordenaSections(num, randomizableSections){
		//<xsl:call-template name="getRandomizableSections"></xsl:call-template>
		var rand = getSectionOrder();
		if (rand==0 || !isSectionRandomOrder()) return;
		ordenaRandomizable("section_title", num, rand, randomizableSections);
		ordenaRandomizable("section_state", num, rand, randomizableSections);
		ordenaRandomizable("section_attempts", num, rand, randomizableSections);
		ordenaRandomizable("section_score", num, rand, randomizableSections);
		ordenaRandomizable("section_time", num, rand, randomizableSections);
	}

	function ordenaRandomizable(id,num,randStr,randomizable){ //Aquesta funció ha de reemplaçar ordena
            //alert("rand="+rand);
	    
		var rand = parseInt(randStr);
		var index = 0;
		var opt=new Array(num);
		for(i=1;i<=num;i++){
			s=id+"_opt"+i;
			var t=document.getElementById(s);
			if (t==null) return;
			opt[i-1] = t.innerHTML;
			opt[i-1] = procesaApplets(opt[i-1]);
		}
		
		var randArray = getRandomArrayRandomizable(num, rand, randomizable);
		
		var j=1;
		for(j=1;j<=num;j++){
			var s=id+"_opt"+j;
			var obj=document.getElementById(s);
			if (obj!=null){
				obj.innerHTML=opt[randArray[j]-1];
				//alert(s+" <-- "+(randArray[j]));
			}
		}
	}

	function ordena(id,num,randStr){
            //alert("rand="+rand);
	    
		/*var rand = parseInt(randStr);
		var index = 0;
		var opt=new Array(num);
		for(i=1;i<=num;i++){
			s=id+"_opt"+i;
		  var t=document.getElementById(s);
                  if (t==null) return;
			opt[i-1]=t.innerHTML;
			opt[i-1] = procesaApplets(opt[i-1]);
		}
		var j=1;
		for(j=1;j<=num;j++){
			var m=j+rand;
			var k=random(m,1,9);
			var s=id+"_opt"+j;
			var obj=document.getElementById(s);
			if (obj!=null){
				index=0;
				var l=0;
				for (l=0;index<k;){
					if (opt[mod(l,num)]!=null){
						index++;
					}
					if (index<k){
						l++;
					}
				}
				obj.innerHTML=opt[mod(l,num)];
				opt[mod(l,num)]=null;
				//alert(s+" <-- "+(mod(l,num)+1));
			}
		}*/
		
		var rand = parseInt(randStr);
		var index = 0;
		var opt=new Array(num);
		for(i=1;i<=num;i++){
			s=id+"_opt"+i;
			var t=document.getElementById(s);
			if (t==null) return;
			opt[i-1] = t.innerHTML;
			opt[i-1] = procesaApplets(opt[i-1]);
		}
		
		var randArray = getRandomArray(num, rand);
		
		var j=1;
		for(j=1;j<=num;j++){
			var s=id+"_opt"+j;
			var obj=document.getElementById(s);
			if (obj!=null){
				obj.innerHTML=opt[randArray[j]-1];
				//alert(s+" <-- "+(randArray[j]));
			}
		}
	}

	function procesaApplets(html){
		var m=html.indexOf("writeJavaApplet");
		if (m>0){
			var ini = m+16;
			coma1 = nextComma(ini-1, html);
			var p1 = eval(html.substring(ini, coma1));
			coma2 = nextComma(coma1, html);
			var p2 = eval(html.substring(coma1+1, coma2));
			coma3 = nextComma(coma2, html);
			var p3 = eval(html.substring(coma2+1, coma3));
			coma4 = nextComma(coma3, html);
			var p4 = eval(html.substring(coma3+1, coma4));
			coma5 = nextComma(coma4, html);
			var p5 = eval(html.substring(coma4+1, coma5));
			coma6 = nextComma(coma5, html);
			var p6 = eval(html.substring(coma5+1, coma6));
			coma7 = nextComma(coma6, html);
			var p7 = eval(html.substring(coma6+1, coma7));
			coma8 = nextComma(coma7, html);
			var p8 = eval(html.substring(coma7+1, coma8));
			coma9 = nextComma(coma8, html);
			var p9 = eval(html.substring(coma8+1, coma9));
			coma10 = nextComma(coma9, html);
			var p10 = eval(html.substring(coma9+1, coma10));
			coma11 = html.indexOf(")", coma10);
			var p11 = eval(html.substring(coma10+1, coma11));

			var appletString = writeJavaAppletString(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
			var s = html.indexOf("<APPLET ");
			var t = html.indexOf("</APPLET>");
			if (s>0 && t>0){
				html = html.substring(0,s)+appletString+html.substring(t+9);
			}
		}
		return html;
	}

	function nextComma(lastComma, s){
		var param = "";
		var j = s.indexOf("'", lastComma+1);
		var k = s.indexOf(",", lastComma+1);
		if (j<0 || k<j){
			return k;
		} else { //Hi ha com a mínim un apostrof abans de la següent coma
			var l = s.indexOf("'", j+1);
			return nextComma(l, s);
		}
	}
