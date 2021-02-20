const openMenu = document.querySelector('.open-button');

openMenu.addEventListener('click', (e) => {
    e.preventDefault();
    openMenu.nextElementSibling.classList.toggle('open-content--show')
});