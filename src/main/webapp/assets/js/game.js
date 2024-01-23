
let bw;
let bh;
let dw;
let dh;

let log = '';
let hlIdx;

let done = false;

let now = new Date().getTime();
let initialOffset = 182;
let i = 0;

let interval;
let pause = false;

function timer() {
    $('.circle_animation').css('stroke-dashoffset', initialOffset);
    $('.circle_animation').css('stroke-dasharray', initialOffset);
    interval = setInterval(function () {
        if(pause) return;
        $('h6').text(time - i);
        if (i == time) {
            if (!done) {
                loose();
            }
            clearInterval(interval);
            return;
        }
        $('.circle_animation').css('stroke-dashoffset', initialOffset - ((i + 1) * (initialOffset / time)));
        i++;
    }, 1000);
}

function calculate() {
    let box = document.getElementById('puzzle-box');

    bw = Math.floor(box.clientWidth / col) * col;
    bh = Math.floor(bw * iH / iW / row) * row;

    box.style.width = bw + 'px';
    box.style.height = bh + 'px';

    dw = bw / col;
    dh = bh / row;

    let xw = 0;
    let yw = 0;
    for (let j = 0; j < images.length; j++) {
        images[j].x = xw;
        images[j].y = yw;
        xw += dw;
        if (xw >= bw) {
            yw += dh;
            xw = 0;
        }
    }
}

function imgboxAnim() {
    let box = document.getElementById('puzzle-box');
    let image;
    let html = '';
    let p = 5;
    let midx = (bw - dw + images.length * p) / 2;
    let midy = (bh - dh - images.length * p) / 2;

    for (let j = 0; j < images.length; j++) {
        image = images[j];
        html += `<div id="p${image.ix}" class="boxy" style="transform: translateX(${midx}px) translateY(${midy}px);width:${dw}px;height:${dh}px">
    <div pos="${image.ix}" id="b${image.ix}" class="blocky draggable"
style="width:${dw - 1}px; height:${dh - 1}px; z-index: 0">
<img style="width:100%; height:100%; object-fit: contain" src="https://s.tykcdn.com/i/susun//${image.img}"/>`;

        html += '</div></div>'

        midx -= p;
        midy += p;
    }
    box.innerHTML = html;

    setTimeout(() => {
        for (let j = 0; j < images.length; j++) {
            anime({
                targets: '#p' + images[j].ix,
                translateX: images[j].x,
                translateY: images[j].y,
                duration: 300
            });
        }
        timer();
    }, 250);
}

const interaction = interact('.draggable');
interaction.draggable({
    modifiers: [],
    listeners: {
        move: dragMoveListener,
        end: end
    }
});
interaction.on('dragstart', (event) => event.target.parentElement.style.zIndex = 2);
interaction.on('dragend', (event) => event.target.parentElement.style.zIndex = 0);

function end(event) {
    let target = event.target;
    let idx = parseInt(target.getAttribute("pos"));
    let el = target;
    let image = ix(idx);
    image.xm = 0;
    image.ym = 0;

    if (!done && hlIdx > 0 && !image.fix) {
        swap(idx, hlIdx);
    } else {
        anime({
            targets: '#b' + idx,
            translateX: [0],
            translateY: [0],
            duration: 300
        });
    }
}

function dragMoveListener(event) {
    if(done) return;
    let box = document.getElementById('puzzle-box');
    let bx = box.getBoundingClientRect().x;
    let by = box.getBoundingClientRect().y;

    let target = event.target;
    let idx = parseInt(target.getAttribute("pos"));

    let img = ix(idx);

    let x = img.xm + event.dx;
    let y = img.ym + event.dy;

    target.parentElement.style.zIndex = '2';
    target.style.transform = `translateX(${x}px) translateY(${y}px)`;
    img.xm = x;
    img.ym = y;

    let xc = event.pageX - bx;
    let yc = event.pageY - by;

    let imgH;
    for (let j = 0; j < images.length; j++) {
        if (!images[j].fix && images[j].ix != idx &&
            xc >= images[j].x &&
            xc < images[j].x + dw &&
            yc >= images[j].y &&
            yc < images[j].y + dh
        ) {
            imgH = images[j].ix;
            break;
        }
    }
    highlight(imgH);
}

function ix(ix) {
    for (let j = 0; j < images.length; j++) {
        if (ix == images[j].ix) return images[j];
    }
}
function xi(ix) {
    for (let j = 0; j < images.length; j++) {
        if (ix == images[j].ix) return j;
    }
}

function swap(a, b, speed = 300) {
    let imga = ix(a);
    let imgb = ix(b);

    let elA = document.getElementById('p' + a);
    let elB = document.getElementById('p' + b);

    elA.style.transform = `translateX(${imgb.x}px) translateY(${imgb.y}px)`;
    elB.style.transform = `translateX(${imga.x}px) translateY(${imga.y}px)`;

    let ia = {...imga};
    let ib = {...imgb};

    imgb.img = ia.img;
    imgb.ix = ia.ix;
    imga.img = ib.img;
    imga.ix = ib.ix;

    anime({
        targets: '#b' + a,
        translateX: [ia.xm == 0 ? ia.x - ib.x : ia.xm, 0],
        translateY: [ia.ym == 0 ? ia.y - ib.y : ia.ym, 0],
        duration: speed
    });
    anime({
        targets: '#b' + b,
        translateX: [imgb.x - imga.x, 0],
        translateY: [imgb.y - imga.y, 0],
        duration: speed
    });
    log += xi(a) + '-' + xi(b) + ",";
    highlight(undefined);
    checkImage();
}

function highlight(imgH) {
    if (hlIdx != imgH) {
        console.log('imgH', imgH);
        if (hlIdx) document.getElementById('p' + hlIdx).classList.remove('selected');
        else hlIdx = undefined;
        if (imgH) {
            document.getElementById('p' + imgH).classList.add('selected');
        }
        hlIdx = imgH;
    }
}

function checkImage() {
    for (let j = 1; j <= images.length; j++) {
        if (images[j - 1].ix != j) return;
    }
    rightAnswer();
}

function loose() {
    done = true;
    window.location.href = "answer?n=0&data=" + log;
}

function rightAnswer() {
    done = true;
    clearInterval(interval);
    setTimeout(function () {
        window.location.href = "answer?n=1&data=" + log;
    }, 1200);
}

function hint(btn) {
    if(done) return;
    if(hintCount <= 0) return;
    pause = true;
    btn.classList.remove('active');
    $.ajax('/hint').done((data)=>{
        if(data.ok){
            pause = false;
            let k = 0;
            while (k < 100) {
                let p = Math.floor(Math.random() * images.length) + 1;
                if (images[p - 1].ix != p) {
                    let d = images[p - 1].ix;
                    swap(p, d, 600);
                    ix(p).fix = true;
                    let el = document.getElementById('b' + p);
                    el.classList.remove('draggable');
                    el.innerHTML = el.innerHTML + '<div class="fixedd"></div>';
                    hintCount--;
                    let btnHi = document.getElementById('btnHi');
                    btnHi.innerHTML = hintCount;
                    if (hintCount > 0) btn.classList.add('active');
                    return;
                }
                k++;
            }
        }else{
            loose();//curang nih
        }
    });

}

window.dragMoveListener = dragMoveListener