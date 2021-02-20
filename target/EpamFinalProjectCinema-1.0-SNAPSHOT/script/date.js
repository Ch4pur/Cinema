let today = new Date();
let day = today.getDay();
let month = today.getMonth();
let year = today.getFullYear();

if (day < 10) {
    day = '0' + day;
}
if (month < 10) {
    month = '0' + (month + 1);
}
let maxDate = (year - 12) + '-' + month + '-' + day;
let minDate = (year - 60) + '-' + month + '-' + day;

document.getElementById("birthday").setAttribute("max", maxDate);
document.getElementById("birthday").setAttribute("min", minDate);