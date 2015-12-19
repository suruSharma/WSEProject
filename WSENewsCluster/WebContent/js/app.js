/**
 * Javscript file for ParallelPosts.html
 */

var maxClusters = 5; //maximum number of allowable clusters (has to be between 5 and 20, default is 5)
var region = 1;

//Icons that are displayed in the header of the accordion
var icons = {
		header: "ui-icon-circle-arrow-e",
		activeHeader: "ui-icon-circle-arrow-s"
};

//Function to set the accordion
$(function() {
    $( "#accordion" ).accordion({
    	icons: icons,
    	collapsible: true,
    	heightStyle: "content"
    });
});

$(document).ready(function() {
	getLocation();
	
	$('#clusterCountVal').on('input', validateCount);
	$('#clusterCountButton').on('click', updateClusterCount);
	$('#indiaButton').on('click', getIndiaNews);
	$('#worldButton').on('click', getWorldNews);
	//fetchData();
});

function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition, showError);
    } else {
        console.log("Geo location is not supported by this browser");
    }
}
function showPosition(position) {
    var latlon = position.coords.latitude + "," + position.coords.longitude;
    console.log(latlon);
    console.log("Fetching location");
	jQuery.ajax({ 
		  url: 'http://ws.geonames.org/countryCode?lat='+position.coords.latitude+'&lng='+position.coords.longitude+'&username=suruchisharma',
		  type: 'GET', 
		  dataType: 'json',
		  success: function(e) {
			var regionResponse = e.responseText;
			toggleRegion(regionResponse);
			console.log(e.responseText);
		  },
		  error:function(e)
		  {
			var regionResponse = e.responseText;
			toggleRegion(regionResponse);
			console.log(e.responseText);
		  }
	});
}
function showError(error) {
    switch(error.code) {
        case error.PERMISSION_DENIED:
            console.log("User denied the request for Geolocation.");
            break;
        case error.POSITION_UNAVAILABLE:
        	console.log("Location information is unavailable.");
            break;
        case error.TIMEOUT:
        	console.log("The request to get user location timed out.");
            break;
        case error.UNKNOWN_ERROR:
        	console.log("An unknown error occurred.");
            break;
    }
    
    getIndiaNews(); //default news location
}

function toggleRegion(region)
{
	if(region === 'IN')
		getIndiaNews();
	else
		getWorldNews();
}

function getIndiaNews()
{
	$('#accordion').empty();	
	$('#indiaButton').removeClass("btn-default").addClass("btn-primary");
	$('#worldButton').removeClass("btn-primary").addClass("btn-default");
	region = 1;
	fetchData();
	
}

function getWorldNews(){
	$('#accordion').empty();	
	$('#worldButton').removeClass("btn-default").addClass("btn-primary");
	$('#indiaButton').removeClass("btn-primary").addClass("btn-default");
	region = 2;
	fetchData();
	
}

//Function to call servlet to update the cluster count
function updateClusterCount()
{
	$('#accordion').empty();	
	var input=$('#clusterCountVal');
	var isValidNumber = !isNaN(input.val());
	if(isValidNumber && input.val()!='')
	{
		if(input.val() < 1)
	    {
	    	maxClusters = 1;
	    }   	
	    else if(input.val() > 20)
	    {
	    	maxClusters = 20;
	    }
	    else
	    	maxClusters = input.val();	
	    }
	    else
	    {
	    	maxClusters = 5;
	    }
	fetchData();
}

//Function to enable/disable the Go! button
function validateCount()
{
    var input=$('#clusterCountVal');
    var isValidNumber = !isNaN(input.val());
    if(isValidNumber && input.val()!='')
    {
    	input.removeClass("invalid").addClass("valid");
        $('#clusterCountButton').prop('disabled', false);
    }
    else
    {
        input.removeClass("valid").addClass("invalid");
        $('#clusterCountButton').prop('disabled', true);
    }
}

//Executed when the servlet call is a success
function successFunction( data, textStatus, jqXHR) {
   	var clusters = data.clusters;
   	$('#accordion').empty();
   	var $bar = $('#accordion');
   	for (var i = 0; i < maxClusters; i++) {
		var data = '<h3><a href="#">'+clusters[i].clusName+'</a></h3><div><p>';
		var numberOfLinks = clusters[i].records.length;
		for(var j = 0;j<numberOfLinks;j++)
		{
			data = data + '<br><a href = "'+clusters[i].records[j].url+'" target="_blank">'+clusters[i].records[j].title+'</a>';
			data = data + '<br><p>'+clusters[i].records[j].description+'</p>';
		}
		data = data +'</p></div>';
		$bar.append(data);	
	}
   	$('#loadingImage').css("visibility", "hidden");
   	$bar.accordion('destroy').accordion({
   		icons:icons,
		collapsible: true,
		heightStyle: "content"
   	}); 
}

//Makes an Ajax call to the servlet
function fetchData()
{
	$('#loadingImage').css("visibility", "visible");
	dataString = "clusterCount=" + maxClusters+"&region="+region;
    $.ajax({
        type: "POST",
        url: "newsServlet",
        data: dataString,
        dataType: "json",        
        success: successFunction,
        error: function(jqXHR, textStatus, errorThrown){
        	console.log("There was some error");
        }
    });
}
