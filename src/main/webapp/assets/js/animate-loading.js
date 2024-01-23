function animateLoading() {
    anime({
        targets: '.loading-logo',
        duration: 1000,
        scale: [0, 1],
        delay: anime.stagger(100)
    });

    anime({
        targets: '.loading-bar span',
        opacity: [0, 1],
        delay: anime.stagger(100, {start: 1000})
    });
}