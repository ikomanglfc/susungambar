let row = document.getElementsByClassName("lead-box-body")[0].getElementsByTagName("LI").length;
let userBox = document.querySelector('.lead-box-body-out');
let boxBody = document.querySelector('.lead-box-body');

if(row < 11){
    userBox.classList.add("hidden");
    userBox.classList.remove("block");
}
else{
    userBox.classList.add("block");
    userBox.classList.remove("hidden");
    boxBody.classList.add("pb-44");
}