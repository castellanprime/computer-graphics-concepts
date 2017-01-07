function showAllLinks(containing){
	var navLinksData = { links:[ 
			{link:"applied geometry", content:["PythagoreanTree", "FindingTangent"]}, 
			{link:"classic algorithms", content:["Line-drawing", "Circles"]}, 
			{link:"fractals", content:["MandelbrotSet"]}, 
			{link:"transforms", content:["GeometricTransforms", "3DTransforms"]}, 
			{link:"others", content:["RayTracing"]}]
		};
	var text = [];
	if (containing == "applied"){
		var i=0;
		for (;i<navLinksData.links[0].content.length;i++){
			text.push(navLinksData.links[0].content[i]);	
		}
	}else if (containing == "classic"){
		var i=0;
		for (;i<navLinksData.links[1].content.length;i++){
			text.push(navLinksData.links[1].content[i]);	
		}
	}else if(containing == "fractals"){
		var i=0;
		for (;i<navLinksData.links[2].content.length;i++){
			text.push(navLinksData.links[2].content[i]);	
		}
	}else if (containing == "transforms"){
		var i=0;
		for (;i<navLinksData.links[3].content.length;i++){
			text.push(navLinksData.links[3].content[i]);	
		}
	} else if (containing == "others"){
		var i=0;
		for (;i<navLinksData.links[4].content.length;i++){
			text.push(navLinksData.links[4].content[i]);	
		}
	}
	var d = document.getElementById('navLinks');
	console.log(d.innerHTML);
	console.log(text);
	console.log(d.children.length);

	
	if (d.children.length > 0){
		var n=0;
		for(;n<d.children.length;n++){
			d.removeChild(d.children[n]);	// removeChild does not remove all the children
		}
		d.removeChild(d.lastChild);
	}

	console.log(d.children.length);
	var counter=0;
	for (;counter<text.length;counter++){
		var aTag = document.createElement('a');
		aTag.setAttribute('href',"#");
		var instructStr = "showInstructions(" + "'"+text[counter] + "'" + ")";
		aTag.setAttribute('onclick',instructStr);
		aTag.classList.add('links');
		aTag.textContent=text[counter];
		d.appendChild(aTag);
	}
	console.log(d.innerHTML);
	console.log(d.children.length);
}

function showInstructions(instruct){

	var instructionLinks = ["PythagoreanTree", "FindingTangent", "Line-drawing", "Circles", "MandelbrotSet", "GeometricTransforms", 
						"3DTransforms", "RayTracing"];
	
	var instructionMap = new Map();

	instructionMap.set(0, "Click two points on the canvas. Make sure that they are relatively close.");
	instructionMap.set(1, "Click two points on the canvas. Enter the values Ox Oy r Px Py");
	instructionMap.set(2, "Click two points on the canvas. Cool.");
	instructionMap.set(3, "Click two points on the canvas. Cool.");
	instructionMap.set(4, "Click two points on the canvas. Nice.");
	instructionMap.set(5, "Click two points on the canvas. Mkertysasa.");
	instructionMap.set(6, "Click two points on the canvas. Mnawqfqe.");
	instructionMap.set(7, "Click two points . atively close.");

	var contentToDisplay = "";
	if (instructionLinks.indexOf(instruct) > -1){
		contentToDisplay += instructionMap.get(instructionLinks.indexOf(instruct));
	}

	var d1 = document.getElementById('instructions');
	d1.innerHTML= "<p>" + contentToDisplay + "</p>";
	d1.classList.add('instructStyle');
	console.log(d1.innerHTML); 
}