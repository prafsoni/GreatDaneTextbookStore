// JavaScript Document

$(function () {		
		$("ul.sf-menu").superfish({ 
        animation: {height:'show'},   // slide-down effect without fade-in 
        delay:    300,              // 1.2 second delay on mouseout 
        autoArrows:  false,
        speed: 300
    });
	
	// Create the dropdown base
      $("<select />").appendTo("nav");
      
      // Create default option "Go to..."
      $("<option />", {
         "selected": "selected",
         "value"   : "",
         "text"    : "Go to..."
      }).appendTo("nav select");
      
      // Populate dropdown with menu items
      $("nav a").each(function() {
       var el = $(this);
       $("<option />", {
           "value"   : el.attr("href"),
           "text"    : el.text()
       }).appendTo("nav select");
      });
      
	   // To make dropdown actually work
	   // To make more unobtrusive: http://css-tricks.com/4064-unobtrusive-page-changer/
      $("nav select").change(function() {
        window.location = $(this).find("option:selected").val();
      });
	  
	  // FLEXSLIDER //
	
	$('.flexslider').flexslider({
    	animation: "fade",
    	controlNav: false,
    	keyboardNav: true,
		directionNav: false
    });
	
	// TWITTER WIDGET //
	$("#tweets").tweet({
        count: 2,
        username: "esksidedesign"
    });
	
})
	
	// Bottom Widget //

	var topContainer = $(".bottom-widget");
	var topTrigger = $(".bottom-open");
	
	topTrigger.click(function(){
		topContainer.animate({
			height: 'toggle'
		});
		
		if( topTrigger.hasClass('tab-closed')){
			topTrigger.removeClass('tab-closed');
		}else{
			topTrigger.addClass('tab-closed');
		}
		
		return false;
		
	});
