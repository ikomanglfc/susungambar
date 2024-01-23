var answer = false;
var now = new Date().getTime();
var initialOffset = '220';
var i = 0;
var time = 10;

$('h6').text(time);
$('.circle_animation').css('stroke-dashoffset', initialOffset);

var interval = setInterval(function() {
    $('h6').text(time - i);
    if (i == time) {
        clearInterval(interval);
    }
    $('.circle_animation').css('stroke-dashoffset', initialOffset-((i+1)*(initialOffset/time)));
    console.log(initialOffset-((i+1)*(initialOffset/time)));
    i++;
}, 1000);