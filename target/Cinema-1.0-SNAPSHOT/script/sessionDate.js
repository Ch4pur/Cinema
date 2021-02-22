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
let minDate = year + '-' + month + '-' + day;

let month2 = today.getMonth() + 2;
if (month2 < 10) {
    month2 = '0' + month2;
}

let maxDate = year  + '-' + month2 + '-' + day;
document.getElementById("date").setAttribute("max", maxDate);
document.getElementById("date").setAttribute("min", minDate);