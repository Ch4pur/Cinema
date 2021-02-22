window.onscroll = function() {
    myFunction()
};

// Get the navbar
let navbar = document.getElementById("body");

// Get the offset position of the navbar
let sticky = navbar.offsetTop;

// Add the sticky class to the navbar when you reach its scroll position. Remove "sticky" when you leave the scroll position
function myFunction() {
    if (window.pageYOffset >= 90) {
        navbar.classList.add("fix");
        alert("created");
    } else {
        navbar.classList.remove("fix");
        alert("removed");
    }
}