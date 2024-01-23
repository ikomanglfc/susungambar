anime({
    targets: '.result h2',
    opacity: [0,1],
    scale: [0,1],
    duration: 1000,
    easing: 'easeInOutElastic(1, .6)'
});
anime({
    targets: '.result h4',
    opacity: [0,1],
    scale: [0,1],
    duration: 1000,
    delay: 250, 
    easing: 'easeInOutElastic(1, .6)'
});
anime({
    targets: '.result-circle',
    opacity: [0,1],
    scale: [0,1],
    delay: 750,
    duration: 1000,
    easing: 'easeInOutElastic(1, .6)'
});
anime({
    targets: '.result-circle-small',
    opacity: [0,1],
    scale: [0,1],
    delay: 1250,
    duration: 1000,
    easing: 'easeInOutElastic(1, .6)'
});
anime({
    targets: '.anim-result-btm',
    opacity: [0,1],
    // scale: [0,1],
    delay: 1750,
    duration: 1000
});
anime({
    targets: '.anim-result-btm button',
    scale: [0,1],
    duration: 1000,
    delay: anime.stagger(100, {start: 2000}),
    easing: 'easeInOutElastic(1, .6)'
});
anime({
    targets: '.winner',
    translateY: [100,0],
    delay: 3000,
    duration: 1000
});