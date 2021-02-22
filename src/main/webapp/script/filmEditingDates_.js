let today = new Date();
let day = today.getDate();
let month = today.getMonth();
let year = today.getFullYear();

if (day < 10) {
    day = '0' + day;
}
if (month < 10) {
    month = '0' + (month + 1);
}
let maxDate = year+ '-' + month + '-' + day;

document.getElementById("releaseDate").setAttribute("max", maxDate);
