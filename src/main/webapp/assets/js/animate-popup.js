const duration = 600;
const popupTrigger = document.querySelectorAll('.popup-trigger');

popupTrigger.forEach(el => {
    el.addEventListener("click", function(){
        popUpClicked( el.getAttribute('data-target'));
    });
});

function popUpClicked(popup){
    //let popup = el.getAttribute('data-target');
    let popupel = document.querySelector(popup);
    if(!popupel.classList.contains('active')){
        popupel.classList.add("active");
        popupel.style.display = 'block';
        popupAnimeIn(popup);
    }else{
        popupel.classList.remove("active");
        popupAnimeOut(popup);
        setTimeout(function(){
            popupel.style.display = 'none';
        },duration+200);
    }
}

function popupAnimeIn(target){
    if(document.querySelector(target+' .popup-backdrop')){
        anime({
            targets: target+' .popup-backdrop',
            duration: duration-200,
            opacity: [0,0.9],
            easing: 'easeInOutSine'
        });
    }
    anime({
        targets: target+' .popup-content',
        opacity: [0,1],
        scale: [0,1],
        duration: duration,
        easing: 'easeInOutElastic(1, .6)'
    });
}

function popupAnimeOut(target){
    if(document.querySelector(target+' .popup-backdrop')){
        anime({
            targets: target+' .popup-backdrop',
            duration: duration+200,
            opacity: [1,0],
            easing: 'easeInOutSine'
        });
    }
    anime({
        targets: target+' .popup-content',
        opacity: [1,0],
        scale: [1,0],
        duration: duration,
        easing: 'easeInOutElastic(1, .6)'
    });
}