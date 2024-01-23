var max = $('.winner-inner div').length - 1;
var y = 22;
var item = 1;
var h = 25.5;
var fbig = 4;
var fstart = 15;
var ftime = 50;

var timer = setInterval(loop, 3000);
function loop(){
    $('#it_'+item).css('font-size', fstart + 'px');
    $('#it_'+item).css('font-weight', '300');
    item ++;
    move(y - h, 400);
    y -= h;

    if(item >= max - 2){
        clearInterval(timer);
    }
}

function move(dy, time){
    var t = ftime / time;
    var val = y;
    var fs = fstart;
    var mov = setInterval(function(){
        val -= h * t;
        fs += (fbig * t )
        if(val <= dy){
            val = dy;
            clearInterval(mov);
            fs = fstart + fbig;
        }
        $('.winner-inner').css('margin-top', val + 'px');
        $('#it_'+item).css('font-size', fs + 'px');
        $('#it_'+item).css('font-weight', '600');
    }, ftime);
}