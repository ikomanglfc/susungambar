anime({
    targets: '.lead-head > span:nth-child(1)',
    translateX: ['-120%',0],
    duration: 1000
});
anime({
    targets: '.lead-head > span:nth-child(2)',
    translateX: ['120%',0],
    duration: 1000,
    delay: 250
});
anime({
    targets: '.lead-box',
    opacity: [0,1],
    borderTopLeftRadius: [0,50],
    borderTopRightRadius: [0,50],
    duration: 1500,
    delay: 750
});
anime({
    targets: '.lead-box-body ol li',
    translateX: [400,0],
    delay: anime.stagger(100, {start: 1500})
});
anime({
    targets: '.lead-box-body-out',
    translateY: ['120%',0],
    duration: 1500,
    delay: 2500
});