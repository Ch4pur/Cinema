let flag = true;

function switchScroll() {
    if (flag) {

        window.scrollTo(0, 0);
        document.body.style.overflowY = "hidden";
    } else {
        document.body.style.overflowY = "auto";
    }
    flag = !(flag);
}