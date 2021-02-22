let today = new Date();
let day = today.getDate();
let month = today.getMonth() + 1;
let year = today.getFullYear();

if (day < 10) {
    day = '0' + day;
}
if (month < 10) {
    month = '0' + month;
}
let maxDate = (year - 12) + '-' + month + '-' + day;
let minDate = (year - 60) + '-' + month + '-' + day;

document.getElementById("birthday").setAttribute("max", maxDate);
document.getElementById("birthday").setAttribute("min", minDate);