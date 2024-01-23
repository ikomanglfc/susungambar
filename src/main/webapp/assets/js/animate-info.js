anime({
    targets: '.info-banner',
    translateY: [-50,0],
    duration: 1500
});
anime({
    targets: '.info-btn',
    opacity: [0,1],
    scale: [0,1],
    duration: 1000,
    delay: 300, 
    easing: 'easeInOutElastic(1, .6)'
});
anime({
    targets: '.info-accord .tab',
    translateX: [-400,0],
    delay: anime.stagger(100, {start: 1000})
});