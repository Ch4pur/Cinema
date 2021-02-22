

function is3d() {
    if (document.getElementById("isWith3d").checked) {
        document.getElementById("3d-image").src = "image/icon/3d-glasses.png";
    } else {
        document.getElementById("3d-image").src = "image/icon/not-3d-glasses.png";
    }
}

is3d();