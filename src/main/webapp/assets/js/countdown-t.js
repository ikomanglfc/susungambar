function startCountDown(next) {
  var currentCount = $("#a");
  var Count = $("#Countdown");
  var nextCount, count = 1;
  var myInterval = setInterval(function () {
    if (count == 4) {
      clearInterval(myInterval);
      Count.hide();
      next();
    } else {
      count++;
      currentCount.hide();
      currentCount = currentCount.next();
      currentCount.show();
    }
  }, 1000);
}