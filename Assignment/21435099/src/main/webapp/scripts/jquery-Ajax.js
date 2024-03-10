$(function() {
    var $films = $('#film-table');
    var $pages = $('#film-pagin-head');
    
    $(window).on('load', function(){
		addWelcomeCardToScreen();
	});
    
    $("#search-button").on('click', function(){
		searchNew(1);	
	});
    
    $("#add-button").click(addFilm);
    
    $films.delegate('.delete-film', 'click', function(){
		deleteFilm(this);
	});
	
    $films.delegate('.edit-film', 'click', function(){
		editFilm(this);
	});
	
	$films.delegate('.dec-edit', 'click', function(){
		declineEditFilm(this);
	});
	
	$films.delegate('.cnf-edit', 'click', function(){
		confirmEditFilm(this);
	});
	
	$films.delegate('.cnf-add', 'click', function(){
		confirmAddFilm(this);
	});
	
	$films.delegate('.dec-add', 'click', function(){
		declineAddFilm();
	});
	
	$pages.delegate('.btn-page', 'click', function(){
		getDataFromPageNumber(this);
	});
    
});

function getDataType(){
	var dataTypeInput = $('#dataType').val();
	let dataType;
	
	if(dataTypeInput == 0)
		dataType = "error";
	if(dataTypeInput == 1)
		dataType = "json";
	if(dataTypeInput == 2)
		dataType = "xml";
	if(dataTypeInput == 3 )
		dataType = "text";
	
	return(dataType);
}

function dataTypeError(){
	$('#dataType').addClass('error');
}

function isANumber(id){
	if(isNaN(id))
		return(0);
	return(1);
}

function idInputError(){
	$('#searchId').addClass('error');
}

function searchNew(){
	var queryString = getQueryString();
	
	searchFilm(1, queryString);
}

function getQueryString(){
	var searchTypeInput = $('#searchType').val();
	var searchID = escape($("#searchId").val())
	var searchName = escape($("#searchName").val())
	let queryString;
	
	if(searchTypeInput == 0)
		queryString = 'error';
	if(searchTypeInput == 1)
		queryString = "q=af&pg=";
	if(searchTypeInput == 2){
		var check = isANumber(searchID);
		if(check == 0)
			queryString = 'error1';
		else
			queryString = "id=" + searchID + "&pg=";
	}
	if(searchTypeInput == 3)
		queryString = "title=" + searchName + "&pg=";
		
	return(queryString);
	
}

function searchTypeError(){
	$('#searchType').addClass('error');
}

function searchFilm(currentPage, qString) {

	$('#dataType').removeClass('error');
	$('#searchType').removeClass('error');
	$('#searchId').removeClass('error');
	$('#film-pagin-head').empty();
	$('#film-table div').empty();
	$('#film-pagin-foot').empty();
	
	var dataType = getDataType();	
	var qStringNoPage = qString;
	var queryString = qString + currentPage;
	
	if(queryString == 'error')
		searchTypeError();
	else if(dataType == 'error')
		dataTypeError();
	else if(queryString == 'error1')
		idInputError();
	else{
		$.ajax({
			type: 'GET',
			url: "filmapi?" + queryString,
			dataType: dataType,
			success: function(data){
				deserializeFromDataType(dataType, data, currentPage, qStringNoPage);
			}
		});
	}
}

function addFilm() {

	$('#film-table div').empty();
	$('#film-pagin-head').empty();
	$('#film-pagin-foot').empty();
	addAddFilmCardToScreen();
}

function deserializeFromDataType(dataType, filmList, currentPage, qString){
	if(dataType === "json"){
		getFilmsFromJson(filmList, currentPage, qString);
	} else if (dataType === "xml"){
		getFilmsFromXML(filmList, currentPage, qString);
	} else {
		getFilmsFromText(filmList, currentPage, qString);
	}
}

function getFilmsFromJson(filmList, currentPage, qString){
	var films = filmList.films;
	$.each(films, function(i, film){
		addFilmsToPageFromJSON(film);
	});
	
	var numFilms = parseInt(filmList.numFilms);
	
	addPagination(numFilms, currentPage, qString);
}

function addFilmsToPageFromJSON(film){
	var title = film.title;
	var year = film.year;
	var id = film.id;
	var director = film.director;
	var stars = film.stars;
	var review = film.review;
	
	addDataToPage(title, year, id, director, stars, review);
}

function getFilmsFromXML(filmList, currentPage, qString){
	$(filmList).find('films film').each(function(){
		var title = $(this).find('title').text();
		var year = $(this).find('year').text();
		var id = $(this).find('id').text();
		var director = $(this).find('director').text();
		var stars = $(this).find('stars').text();
		var review = $(this).find('review').text();
		
		addDataToPage(title, year, id, director, stars, review);
	});
	
	var numFilms = parseInt($(filmList).find('numFilms').text());
	
	addPagination(numFilms, currentPage, qString);
}

function getFilmsFromText(filmList, currentPage, qString){
	var data = filmList.split("£&");
	
	var films = data[1].split("£%");
	$(films).each(function(){
		addFilmsToPageFromText(this);
	});
	
	var numFilms = data[0];
	
	addPagination(numFilms, currentPage, qString);
}

function addFilmsToPageFromText(film){
	var variables = film.split("%&");
	var id = variables[0].split("&&");
	var title = variables[1].split("&&");
	var year = variables[2].split("&&");
	var director = variables[3].split("&&");
	var stars = variables[4].split("&&");
	var review = variables[5].split("&&");
	
	addDataToPage(title[1], year[1], id[1], director[1], stars[1], review[1]);
}

function addWelcomeCardToScreen(){
	var $films = $('#film-table');
	
	$films.append(
		'<div class="crd-mrg" id="crd-welcome">' +	
			'<div class="card">' +
				'<div class="card-body cb-wel">' +
					'<br>' +
					'<div class="welcome-txt-hd">' +
						'Welcome to MMU-IMDB!' +
					'</div>' +
					'<div class="welcome-txt-bd">' +
						'Please use the navigation bar at the top to search the database for films or to add a new film' +
					'</div>' +
					'<br>' +
				'</div>' +
			'</div>' +
		'</div>'
	);
}



function addDataToPage(title, year, id, director, stars, review){
	var $films = $('#film-table');
	
	$films.append(
		'<div class="crd-mrg" id="crd-id-' + id +'">' +	
			'<div class="card">' +
				'<div class="card-header ch">' +
					'<h5>'+
						'<span class="noedit title" id="title"><i class="bi bi-camera-reels"> </i>' + title + ' ' + '</span><span class="noedit year id="year"><i>(' + year + ')</i></span>' +
						'<label class="edit">Title: </label><input class="form-control form-control-sm edit title f-header-input" id="edit-title" value="' + title + '"/><label class="edit"> Year: </label><input class="form-control form-control-sm edit year f-header-input" id="edit-year" value="' + year + '"/>' +
						'<div class="btn-edit">' +
							'<button type = "button" data-id="' + id + '" class="btn btn-outline-light btn-sm edit-film">' +
								'<i class="bi bi-pencil-square"></i>' +
							'</button>' +
						'</div>' +
						'<div class="btn-delete">' +
							'<button type = "button" data-id="' + id + '" class="btn btn-outline-light btn-sm delete-film">' +
								'<i class="bi bi-trash"></i>' +
							'</button>' +
						'</div>' +
					'</h5>' +
				'</div>' +
				'<div class="card-body cb">' +
					'<span class="noedit director" id="director"><i>' + director + '</i></span>' +
					'<label class="edit">Director: </label><input class="form-control form-control-sm edit director" id="edit-director" value="' + director + '"/><br>' +
					'<span class="noedit stars" id="stars"><b>Starring: </b>' + stars + '</span>' +
					'<label class="edit">Stars: </label><input class="form-control form-control-sm edit stars" id="edit-stars" value="' + stars + '"/><br>' +
					'<span class="noedit review"id="review"><b>Review: </b>' + review + '</span>' +
					'<label class="edit">Review: </label><br><textarea name="Text1" style="width:100%;" rows="6" id="edit-review" class=" edit review">' + review + '</textarea><br class="edit"><br class="edit">' +
					'<button type = "button" data-id="' + id + '" class="btn btn-outline-dark btn-sm cnf-edit btn-brown edit">' +
								'<i class="bi bi-check-circle"></i>' +
					'</button>' +
					'<button type = "button" data-id="' + id + '" class="btn btn-outline-dark btn-sm btn-brown dec-edit edit">' +
								'<i class="bi bi-x-circle"></i>' +
					'</button>' +
					'<br class="edit"><br class="edit">' +
				'</div>' +
			'</div>' +
		'</div>'
	);
}

function addAddFilmCardToScreen(){
	var $films = $('#film-table');
	
	$films.append(
		'<div class="crd-mrg" id="crd-add">' +	
			'<div class="card">' +
				'<div class="card-header ch">' +
					'<h5>'+
						'<i class="bi bi-camera-reels"></i> Add a new film to MMUIMDB!' +
					'</h5>' +
				'</div>' +
				'<div class="card-body cb">' +
					'<label>Title: </label><input class="form-control form-control-sm title" id="add-title"/><br>' +
					'<label>Year: </label><input class="form-control form-control-sm year" id="add-year"/><br>' +
					'<label>Director: </label><input class="form-control form-control-sm director" id="add-director"/><br>' +
					'<label>Stars: </label><input class="form-control form-control-sm stars" id="add-stars"/><br>' +
					'<label>Review: </label><br><textarea name="Text1" style="width:100%;" rows="6" id="add-review" ></textarea><br><br>' +
					'<button type="button" class="btn btn-outline-dark btn-sm btn-brown cnf-add">' +
								'<i class="bi bi-check-circle"></i>' +
					'</button>' +
					'<button type="button" class="btn btn-outline-dark btn-sm btn-brown dec-add">' +
								'<i class="bi bi-x-circle"></i>' +
					'</button>' +
					'<br><br>' +
				'</div>' +
			'</div>' +
		'</div>'
	);
}

function encodeQueryStringToButton(qString){
	
	var querys = qString.split('&');
	var search = querys[0].split('=');
	var pages = querys[1].split('=');
	
	var queryString = search[0] + '&&' + search[1] + '££' + pages[0] + '&&';
	return(queryString);
}

function decodeQueryStringFromButton(qString){
	var querys = qString.split('££');
	var search = querys[0].split('&&');
	var pages = querys[1].split('&&');
	
	var queryString = search[0] + '=' + search[1] + '&' + pages[0] + '=';
	return(queryString);
}

function getPageNumberFromButton(qString){
	var querys = qString.split('££');
	var pages = querys[1].split('&&');
	var pageNum = parseInt(pages[1]);
	return(pageNum);
}

function getDataFromPageNumber(button){
	
	var $buttonData = $(button).attr('data-id');
	var $pageNum = getPageNumberFromButton($buttonData);
	var $queryString = decodeQueryStringFromButton($buttonData);
	
	searchFilm($pageNum, $queryString);
	
}

function addPagination(numFilms, currentPage, qString){
	
	var $paginhead = $('#film-pagin-head');
	var $paginfoot = $('#film-pagin-foot');
	var numPages = Math.trunc(numFilms / 10) + 1;
	
	var queryString = encodeQueryStringToButton(qString);
	
	$paginhead.append(
		paginationHeader() +
		paginationFirst(numPages, currentPage, queryString) +
		paginationBody(numPages, currentPage, queryString) +
		paginationLast(numPages, currentPage, queryString) +
		paginationFooter()
	);
	
	$paginfoot.append(
		paginationHeader() +
		paginationFirst(numPages, currentPage, queryString) +
		paginationBody(numPages, currentPage, queryString) +
		paginationLast(numPages, currentPage, queryString) +
		paginationFooter()
	);
}

function paginationHeader(){
	var header = '' +
		'<nav>' +
  			'<ul class="pagination justify-content-end pag-mrg">';
	
	return(header);
}


function paginationFirst(numPages, currentPage, queryString){
	var first = '' +
		'<li class="page-item">' +
  			'<button type="button" class="page-link btn-page btn-brown" data-id="' + queryString + '1">First</button>' +
		'</li>';
		
	if(numPages > 5 && currentPage > 3){
		return(first);
	} else {
		return('');
	}
}

function paginationBody(numPages, currentPage, queryString){
	var body = '';
	
	if(numPages < 5){
		for(let i = 1; i <= numPages; i++){
			let active = '';
			if(i == currentPage){
				active = 'active';
			}
			var button = '<li class="page-item">' +
					'<button type="button" class="page-link btn-brown btn-page ' + active + '" data-id="' + queryString + i + '">' +
					 	i +
				 	'</button>' +
			 	'</li>';
		 	body += button;
		}
	}
	
	if(currentPage < 3 && numPages > 5){
		for(var i = 1; i <= 5; i++){
			let active = '';
			if(i == currentPage){
				active = 'active';
			}
			var button = '<li class="page-item">' +
					'<button type="button" class="page-link btn-brown btn-page ' + active + '" data-id="'+ queryString + i + '">' +
					 	i +
				 	'</button>' +
			 	'</li>';
		 	body += button;
		}
	}
	
	if(currentPage >= 3 && numPages > 5 && currentPage < (numPages-2)){
		for(let i = (currentPage -2); i <= (currentPage + 2); i++){
			let active = '';
			if(i == currentPage){
				active = 'active';
			}
			var button = '<li class="page-item">' +
					'<button type="button" class="page-link btn-brown btn-page ' + active + '" data-id="' + queryString + i + '">' +
					 	i +
				 	'</button>' +
			 	'</li>';
		 	body += button;
		}
	}
	
	if(currentPage >= 3 && numPages > 5 && currentPage > (numPages-2)){
		for(let i = numPages -5; i <= numPages; i++){
			let active = '';
			if(i == currentPage){
				active = 'active';
			}
			var button = '<li class="page-item">' +
					'<button type="button" class="page-link btn-brown btn-page ' + active + '" data-id="' + queryString + i + '">' +
					 	i +
				 	'</button>' +
			 	'</li>';
		 	body += button;
		}
	}

	return(body);
}

function paginationLast(numPages, currentPage, queryString){
	var last = '' +
		'<li class="page-item">' +
  			'<button type="button" data-id="' + queryString + numPages + '" class="page-link btn-page btn-brown">Last</button>'
		'</li>';
		
	if(numPages > 5 && currentPage < (numPages -2)){
		return(last);
	} else {
		return('');
	}
}

function paginationFooter(){
	var foot = '' +
		'</ul>' +
			'</nav>';
			
	return(foot);
}



/*
*Rest API works by receiving a film in the specified data format in the header
*need to be able to build that object before sending it to the API
*need a function to build JSON, XML and Text - not only that, but buttonclick for delete will only have
*data on ID of film, so will need to make a mock film with only the ID relevant as thats all thats required
*for the SQL purpose of deleting
*/

function deleteFilm(card){

	$('#dataType').removeClass('error');
	
	var $id = $(card).attr('data-id');
	var cardid = '#crd-id-' + $id;
	var $toRemove = $(cardid);
	var dataType = getDataType();
	let filmToDelete;
	
	if(dataType == 'error')
		dataTypeError();
	else{
		if(dataType == "json")
			filmToDelete = dataToJsonFilm($id, "undefined", "9999", "undefined", "undefined", "undefined");
		if(dataType == "xml")
			filmToDelete = dataToXmlFilm($id, "undefined", "9999", "undefined", "undefined", "undefined");
		if(dataType == "text")
			filmToDelete = dataToTextFilm($id, "undefined", "9999", "undefined", "undefined", "undefined");
		
		
		$.ajax({
			type: 'DELETE',
			url: "filmapi",
			dataType: dataType,
			data: filmToDelete,
			contentType: "text/plain",
			success: function(){
				$toRemove.fadeOut(300, function(){
					$(this).empty();
				});
			},
			error: function(){
				console.log("wtf?");
			}
		}); 
	}
}

function editFilm(card){
	var $id = $(card).attr('data-id');
	var cardid = '#crd-id-' + $id;
	var $toEdit = $(cardid);
	
	$toEdit.addClass('edit');
}

function declineEditFilm(card){
	var $id = $(card).attr('data-id');
	var cardid = '#crd-id-' + $id;
	var $toEdit = $(cardid);
	
	$toEdit.removeClass('edit');
}

function declineAddFilm(){
	
	$('#film-table div').empty();
	addWelcomeCardToScreen();
}


function confirmEditFilm(card){
	$('#edit-title').removeClass('error');
	$('#edit-year').removeClass('error');
	$('#edit-director').removeClass('error');
	$('#edit-stars').removeClass('error');
	$('#edit-review').removeClass('error');
	$('#dataType').removeClass('error');
	
	
	var $id = $(card).attr('data-id');
	var cardid = '#crd-id-' + $id;
	var $toEdit = $(cardid);
	
	var $title=$toEdit.find('#edit-title').val();
	var $year=$toEdit.find('#edit-year').val()
	var $director=$toEdit.find('#edit-director').val();
	var $stars=$toEdit.find('#edit-stars').val();
	var $review=$toEdit.find('#edit-review').val();
	
	var dataType = getDataType();
	let filmToEdit;
	
	if($title === "")
		$('#edit-title').addClass('error');
	else if(isANumber($year) == 0 || $year === "")
		$('#edit-year').addClass('error');
	else if($director === "")
		$('#edit-director').addClass('error');
	else if($stars === "")
		$('#edit-stars').addClass('error');
	else if($review === "")
		$('#edit-review').addClass('error');
	else if(dataType == 'error')
		dataTypeError();
	else{
	
		if(dataType == "json")
			filmToEdit = dataToJsonFilm($id, $title, $year, $director, $stars, $review);
		if(dataType == "xml")
			filmToEdit = dataToXmlFilm($id, $title, $year, $director, $stars, $review);
		if(dataType == "text")
			filmToEdit = dataToTextFilm($id, $title, $year, $director, $stars, $review);
		
		$.ajax({
			type: 'PUT',
			url: "filmapi",
			dataType: dataType,
			data: filmToEdit,
			contentType: "text/plain",
			success: function(){
				
				$toEdit.find('#title').html('<i class="bi bi-camera-reels"> </i>' + $title + ' ');
				$toEdit.find('#year').html('<i>"(' + $year + ')</i>');
				$toEdit.find('#director').html('<i>' + $director + '</i>');
				$toEdit.find('#stars').html('<b>Starring: </b>' + $stars);
				$toEdit.find('#review').html('<b>Review: </b>' + $review);
				$toEdit.removeClass('edit');
				
			},
			error: function(){
				console.log("wtf?");
			}
		});
	}
}


function blankTitleError(){
	$('#add-title').addClass('error');
}

function confirmAddFilm(){
	
	$('#add-title').removeClass('error');
	$('#add-year').removeClass('error');
	$('#add-director').removeClass('error');
	$('#add-stars').removeClass('error');
	$('#add-review').removeClass('error');
	$('#dataType').removeClass('error');
	
	var $toAdd = $('#crd-add');
	
	var $title=$toAdd.find('#add-title').val();
	var $year=$toAdd.find('#add-year').val()
	var $director=$toAdd.find('#add-director').val();
	var $stars=$toAdd.find('#add-stars').val();
	var $review=$toAdd.find('#add-review').val();
	var $id = "9999"; 
	var queryString = "title=" + $title + "&pg=1";
	
	var dataType = getDataType();
	
	let filmToEdit;
	
	if($title === "")
		$('#add-title').addClass('error');
	else if(isANumber($year) == 0 || $year === "")
		$('#add-year').addClass('error');
	else if($director === "")
		$('#add-director').addClass('error');
	else if($stars === "")
		$('#add-stars').addClass('error');
	else if($review === "")
		$('#add-review').addClass('error');
	else if(dataType == 'error')
		dataTypeError();
	else{
		
		if(dataType == "json")
			filmToEdit = dataToJsonFilm($id, $title, $year, $director, $stars, $review);
		if(dataType == "xml")
			filmToEdit = dataToXmlFilm($id, $title, $year, $director, $stars, $review);
		if(dataType == "text")
			filmToEdit = dataToTextFilm($id, $title, $year, $director, $stars, $review);
			
			console.log(filmToEdit);

		$.ajax({
			type: 'POST',
			url: "filmapi",
			dataType: dataType,
			data: filmToEdit,
			contentType: "text/plain",
			success: function(){
				$.ajax({
					type: 'GET',
					url: "filmapi?" + queryString,
					dataType: dataType,
					success: function(data){
						$('#film-table div').empty();
						deserializeFromDataType(dataType, data);
					}
				});
			},
			error: function(){
				console.log("wtf?");
			}
		});
	}
}


function dataToJsonFilm(id, title, year, director, stars, review){
	
	var film = {
		"id": parseInt(id),
		"title": title,
		"year": parseInt(year),
		"director": director,
		"stars": stars,
		"review": review
	}
	
	return(JSON.stringify(film));
}

function dataToXmlFilm(id, title, year, director, stars, review){
	
	 var film = '' +
	 '<film>' +
     	'<director>' + director + '</director>' +
     	'<id>' + id + '</id>' +
    	'<review>' + review + '</review>' +
        '<stars>' + stars + '</stars>' +
        '<title>' + title + '</title>' +
        '<year>' + year + '</year>' +
     '</film>';
     
     return(film);
}

function dataToTextFilm(id, title, year, director, stars, review){
	
	var film = '' +
		'id&&' + id + '%&' +
		'title&&' + title + '%&' +
		'year&&' + year + '%&' +
		'director&&' + director + '%&' +
		'stars&&' + stars + '%&' +
		'review&&' + review;
		
	return(film); 
}