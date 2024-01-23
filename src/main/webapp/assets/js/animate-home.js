function animateBanner() {
    return anime({
        targets: '.home-banner',
        translateY: [-500, 0],
        duration: 1500,
        autoplay: false
    });
}

function animateHome() {
    return anime({
        targets: '.home-button button',
        translateX: [-350, 0],
        delay: anime.stagger(100, {start: 1000}),
        autoplay: false
    });
}