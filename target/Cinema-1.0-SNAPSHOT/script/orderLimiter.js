let numberOfSelected = 0;
let inputs = document.querySelectorAll("input[type='checkbox']");

for (let i = 0; i < inputs.length; i++) {
    if (inputs[i].checked) {
        numberOfSelected++;
    }
}


function checkLimit(checkbox) {
    numberOfSelected += checkbox.checked ? 1 : -1;
    let inputs = document.querySelectorAll("input[type='checkbox']");
    for (let i = 0; i < inputs.length; i++) {
        if (!inputs[i].checked) {
            inputs[i].disabled = numberOfSelected >= 6;
        }
    }
}