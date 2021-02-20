let flag = true;

function switchScroll() {
    if (flag) {
        document.body.style.overflowY = "hidden";

    } else {
        document.body.style.overflowY = "auto";
    }
    flag = !(flag);
}